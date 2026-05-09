package net.shule.shulespotions.Potions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import java.util.Map;

public class IngredientStatLoader extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();

    public IngredientStatLoader() {
        super(GSON, "potion_ingredients");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object,
                         ResourceManager resourceManager,
                         ProfilerFiller profiler) {

        ItemStatRegistry.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {

            ResourceLocation fileId = entry.getKey();

            String path = fileId.getPath();

            String[] split = path.split("/", 2);

            if (split.length < 2) {
                System.out.println("Invalid ingredient path: " + fileId);
                continue;
            }

            ResourceLocation itemId =
                    ResourceLocation.fromNamespaceAndPath(split[0], split[1]);

            if (!BuiltInRegistries.ITEM.containsKey(itemId)) {
                System.out.println("[ShulesPotions] Unknown item: " + itemId);
                continue;
            }

            Item item = BuiltInRegistries.ITEM.get(itemId);


            IngredientStat stat = IngredientStat.CODEC
                    .parse(JsonOps.INSTANCE, entry.getValue())
                    .resultOrPartial(error ->
                            System.out.println("[ShulesPotions] " + error)
                    )
                    .orElse(null);

            if (stat == null) continue;

            ItemStatRegistry.register(item, stat);

            System.out.println("[ShulesPotions] Loaded stats for " + itemId);
        }
    }
}