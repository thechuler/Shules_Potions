package net.shule.shulespotions.Blocks.Entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PotionLiquidTankBE extends BlockEntity {

    protected final FluidTank fluidTank;
    private final LazyOptional<IFluidHandler> lazyFluidHandler;

    public PotionLiquidTankBE(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity) {
        super(type, pos, state);
        
        this.fluidTank = new FluidTank(capacity) {
            @Override
            protected void onContentsChanged() {
                PotionLiquidTankBE.this.onContentsChanged();
            }
            
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return PotionLiquidTankBE.this.isFluidValid(stack);
            }
        };
        
        this.lazyFluidHandler = LazyOptional.of(() -> fluidTank);
    }
    
    protected boolean isFluidValid(FluidStack stack) {
        return true;
    }

    protected void onContentsChanged() {
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public FluidTank getTank() {
        return fluidTank;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        fluidTank.readFromNBT(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        fluidTank.writeToNBT(nbt);
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
    }
}
