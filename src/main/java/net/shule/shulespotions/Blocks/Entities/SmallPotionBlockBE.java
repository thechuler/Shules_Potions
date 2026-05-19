package net.shule.shulespotions.Blocks.Entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.shule.shulespotions.Blocks.ModBlockEntities;

public class SmallPotionBlockBE extends BlockEntity {

    private FluidStack fluid = FluidStack.EMPTY;

    public SmallPotionBlockBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SMALL_POTION_BE.get(), pos, state);
    }

    public void setFluid(FluidStack fluid) {
        this.fluid = fluid;
        setChanged();
    }

    public FluidStack getFluid() {
        return fluid;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if(!fluid.isEmpty()) {
            tag.put("Fluid", fluid.writeToNBT(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if(tag.contains("Fluid")) {
            fluid = FluidStack.loadFluidStackFromNBT(
                    tag.getCompound("Fluid")
            );
        }
    }
}