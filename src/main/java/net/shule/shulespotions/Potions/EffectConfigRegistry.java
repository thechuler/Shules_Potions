package net.shule.shulespotions.Potions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class EffectConfigRegistry {
    private static final Map<ResourceLocation, EffectConfig> CONFIGS = new HashMap<>();

    public static void clear() {
        CONFIGS.clear();
    }

    public static void register(ResourceLocation effect, EffectConfig config) {
        CONFIGS.put(effect, config);
    }

    public static EffectConfig get(MobEffect effect) {
        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        return get(id);
    }

    public static EffectConfig get(ResourceLocation effect) {
        if (effect == null) {
            return EffectConfig.DEFAULT;
        }
        return CONFIGS.getOrDefault(effect, EffectConfig.DEFAULT);
    }

    public static int getLevel(MobEffect effect) {
        return get(effect).level();
    }

    public static int getLevel(ResourceLocation effect) {
        return get(effect).level();
    }

    public static boolean isHidden(MobEffect effect) {
        return get(effect).hidden();
    }

    public static boolean isHidden(ResourceLocation effect) {
        return get(effect).hidden();
    }

    public static Map<ResourceLocation, EffectConfig> getAllConfigs() {
        return Map.copyOf(CONFIGS);
    }
}
