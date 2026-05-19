package net.shule.shulespotions.Items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.registries.ForgeRegistries;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;
import net.shule.shulespotions.Screens.RecipeScrollScreen;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecipeScroll extends Item {

    public static final String INGREDIENTS_TAG = "SPIngredients";
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

        List<Item> ingredients = cauldron.getIngredients();
        FluidStack fluid = cauldron.getTank().getFluid();

        if (ingredients.isEmpty() || fluid.isEmpty()) {
            return InteractionResult.FAIL;
        }

        ItemStack scroll = pContext.getItemInHand();

        setIngredients(scroll, ingredients);
        setPotionFluid(scroll, fluid);

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

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
     * INGREDIENTS
     * =========================
     */

    public static void setIngredients(ItemStack stack, List<Item> ingredients) {

        CompoundTag tag = stack.getOrCreateTag();
        ListTag ingredientList = new ListTag();

        for (Item item : ingredients) {

            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);

            if (id != null) {
                ingredientList.add(StringTag.valueOf(id.toString()));
            }
        }

        tag.put(INGREDIENTS_TAG, ingredientList);
    }

    public static List<Item> getIngredients(ItemStack stack) {

        List<Item> ingredients = new ArrayList<>();

        CompoundTag tag = stack.getTag();

        if (tag == null || !tag.contains(INGREDIENTS_TAG, Tag.TAG_LIST)) {
            return ingredients;
        }

        ListTag ingredientList = tag.getList(INGREDIENTS_TAG, Tag.TAG_STRING);

        for (Tag element : ingredientList) {

            String itemId = element.getAsString();

            Item item = ForgeRegistries.ITEMS.getValue(
                    ResourceLocation.parse(itemId)
            );

            if (item != null) {
                ingredients.add(item);
            }
        }

        return ingredients;
    }

    /*
     * =========================
     * POTION FLUID
     * =========================
     */

    public static void setPotionFluid(ItemStack stack, FluidStack fluidStack) {

        CompoundTag tag = stack.getOrCreateTag();

        CompoundTag fluidTag = new CompoundTag();
        fluidStack.writeToNBT(fluidTag);

        tag.put(POTION_FLUID_TAG, fluidTag);
    }

    public static FluidStack getPotionFluid(ItemStack stack) {

        CompoundTag tag = stack.getTag();

        if (tag == null || !tag.contains(POTION_FLUID_TAG, Tag.TAG_COMPOUND)) {
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
                && tag.contains(INGREDIENTS_TAG)
                && tag.contains(POTION_FLUID_TAG);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(
                Component.translatable("tooltip.shulespotions.recipe_scroll")
                        .withStyle(ChatFormatting.DARK_GRAY)
        );
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}