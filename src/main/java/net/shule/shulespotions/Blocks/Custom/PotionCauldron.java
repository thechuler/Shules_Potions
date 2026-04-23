package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;


import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.shule.shulespotions.Items.custom.PotionBarrel;
import net.shule.shulespotions.Items.custom.PotionLiquidBottleItem;
import net.shule.shulespotions.Items.custom.RecipeSheetItem;
import net.shule.shulespotions.Particles.ModParticles;
import net.shule.shulespotions.util.CauldronActions.CauldronActionContext;
import net.shule.shulespotions.util.CauldronActions.CauldronActionTrigger;
import net.shule.shulespotions.util.CauldronState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import static net.shule.shulespotions.util.ColorUtils.intToRGB;


public class PotionCauldron extends BaseEntityBlock {

    public static final EnumProperty<CauldronState> STATE = EnumProperty.create("state", CauldronState.class);

    private static final VoxelShape INSIDE = box(2.0D, 3.0D, 2.0D, 14.0D, 15.0D, 14.0D);

    protected static final VoxelShape SHAPE = Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE);

    public PotionCauldron(Properties pProperties) {
        super(pProperties);
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
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (lvl, pos, st, be) -> {
            if (be instanceof PotionCauldronBE cauldron) {


                if (!lvl.isClientSide) {
                    PotionCauldronBE.tick(lvl, pos, st, cauldron);
                }


                if (lvl.isClientSide) {
                    int color = cauldron.getColor();
                    float[] rgb = intToRGB(color);
                    float chance = cauldron.IsRecipeFinished() ? 0.4f :0.5f; //Esto  hay que cambiarlo nunca va a ser true


                    if (lvl.random.nextFloat() < chance && cauldron.HasRecipe() && cauldron.getCurrentRecipeAction() > 0) {
                        double offsetX = (lvl.random.nextDouble() - 0.5) * 0.5; // ancho
                        double offsetZ = (lvl.random.nextDouble() - 0.5) * 0.5; //largo

                        lvl.addParticle(ModParticles.BUBBLE.get(), pos.getX() + 0.5 + offsetX, pos.getY() + 1.0, pos.getZ() + 0.5 + offsetZ, rgb[0], rgb[1], rgb[2]);
                    }

                }
            }
        };
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack item = pPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        if (!pLevel.isClientSide()) {
            BlockEntity CauldronBe = pLevel.getBlockEntity(pPos);

            if (CauldronBe instanceof PotionCauldronBE cauldron) {


                if (cauldron.hasPotionLiquid() && item.getItem() instanceof PotionLiquidBottleItem)
                    return InteractionResult.PASS;


                BlockState state = pState;
                CauldronState current = state.getValue(STATE);

                if (current == CauldronState.BASE && item.is(Items.FLINT_AND_STEEL)) {
                    state = state.setValue(STATE, CauldronState.HOT);
                }

                if (item.is(Items.WOODEN_SHOVEL)) {
                    state = state.setValue(STATE, CauldronState.BASE);
                }

                if (item.is(Items.ICE)) {
                    state = state.setValue(STATE, CauldronState.COLD);
                }

                if (state != pState) {
                    pLevel.setBlock(pPos, state, Block.UPDATE_CLIENTS);
                }

                if (item.getItem() instanceof RecipeSheetItem recipe) {

                    cauldron.setRecipeId(recipe.getRecipeId());
                    cauldron.removePotionLiquid();
                    cauldron.setRecipeFinished(false);
                    cauldron.removePotionLiquid();
                    cauldron.setLiquidLevel(0);
                    cauldron.setCurrentRecipeAction(0);


                    if (pLevel instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ModParticles.BUBBLE.get(), pPos.getX() + 0.5, pPos.getY() + 1.2, pPos.getZ() + 0.5, 20, 0.3, 0.3, 0.3, 0.01);
                    }

                    //Si no esta en creativo consume la receta
                    if (!pPlayer.getAbilities().instabuild) {
                        item.shrink(1);
                    }

                } else {
                    if (cauldron.HasRecipe()) {
                        cauldron.handleTrigger(CauldronActionTrigger.RIGHT_CLICK, new CauldronActionContext(cauldron, pPlayer, null));
                        pLevel.sendBlockUpdated(pPos, pState, pState, 3);
                    }
                }
            }


        }
        return InteractionResult.SUCCESS;
    }


    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) return;

        if (!(entity instanceof ItemEntity itemEntity)) return;

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PotionCauldronBE cauldron) {

            cauldron.handleTrigger(CauldronActionTrigger.ITEM_INSIDE, new CauldronActionContext(cauldron, null, itemEntity));
        }
    }
}







