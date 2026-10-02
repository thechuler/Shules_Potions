package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;


import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;

import org.jetbrains.annotations.Nullable;


public class PotionCauldron extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private final int MAX_INGREDIENT_COUNT;
    private final int MAX_EFFECT_COUNT;
    private final int cauldronLevel;

    private static final VoxelShape INSIDE = box(2.0D, 3.0D, 2.0D, 14.0D, 15.0D, 14.0D);

   protected static final VoxelShape SHAPE = Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE);

    public PotionCauldron(Properties pProperties, int maxIngredientCount, int maxEffectCount, int maxLiquidLevel, int cauldronLevel) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        MAX_INGREDIENT_COUNT = maxIngredientCount;
        MAX_EFFECT_COUNT = maxEffectCount;
        this.cauldronLevel = cauldronLevel;
    }

    public int getCauldronLevel() {
        return cauldronLevel;
    }

    public int getMAX_EFFECT_COUNT() {
        return MAX_EFFECT_COUNT;
    }



    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
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
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PotionCauldronBE(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {


        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(Items.WATER_BUCKET)) {

            if (!level.isClientSide()) {

                BlockEntity be = level.getBlockEntity(pos);

                if (be instanceof PotionCauldronBE cauldron) {

                    FluidStack water = new FluidStack(
                            Fluids.WATER,
                            1000
                    );

                    int filled = cauldron.getTank().fill(
                            water,
                            IFluidHandler.FluidAction.EXECUTE
                    );

                    if (filled == 1000) {

                        if (!player.getAbilities().instabuild) {
                            player.setItemInHand(
                                    hand,
                                    new ItemStack(Items.BUCKET)
                            );
                        }

                        cauldron.setChanged();
                        cauldron.sync();

                        level.playSound(
                                null,
                                pos,
                                SoundEvents.BUCKET_FILL,
                                SoundSource.BLOCKS,
                                1.0F,
                                1.0F
                        );
                    }
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }







        return InteractionResult.PASS;
    }




    public AABB getItemCaptureAABB(BlockPos pos) {
        return new AABB(pos).move(0, 0.2, 0).deflate(0.2, 0.2, 0.2);
    }


    public int getMAX_INGREDIENT_COUNT() {
        return MAX_INGREDIENT_COUNT;
    }



    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (lvl, pos, st, be) -> {

            if (!(be instanceof PotionCauldronBE cauldron)) return;


            PotionCauldronBE.tick(lvl, cauldron);


            if (lvl.isClientSide) {
                cauldron.clientTick();

            }
        };
    }



    public float getLiquidMinY() { return 0.2f; }
    public float getLiquidMaxY() { return 0.9f; }
    public float getLiquidDropOffset() { return 0.35f; }
    public float getLiquidWidthMin() { return 0.1f; }
    public float getLiquidWidthMax() { return 0.9f; }
    public float getItemRotationRadius() { return 0.25f; }
    public float getBeamWidth() { return 0.35f; }
    public float getBeamLength() { return 4.5f; }


    public float getBubbleParticleChance() { return 0.4f; }
    public float getBubbleParticleSpread() { return 0.5f; } // Multiplicador para offsetX/offsetZ
    public float getBubbleParticleY() { return 1.0f; }

    public int getExplosionParticleCount() { return 200; }
    public double getExplosionParticleRadius() { return 2.5; }
    public double getExplosionParticleHeightSpread() { return 1.8; }
    public double getExplosionParticleY() { return 1.0; }
}












