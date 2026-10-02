package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.Events.CameraShakeHandler;
import net.shule.shulespotions.Items.AncientKey;
import net.shule.shulespotions.Sounds.ModSounds;

import java.util.ArrayList;
import java.util.List;

public class AnciteLock extends Block {
    public AnciteLock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

       if(pPlayer.getItemInHand(pHand).getItem() instanceof AncientKey) {
           pPlayer.getItemInHand(pHand).shrink(1);
           DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                   CameraShakeHandler.triggerShake(pPos.getX(), pPos.getY(), pPos.getZ(), 40, 1.4f));

           pLevel.playSound(pPlayer, pPos, ModSounds.CRUMBLING.get(), SoundSource.AMBIENT, 10, 1);
           if (!pLevel.isClientSide) {
               pLevel.scheduleTick(pPos, this, 2);
           }
       }
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        List<BlockPos> validNeighbors = new ArrayList<>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 2; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;

                    BlockPos neighbor = pPos.offset(x, y, z);
                    BlockState neighborState = pLevel.getBlockState(neighbor);
                    
                    if (neighborState.is(ModBlocks.ANCITE_BLOCKING_WALL.get()) ||
                            neighborState.is(ModBlocks.ANCITE_LOCK.get())) {
                        validNeighbors.add(neighbor);
                    }
                }
            }
        }

        if (!validNeighbors.isEmpty()) {
            BlockPos toBreak = validNeighbors.get(pRandom.nextInt(validNeighbors.size()));
            pLevel.destroyBlock(toBreak, false);
            pLevel.scheduleTick(pPos, this, 3);
        } else {
            pLevel.destroyBlock(pPos, false);
        }
    }
}
