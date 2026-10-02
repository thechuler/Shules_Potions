package net.shule.shulespotions.Potions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class EffectLevelRegistry {
    private static final Map<ResourceLocation, Integer> EFFECT_LEVELS = new HashMap<>();

    public static void clear() {
        EFFECT_LEVELS.clear();
    }

    public static void register(ResourceLocation effect, int level) {
        EFFECT_LEVELS.put(effect, level);
    }

    public static int getLevel(MobEffect effect) {
        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        return EFFECT_LEVELS.getOrDefault(id, 1);
    }

    public static int getLevel(ResourceLocation effect) {
        return EFFECT_LEVELS.getOrDefault(effect, 1);
    }
}
