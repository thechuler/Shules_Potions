package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BigCauldronPart extends Block {

    public static final IntegerProperty OFFSET_X = IntegerProperty.create("offset_x", 0, 2);

    public static final IntegerProperty OFFSET_Y = IntegerProperty.create("offset_y", 0, 1);

    public static final IntegerProperty OFFSET_Z = IntegerProperty.create("offset_z", 0, 2);

    public BigCauldronPart(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(OFFSET_X, 1)
                .setValue(OFFSET_Y, 0)
                .setValue(OFFSET_Z, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    public BlockPos getCorePos(BlockPos pos, BlockState state) {
        int x = state.getValue(OFFSET_X) - 1;
        int y = state.getValue(OFFSET_Y);
        int z = state.getValue(OFFSET_Z) - 1;
        return pos.offset(-x, -y, -z);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockPos corePos = getCorePos(pos, state);
        BlockState coreState = level.getBlockState(corePos);
        if (coreState.getBlock() instanceof BigCauldronCore) {
            return coreState.use(level, player, hand, hit.withPosition(corePos));
        }
        return InteractionResult.PASS;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos corePos = getCorePos(pos, state);
        BlockState coreState = level.getBlockState(corePos);
        if (coreState.getBlock() instanceof BigCauldronCore) {
            level.destroyBlock(corePos, !player.isCreative());
        }
        super.playerWillDestroy(level, pos, state, player);
    }


    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.Entity entity) {
        BlockPos corePos = getCorePos(pos, state);
        BlockState coreState = level.getBlockState(corePos);
        if (coreState.getBlock() instanceof BigCauldronCore) {
            coreState.entityInside(level, corePos, entity);
        }
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int x = state.getValue(OFFSET_X) - 1;
        int y = state.getValue(OFFSET_Y);
        int z = state.getValue(OFFSET_Z) - 1;


        if (x == 0 && z == 0 && y == 1) {
            return Shapes.empty();
        }

        return Shapes.block();
    }


    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }
}
