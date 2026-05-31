package net.shule.shulespotions.Items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;
import net.shule.shulespotions.Screens.RecipeScrollScreen;
import net.shule.shulespotions.util.CauldronActions.CauldronAction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecipeScroll extends Item {

    public static final String ACTIONS_TAG = "SPActions";
    public static final String POTION_FLUID_TAG = "SPPotionFluid";

    public RecipeScroll(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {

        if (pContext.getLevel().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity be = pContext.getLevel().getBlockEntity(pContext.getClickedPos());

        if (!(be instanceof PotionCauldronBE cauldron)) {
            return super.useOn(pContext);
        }

        List<CauldronAction> actions = cauldron.getActions();
        FluidStack fluid = cauldron.getTank().getFluid();

        if (actions.isEmpty() || fluid.isEmpty()) {
            return InteractionResult.FAIL;
        }

        ItemStack heldStack = pContext.getItemInHand();
        Player player = pContext.getPlayer();

        if (player == null) {
            return InteractionResult.FAIL;
        }


        ItemStack recipeScroll = new ItemStack(this);

        setActions(recipeScroll, actions);
        setPotionFluid(recipeScroll, fluid);

        heldStack.shrink(1);

        if (!player.getInventory().add(recipeScroll)) {
            player.drop(recipeScroll, false);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel,
                                                  Player pPlayer,
                                                  InteractionHand pUsedHand) {

        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (hasRecipeData(stack)) {

            if (pLevel.isClientSide) {
                Minecraft.getInstance().setScreen(
                        new RecipeScrollScreen(stack)
                );
            }

            pLevel.playSound(
                    pPlayer,
                    pPlayer.getOnPos(),
                    SoundEvents.BOOK_PAGE_TURN,
                    SoundSource.PLAYERS
            );

            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.fail(stack);
    }

    /*
     * =========================
     * ACTIONS
     * =========================
     */

    public static void setActions(ItemStack stack,
                                  List<CauldronAction> actions) {

        CompoundTag tag = stack.getOrCreateTag();
        ListTag actionList = new ListTag();

        for (CauldronAction action : actions) {

            CompoundTag actionTag = new CompoundTag();

            actionTag.putString("Type", action.getType());
            actionTag.put("Data", action.save());

            actionList.add(actionTag);
        }

        tag.put(ACTIONS_TAG, actionList);
    }

    public static List<CompoundTag> getActions(ItemStack stack) {

        List<CompoundTag> actions = new ArrayList<>();

        CompoundTag tag = stack.getTag();

        if (tag == null || !tag.contains(ACTIONS_TAG, Tag.TAG_LIST)) {
            return actions;
        }

        ListTag list = tag.getList(ACTIONS_TAG, Tag.TAG_COMPOUND);

        for (int i = 0; i < list.size(); i++) {
            actions.add(list.getCompound(i));
        }

        return actions;
    }

    /*
     * =========================
     * POTION FLUID
     * =========================
     */

    public static void setPotionFluid(ItemStack stack,
                                      FluidStack fluidStack) {

        CompoundTag tag = stack.getOrCreateTag();

        CompoundTag fluidTag = new CompoundTag();
        fluidStack.writeToNBT(fluidTag);

        tag.put(POTION_FLUID_TAG, fluidTag);
    }

    public static FluidStack getPotionFluid(ItemStack stack) {

        CompoundTag tag = stack.getTag();

        if (tag == null ||
                !tag.contains(POTION_FLUID_TAG, Tag.TAG_COMPOUND)) {

            return FluidStack.EMPTY;
        }

        CompoundTag fluidTag = tag.getCompound(POTION_FLUID_TAG);

        return FluidStack.loadFluidStackFromNBT(fluidTag);
    }

    /*
     * =========================
     * HELPERS
     * =========================
     */

    public static boolean hasRecipeData(ItemStack stack) {

        CompoundTag tag = stack.getTag();

        return tag != null
                && tag.contains(ACTIONS_TAG)
                && tag.contains(POTION_FLUID_TAG);
    }

    @Override
    public void appendHoverText(ItemStack pStack,
                                @Nullable Level pLevel,
                                List<Component> pTooltipComponents,
                                TooltipFlag pIsAdvanced) {

        pTooltipComponents.add(
                Component.translatable("tooltip.shulespotions.recipe_scroll")
                        .withStyle(ChatFormatting.DARK_GRAY)
        );

        super.appendHoverText(
                pStack,
                pLevel,
                pTooltipComponents,
                pIsAdvanced
        );
    }
}