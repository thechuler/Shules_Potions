package net.shule.shulespotions.Blocks.Custom;



import net.minecraft.core.BlockPos;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.shule.shulespotions.Blocks.Entities.PotionSplashBE;
import org.jetbrains.annotations.Nullable;



public class PotionSplashBlock extends MultifaceBlock implements EntityBlock {



    private final MultifaceSpreader spreader =
            new MultifaceSpreader(this);
    public PotionSplashBlock(Properties pProperties) {
        super(pProperties);

    }



    @Override
    public MultifaceSpreader getSpreader() {
        return spreader;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PotionSplashBE(pPos, pState);
    }


}
