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
            try {
                if (!fileEntry.getValue().isJsonObject()) {
                    System.out.println("Skipping file " + fileEntry.getKey() + " as root is not a JsonObject");
                    continue;
                }

                JsonObject root = fileEntry.getValue().getAsJsonObject();

                if (root.has("forge:conditions")) {
                    JsonElement conditionsElement = root.get("forge:conditions");
                    if (!conditionsElement.isJsonArray()) {
                        System.out.println("Skipping file " + fileEntry.getKey() + " as forge:conditions is not a JsonArray");
                        continue;
                    }
                    if (!net.minecraftforge.common.crafting.CraftingHelper.processConditions(conditionsElement.getAsJsonArray(), net.minecraftforge.common.crafting.conditions.ICondition.IContext.EMPTY)) {
                        System.out.println("Skipping file " + fileEntry.getKey() + " due to Forge conditions not met");
                        continue;
                    }
                }

                for (Map.Entry<String, JsonElement> ingredientEntry : root.entrySet()) {
                    if (ingredientEntry.getKey().equals("forge:conditions")) {
                        continue;
                    }

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
            } catch (Exception e) {
                System.err.println("Error parsing ingredient stats file: " + fileEntry.getKey());
                e.printStackTrace();
            }
        }
    }
}