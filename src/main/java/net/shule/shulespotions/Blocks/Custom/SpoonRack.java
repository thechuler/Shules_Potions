package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.shule.shulespotions.Blocks.Entities.SpoonRackBE;
import net.shule.shulespotions.Items.custom.SpoonItem;
import org.jetbrains.annotations.Nullable;

public class SpoonRack extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    protected static final VoxelShape NORTH_SHAPE =
            Block.box(0.0D, 12.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH_SHAPE =
            Block.box(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape WEST_SHAPE =
            Block.box(13.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_SHAPE =
            Block.box(0.0D, 12.0D, 0.0D, 3.0D, 16.0D, 16.0D);

    public SpoonRack(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {

        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!(blockEntity instanceof SpoonRackBE rack))
            return InteractionResult.PASS;

        ItemStack heldItem = player.getItemInHand(hand);

        // Insertar cuchara
        if (heldItem.getItem() instanceof SpoonItem) {

            if (rack.addSpoon(heldItem)) {

                    heldItem.shrink(1);

                return InteractionResult.CONSUME;
            }

            return InteractionResult.FAIL;
        }

        // Sacar cuchara con mano vacía
        if (heldItem.isEmpty()) {

            ItemStack removed = rack.removeLastSpoon();

            if (!removed.isEmpty()) {
                player.setItemInHand(hand, removed);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos,
                         BlockState newState, boolean isMoving) {

        if (!state.is(newState.getBlock())) {

            BlockEntity be = level.getBlockEntity(pos);

            if (be instanceof SpoonRackBE rack) {

                for (int i = 0; i < 3; i++) {

                    ItemStack stack = rack.getSpoon(i);

                    if (!stack.isEmpty()) {
                        Containers.dropItemStack(
                                level,
                                pos.getX(),
                                pos.getY(),
                                pos.getZ(),
                                stack
                        );
                    }
                }
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SpoonRackBE(pPos,pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch ((Direction) pState.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> EAST_SHAPE;
        };
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



}


