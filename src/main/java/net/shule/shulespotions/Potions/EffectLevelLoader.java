package net.shule.shulespotions.Potions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class EffectLevelLoader extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();

    public EffectLevelLoader() {
        super(GSON, "potion_effect_levels");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object,
                         ResourceManager resourceManager,
                         ProfilerFiller profiler) {

        EffectLevelRegistry.clear();

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
                    if (!CraftingHelper.processConditions(conditionsElement.getAsJsonArray(), ICondition.IContext.EMPTY)) {
                        System.out.println("Skipping file " + fileEntry.getKey() + " due to Forge conditions not met");
                        continue;
                    }
                }

                for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                    if (entry.getKey().equals("forge:conditions")) {
                        continue;
                    }

                    ResourceLocation effectId = ResourceLocation.tryParse(entry.getKey());

                    if (effectId == null) {
                        System.out.println("Invalid effect id: " + entry.getKey());
                        continue;
                    }

                    if (!ForgeRegistries.MOB_EFFECTS.containsKey(effectId)) {
                        System.out.println("Unknown effect: " + effectId);
                        continue;
                    }

                    if (entry.getValue().isJsonPrimitive() && entry.getValue().getAsJsonPrimitive().isNumber()) {
                        int level = entry.getValue().getAsInt();
                        EffectLevelRegistry.register(effectId, level);
                        System.out.println("Loaded level " + level + " for effect " + effectId);
                    } else if (entry.getValue().isJsonObject() && entry.getValue().getAsJsonObject().has("level")) {
                        int level = entry.getValue().getAsJsonObject().get("level").getAsInt();
                        EffectLevelRegistry.register(effectId, level);
                        System.out.println("Loaded level " + level + " for effect " + effectId);
                    } else {
                        System.out.println("Invalid level format for effect: " + effectId);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error parsing effect levels file: " + fileEntry.getKey());
                e.printStackTrace();
            }
        }
    }
}
