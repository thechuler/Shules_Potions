package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FrostedIceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class FrostWalkEffect extends MobEffect {
    public FrostWalkEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.onGround()) {
            BlockState blockstate = Blocks.FROSTED_ICE.defaultBlockState();
            int i = Math.min(16, 2 + pAmplifier);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(BlockPos blockpos : BlockPos.betweenClosed(pLivingEntity.blockPosition().offset(-i, -1, -i), pLivingEntity.blockPosition().offset(i, -1, i))) {
                if (blockpos.closerToCenterThan(pLivingEntity.position(), i)) {
                    blockpos$mutableblockpos.set(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                    BlockState blockstate1 = pLivingEntity.level().getBlockState(blockpos$mutableblockpos);
                    if (blockstate1.isAir()) {
                        BlockState blockstate2 = pLivingEntity.level().getBlockState(blockpos);
                        if (blockstate2 == FrostedIceBlock.meltsInto() && blockstate.canSurvive(pLivingEntity.level(), blockpos) && pLivingEntity.level()
                                .isUnobstructed(blockstate, blockpos, CollisionContext.empty())
                                && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(pLivingEntity,
                                net.minecraftforge.common.util.BlockSnapshot.create(pLivingEntity.level().dimension(), pLivingEntity.level(), blockpos),
                                net.minecraft.core.Direction.UP)) {

                            pLivingEntity.level().setBlockAndUpdate(blockpos, blockstate);
                            pLivingEntity.level().scheduleTick(blockpos, Blocks.FROSTED_ICE, Mth.nextInt(pLivingEntity.level().getRandom(), 60, 120));
                        }
                    }
                }
            }

        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }
}
