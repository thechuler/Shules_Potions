package net.shule.shulespotions.Potions;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.shule.shulespotions.Fluids.PotionFluidHelper;
import org.joml.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PotionLiquidUtils {



    public static List<MobEffect> resolve(PotionLiquid pl) {

        Map<ResourceLocation, Integer> weights = pl.getStats().getEffectWeights();

        List<MobEffect> result = new ArrayList<>();

        Random random = new Random();

        for (Map.Entry<ResourceLocation, Integer> entry : weights.entrySet()) {

            int chance = entry.getValue();

            if (random.nextInt(100) < chance) {

                MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(entry.getKey());

                if (effect != null) {
                    result.add(effect);
                }
            }
        }

        return result;
    }




    public static int generatePotionColor(int purity, int vitality, int flavor, int stability) {

        // -----------------------------------
        // HASH DETERMINISTA
        // -----------------------------------

        long seed = 1L;

        seed = seed * 31L + purity;
        seed = seed * 31L + vitality;
        seed = seed * 31L + flavor;
        seed = seed * 31L + stability;

        Random random = new Random(seed);

        // -----------------------------------
        // HUE
        // -----------------------------------

        // Full random procedural
        float hue = random.nextFloat();

        // -----------------------------------
        // SATURATION
        // -----------------------------------

        // Siempre colores vivos
        float saturation = 0.65f + random.nextFloat() * 0.35f;

        // -----------------------------------
        // BRIGHTNESS
        // -----------------------------------

        float brightness = 0.7f + random.nextFloat() * 0.3f;

        // -----------------------------------
        // INFLUENCIA DE STATS
        // -----------------------------------

        // Purity positiva ilumina
        brightness += Math.max(0, purity) / 300f;

        // Purity negativa ensucia
        brightness -= Math.abs(Math.min(0, purity)) / 400f;

        // Stability negativa hace colores más violentos
        saturation += Math.abs(Math.min(0, stability)) / 300f;

        // Stability positiva suaviza un poco
        saturation -= Math.max(0, stability) / 500f;

        // Flavor mueve levemente el hue
        hue += flavor / 500f;

        // Vitality aumenta energía visual
        brightness += Math.abs(vitality) / 500f;

        // -----------------------------------

        hue = (hue % 1f + 1f) % 1f;

        saturation = clamp(saturation, 0.45f, 1f);
        brightness = clamp(brightness, 0.35f, 1f);

        return java.awt.Color.HSBtoRGB(
                hue,
                saturation,
                brightness
        );
    }



    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }




    public static List<MobEffect> getPossibleEffects(PotionLiquid pl) {
        return new ArrayList<>(pl.getStats()
                .getEffectWeights()
                .keySet()
                .stream()
                .map(BuiltInRegistries.MOB_EFFECT::get)
                .toList());
    }

    public static PotionLiquid getPotionLiquidFromStack(ItemStack stack) {

        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("SPPotionFluid")) return null;

        CompoundTag fluidTag = tag.getCompound("SPPotionFluid");

        FluidStack fluid = FluidStack.loadFluidStackFromNBT(fluidTag);

        return PotionFluidHelper.getPotionLiquid(fluid);
    }


    public static int getEffectChance(PotionLiquid potion, MobEffect effect) {

        ResourceLocation id =
                ForgeRegistries.MOB_EFFECTS.getKey(effect);

        if (id == null) {
            return 0;
        }

        return potion.getStats().getEffectWeight(id);
    }
}

