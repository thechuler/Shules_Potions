package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;


import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;

import net.shule.shulespotions.Blocks.ModBlockEntities;
import net.shule.shulespotions.Particles.ModParticles;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.Potions.PotionLiquidUtils;
import net.shule.shulespotions.util.CauldronState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.shule.shulespotions.util.ColorUtils.intToRGB;


public class PotionCauldron extends BaseEntityBlock {

    private final int MAX_INGREDIENT_COUNT;
    private final int MAX_LIQUID_LEVEL;

    public static final EnumProperty<CauldronState> STATE = EnumProperty.create("state", CauldronState.class);

    private static final VoxelShape INSIDE = box(2.0D, 3.0D, 2.0D, 14.0D, 15.0D, 14.0D);

    protected static final VoxelShape SHAPE = Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE);

    public PotionCauldron(Properties pProperties, int maxIngredientCount, int maxLiquidLevel) {
        super(pProperties);
        MAX_INGREDIENT_COUNT = maxIngredientCount;
        MAX_LIQUID_LEVEL = maxLiquidLevel;
        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, CauldronState.BASE));
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STATE);
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
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        ItemStack stack = pPlayer.getItemInHand(pHand);


        if (stack.is(Items.WATER_BUCKET)) {

            if (!pLevel.isClientSide()) {

                BlockEntity be = pLevel.getBlockEntity(pPos);

                if (be instanceof PotionCauldronBE cauldron) {

                    cauldron.setLiquidLevel(MAX_LIQUID_LEVEL);

                    if (!pPlayer.getAbilities().instabuild) {
                        pPlayer.setItemInHand(pHand, new ItemStack(Items.BUCKET));
                    }

                    cauldron.setChanged();
                    cauldron.sync();
                }
            }


            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        }


        return InteractionResult.PASS;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) return;

        if (!(entity instanceof ItemEntity itemEntity)) return;

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PotionCauldronBE cauldron) {

            if(cauldron.getLiquidLevel() < 1 || cauldron.getIngredients().size() >= MAX_INGREDIENT_COUNT) return;

            cauldron.checkItem(itemEntity.getItem());
            itemEntity.discard();
        }
    }


    public int getMAX_INGREDIENT_COUNT() {
        return MAX_INGREDIENT_COUNT;
    }


    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level pLevel,
            BlockState pState,
            BlockEntityType<T> pBlockEntityType
    ) {
        return (lvl, pos, st, be) -> {

            if (!(be instanceof PotionCauldronBE cauldron)) return;


            PotionCauldronBE.tick(lvl, cauldron);


            if(cauldron.getLiquidLevel() < 1 ||
                    cauldron.getIngredients().size() >= MAX_INGREDIENT_COUNT) {
                return;
            }


            if (lvl.isClientSide) {

                int color = cauldron.getRenderColor();
                float[] rgb = intToRGB(color);

                float chance = 0.4f;

                if (lvl.random.nextFloat() < chance) {

                    double offsetX = (lvl.random.nextDouble() - 0.5) * 0.5;
                    double offsetZ = (lvl.random.nextDouble() - 0.5) * 0.5;

                    lvl.addParticle(
                            ModParticles.BUBBLE.get(),
                            pos.getX() + 0.5 + offsetX,
                            pos.getY() + 1.0,
                            pos.getZ() + 0.5 + offsetZ,
                            rgb[0],
                            rgb[1],
                            rgb[2]
                    );
                }
            }
        };
    }
    }












