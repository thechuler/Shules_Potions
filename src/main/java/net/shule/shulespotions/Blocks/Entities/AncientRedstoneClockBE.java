package net.shule.shulespotions.Blocks.Entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.shule.shulespotions.Blocks.Custom.AncientRedstoneClock;
import net.shule.shulespotions.Blocks.ModBlockEntities;

public class AncientRedstoneClockBE extends BlockEntity {
    private int intervalInSeconds = 1;
    private int tickCounter = 0;

    public AncientRedstoneClockBE(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ANCIENT_REDSTONE_CLOCK_BE.get(), pPos, pBlockState);
    }

    public int incrementInterval() {
        this.intervalInSeconds++;
        if (this.intervalInSeconds > 5) {
            this.intervalInSeconds = 1;
        }
        this.setChanged();
        return this.intervalInSeconds;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("IntervalInSeconds", this.intervalInSeconds);
        pTag.putInt("TickCounter", this.tickCounter);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.intervalInSeconds = pTag.getInt("IntervalInSeconds");
        if (this.intervalInSeconds < 1) this.intervalInSeconds = 1;
        this.tickCounter = pTag.getInt("TickCounter");
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, AncientRedstoneClockBE pBlockEntity) {
        pBlockEntity.tickCounter++;
        if (pBlockEntity.tickCounter >= pBlockEntity.intervalInSeconds * 20) {
            pBlockEntity.tickCounter = 0;
            
            if (!pState.getValue(AncientRedstoneClock.POWERED)) {
                pLevel.setBlock(pPos, pState.setValue(AncientRedstoneClock.POWERED, true), 3);
                pLevel.updateNeighborsAt(pPos, pState.getBlock());
                pLevel.scheduleTick(pPos, pState.getBlock(), 2);
            }
        }
    }
}
