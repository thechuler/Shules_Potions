package net.shule.shulespotions.Potions;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.shule.shulespotions.Fluids.PotionFluidHelper;
import net.shule.shulespotions.util.ColorUtils;
import org.joml.Random;

import java.awt.*;
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




    public static int generatePotionColor(PotionLiquid potion) {

        int purity = potion.getStats().getPurity();
        int vitality = potion.getStats().getVitality() + 100;
        int flavor = potion.getStats().getFlavor() + 100;
        int stability = potion.getStats().getStability() + 100;
        int power = potion.getPower();

        float hue =
                (purity * 2f +
                        vitality * 3f +
                        flavor * 5f +
                        stability * 7f +
                        power * 11f) % 360f;

        float saturation = 0.6f + (vitality / 200f) * 0.4f;
        float brightness = 0.6f + (stability / 200f) * 0.4f;

        return Color.HSBtoRGB(
                hue / 360f,
                Math.min(saturation, 1f),
                Math.min(brightness, 1f)
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

