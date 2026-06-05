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




    public static int generatePotionColor(PotionLiquid potion) {

        IngredientStat stats = potion.getStats();

        long hash = 0;

        hash = hash * 31 + potion.getDuration();
        hash = hash * 31 + potion.getPower();

        hash = hash * 31 + stats.getPurity();
        hash = hash * 31 + stats.getVitality();
        hash = hash * 31 + stats.getFlavor();
        hash = hash * 31 + stats.getStability();

        int r = (int)((hash >> 0) & 0xFF);
        int g = (int)((hash >> 8) & 0xFF);
        int b = (int)((hash >> 16) & 0xFF);

        // Evitar colores demasiado oscuros
        r = (r + 128) / 2;
        g = (g + 128) / 2;
        b = (b + 128) / 2;

        return (r << 16) | (g << 8) | b;
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

