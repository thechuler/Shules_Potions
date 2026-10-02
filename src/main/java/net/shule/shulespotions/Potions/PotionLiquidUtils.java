package net.shule.shulespotions.Potions;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.shule.shulespotions.Fluids.PotionFluidHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PotionLiquidUtils {



    public static List<MobEffect> resolve(PotionLiquid pl, int cauldronLevel, int maxEffectCount) {

        Map<ResourceLocation, Integer> weights = pl.getStats().getEffectWeights();
        List<MobEffect> result = new ArrayList<>();
        java.util.Random random = new java.util.Random();

        List<Map.Entry<ResourceLocation, Integer>> sortedEntries = new ArrayList<>(weights.entrySet());
        sortedEntries.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

        int limit = Math.min(sortedEntries.size(), maxEffectCount);

        for (int i = 0; i < limit; i++) {
            Map.Entry<ResourceLocation, Integer> entry = sortedEntries.get(i);
            int chance = entry.getValue();

            if (random.nextInt(100) < chance) {
                MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(entry.getKey());
                if (effect != null) {
                    if (EffectLevelRegistry.getLevel(effect) <= cauldronLevel) {
                        result.add(effect);
                    }
                }
            }
        }

        return result;
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
        if (tag == null || !tag.contains(net.shule.shulespotions.Items.custom.PotionLiquidTankItem.FLUID_TAG)) return null;

        CompoundTag fluidTag = tag.getCompound(net.shule.shulespotions.Items.custom.PotionLiquidTankItem.FLUID_TAG);

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

