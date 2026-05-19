package net.shule.shulespotions.Fluids;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import net.shule.shulespotions.Potions.PotionLiquid;

public class PotionFluidHelper {

    private static final String TAG_KEY = "PotionLiquid";

    public static FluidStack withPotionLiquid(FluidStack stack, PotionLiquid pl) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put(TAG_KEY, pl.save());
        return stack;
    }

    public static PotionLiquid getPotionLiquid(FluidStack stack) {
        if (!stack.hasTag()) return new PotionLiquid();

        CompoundTag tag = stack.getTag();

        if (tag != null && tag.contains(TAG_KEY)) {
            return PotionLiquid.load(tag.getCompound(TAG_KEY));
        }

        return new PotionLiquid();
    }
}