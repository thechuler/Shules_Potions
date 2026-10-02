package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.shule.shulespotions.Blocks.ModBlocks;

public class InstableAnciteBrick extends Block {
    public InstableAnciteBrick(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!pLevel.isClientSide && pEntity instanceof Player) {
            spreadDestruction(pPos, pLevel);
        }
        super.stepOn(pLevel, pPos, pState, pEntity);
    }


    public static void spreadDestruction(BlockPos pPos, Level pLevel) {
        BlockPos belowBlock = pPos.below();

        if (pLevel.getBlockState(belowBlock).isAir()) {
            pLevel.destroyBlock(pPos, false);

            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && z == 0) continue;

                    BlockPos neighbor = pPos.offset(x, 0, z);
                    if (pLevel.getBlockState(neighbor).is(ModBlocks.INSTABLE_ANCITE_BRICKS.get())) {
                        spreadDestruction(neighbor, pLevel);
                    }
                }
            }
        }
    }
}