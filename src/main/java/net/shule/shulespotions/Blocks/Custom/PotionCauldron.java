package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;


import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;

import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;
import net.shule.shulespotions.Items.custom.PotionRecipeItem;
import org.jetbrains.annotations.Nullable;


public class PotionCauldron extends BaseEntityBlock {
    private static final VoxelShape INSIDE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), BooleanOp.ONLY_FIRST);

    public PotionCauldron(Properties pProperties) {
        super(pProperties);
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


    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack item = pPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        if (!pLevel.isClientSide()) {
            BlockEntity CauldronBe = pLevel.getBlockEntity(pPos);
            if (CauldronBe instanceof PotionCauldronBE cauldron) {
                //Este bloque es temporal para debugging
                if (item.isEmpty()) {
                    if (cauldron.getRecipeId() != null &&
                            cauldron.getRecipeId().getNamespace().compareTo("shulespotions") == 0) {
                        if(cauldron.getCurrentRecipeAction() < cauldron.getRecipe(pLevel).getActions().size()) {
                            pPlayer.sendSystemMessage(
                                    Component.literal("Paso" + cauldron.getCurrentRecipeAction()
                                            + " Item Esperado: " +
                                            cauldron.getRecipe(pLevel)
                                                    .getActions()
                                                    .get(cauldron.getCurrentRecipeAction())
                                                    .getExpectedItem()
                                                    .getItem()
                                                    .getDefaultInstance()
                                                    .getHoverName()
                                                    .getString()));
                        } else pPlayer.sendSystemMessage(Component.literal("Ya esta gordo"));
                    } else pPlayer.sendSystemMessage(Component.literal("Sin Receta"));
                    return InteractionResult.SUCCESS;
                }

                if (item.getItem() instanceof PotionRecipeItem recipe) {
                        cauldron.setRecipeId(recipe.getRecipeId());
                        cauldron.setCurrentRecipeAction(0);
                } else {
                    if (cauldron.getRecipeId() != null &&
                            cauldron.getRecipeId().getNamespace().compareTo("shulespotions") == 0) {
                        cauldron.HandleActions(pPlayer);
                        pLevel.sendBlockUpdated(pPos, pState, pState, 3);
                    }
                }
            }


        }
        return InteractionResult.SUCCESS;
    }


}





