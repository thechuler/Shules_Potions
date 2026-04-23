package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RecipeLectern extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;


    protected static final VoxelShape SHAPE = Shapes.or(
            box(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D),
            box(6.0D, 2.0D, 6.0D, 10.0D, 10.0D, 10.0D),
            box(2.0D, 10.0D, 2.0D, 14.0D, 14.0D, 14.0D)
    );

    public RecipeLectern(Properties pProperties) {
        super(pProperties);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }


    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }


    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

}
