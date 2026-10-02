package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.shule.shulespotions.Blocks.Entities.SpitterTrapBE;
import net.shule.shulespotions.Blocks.ModBlockEntities;
import net.shule.shulespotions.Items.custom.PotionLiquidBottleItem;
import org.jetbrains.annotations.Nullable;

public class SpitterTrap extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    public static final BooleanProperty SPITTING = BooleanProperty.create("spitting");
    private static final int TRIGGER_DELAY = 4;

    public SpitterTrap(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(TRIGGERED, Boolean.FALSE)
                .setValue(SPITTING, Boolean.FALSE));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpitterTrapBE(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.SPITTER_TRAP_BE.get(), SpitterTrapBE::tick);
    }


    private boolean hasBackSignal(Level level, BlockPos pos, Direction facing) {
        Direction back = facing.getOpposite();
        BlockPos backPos = pos.relative(back);
        return level.getSignal(backPos, facing) > 0 
                || level.hasSignal(backPos, facing) 
                || level.getDirectSignal(backPos, facing) > 0;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            boolean hasSignal = hasBackSignal(level, pos, state.getValue(FACING));
            boolean isTriggered = state.getValue(TRIGGERED);

            if (hasSignal && !isTriggered) {
                level.scheduleTick(pos, this, TRIGGER_DELAY);
                level.setBlock(pos, state.setValue(TRIGGERED, Boolean.TRUE).setValue(SPITTING, Boolean.TRUE), Block.UPDATE_CLIENTS);
                if (level.getBlockEntity(pos) instanceof SpitterTrapBE be) {
                    be.startAnimationTimer(20);
                }
            } else if (!hasSignal && isTriggered) {
                level.setBlock(pos, state.setValue(TRIGGERED, Boolean.FALSE), Block.UPDATE_CLIENTS);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof SpitterTrapBE be) {
            be.shoot(level, pos, state.getValue(FACING));
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isCreative()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof PotionLiquidBottleItem bottle) {
                if (bottle.hasFluid(stack)) {
                    if (!level.isClientSide) {
                        if (level.getBlockEntity(pos) instanceof SpitterTrapBE be) {
                            ItemStack copy = stack.copy();
                            copy.setCount(1);
                            be.setPotionData(copy);
                            level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
                            player.displayClientMessage(Component.literal("Potion Added"), true);
                        }
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED, SPITTING);
    }
}
