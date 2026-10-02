package net.shule.shulespotions.Blocks.Entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.shule.shulespotions.Blocks.ModBlockEntities;
import net.shule.shulespotions.Events.CameraShakeHandler;
import net.shule.shulespotions.Sounds.ModSounds;

public class ElectionAncientPedestalBE extends AncientPedestalBE {
    public ElectionAncientPedestalBE(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ELECTION_ANCIENT_PEDESTAL_BE.get(), pPos, pBlockState);
    }

    @Override
    public double getItemRenderYOffset() {
        return 1.45D;
    }

    @Override
    public void OnItemRemoved() {
        if (level instanceof ServerLevel serverLevel) {
            int radius = 8;
            BlockPos minPos = worldPosition.offset(-radius, -radius, -radius);
            BlockPos maxPos = worldPosition.offset(radius, radius, radius);

            for (BlockPos targetPos : BlockPos.betweenClosed(minPos, maxPos)) {
                if (targetPos.equals(worldPosition)) continue;

                BlockEntity be = serverLevel.getBlockEntity(targetPos);
                if (be instanceof ElectionAncientPedestalBE otherPedestal) {
                    if (otherPedestal.hasItem()) {
                        otherPedestal.burnItem();
                    }
                }
            }
            serverLevel.playSound(null, worldPosition, ModSounds.SELECTION.get(), SoundSource.BLOCKS, 3f, 1f);
          DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                    CameraShakeHandler.triggerShake(this.getBlockPos().getX(),this.getBlockPos().getY(), getBlockPos().getZ(), 20, 1.0f));
        }
    }

    public void burnItem() {
        if (!this.hasItem()) return;

        this.clearItem();

        if (level instanceof ServerLevel serverLevel) {
            double x = worldPosition.getX() + 0.5D;
            double y = worldPosition.getY() + 1.2D;
            double z = worldPosition.getZ() + 0.5D;

            for (int i = 0; i < 4; i++) {
                serverLevel.sendParticles(ParticleTypes.FLAME, x, y + (i * 0.4D), z, 12, 0.08D, 0.1D, 0.08D, 0.03D);
                serverLevel.sendParticles(ParticleTypes.SMOKE, x, y + (i * 0.4D), z, 8, 0.08D, 0.1D, 0.08D, 0.02D);
            }

            serverLevel.playSound(null, worldPosition, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.6f, 0.8f);
        }
    }
}