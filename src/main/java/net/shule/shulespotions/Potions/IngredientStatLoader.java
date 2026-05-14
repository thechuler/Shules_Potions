package net.shule.shulespotions.Potions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

        for (Map.Entry<ResourceLocation, JsonElement> fileEntry : object.entrySet()) {

            JsonObject root = fileEntry.getValue().getAsJsonObject();

            for (Map.Entry<String, JsonElement> ingredientEntry : root.entrySet()) {

                ResourceLocation itemId =
                        ResourceLocation.tryParse(ingredientEntry.getKey());

                if (itemId == null) {
                    System.out.println("Invalid item id: " + ingredientEntry.getKey());
                    continue;
                }

                if (!BuiltInRegistries.ITEM.containsKey(itemId)) {
                    System.out.println("Unknown item: " + itemId);
                    continue;
                }

                Item item = BuiltInRegistries.ITEM.get(itemId);

                IngredientStat stat = IngredientStat.CODEC
                        .parse(JsonOps.INSTANCE, ingredientEntry.getValue())
                        .resultOrPartial(System.out::println)
                        .orElse(null);

                if (stat == null) continue;

                ItemStatRegistry.register(item, stat);

                System.out.println("Loaded stats for " + itemId);
            }
        }
    }
}