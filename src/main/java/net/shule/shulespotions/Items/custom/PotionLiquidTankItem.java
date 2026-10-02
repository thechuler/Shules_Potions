package net.shule.shulespotions.Items.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class PotionLiquidTankItem extends Item {

    public static final String FLUID_TAG = "StoredFluid";

    public PotionLiquidTankItem(Properties properties) {
        super(properties);
    }

    public void setFluid(ItemStack stack, FluidStack fluid) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put(FLUID_TAG, fluid.writeToNBT(new CompoundTag()));
    }

    public FluidStack getFluid(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(FLUID_TAG)) {
            return FluidStack.EMPTY;
        }
        return FluidStack.loadFluidStackFromNBT(tag.getCompound(FLUID_TAG));
    }

    public boolean hasFluid(ItemStack stack) {
        return !getFluid(stack).isEmpty();
    }

    public void removeFluid(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            tag.remove(FLUID_TAG);
        }
    }
}
