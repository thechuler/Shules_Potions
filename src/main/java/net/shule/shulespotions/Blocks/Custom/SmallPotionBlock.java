package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.shule.shulespotions.Blocks.Entities.SmallPotionBlockBE;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmallPotionBlock extends BaseEntityBlock {


    private static final VoxelShape SHAPE =
            box(7, 0, 7, 9, 7, 9);

    public SmallPotionBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SmallPotionBlockBE(pos, state);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state,
                                        BlockGetter level,
                                        BlockPos pos,
                                        CollisionContext context) {

        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state,
                                                 BlockGetter level,
                                                 BlockPos pos,
                                                 CollisionContext context) {

        return SHAPE;
    }
}