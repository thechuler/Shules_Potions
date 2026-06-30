package net.shule.shulespotions.Blocks.Entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.shule.shulespotions.Blocks.ModBlockEntities;

import net.shule.shulespotions.Recipes.ModRecipeTypes;
import net.shule.shulespotions.Recipes.MortarContainer;
import net.shule.shulespotions.Recipes.MortarRecipe;
import net.shule.shulespotions.util.ColorUtils;

public class MortarBE extends BlockEntity {

    private final NonNullList<ItemStack> ingredients = NonNullList.withSize(3, ItemStack.EMPTY);

    private int grindProgress;
    private int bowls;
    private int requiredGrinds;
    private ItemStack result = ItemStack.EMPTY;
    private ResourceLocation recipeId;
    private MortarRecipe currentRecipe;
    private int powderColor;

    public MortarBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MORTAR_BE.get(), pos, state);
    }

    // ----------------------------
    // INGREDIENTS
    // ----------------------------

    public NonNullList<ItemStack> getIngredients() {
        return ingredients;
    }

    public boolean isEmpty() {
        for (ItemStack s : ingredients)
            if (!s.isEmpty()) return false;
        return true;
    }

    public boolean isFull() {
        for (ItemStack s : ingredients)
            if (s.isEmpty()) return false;
        return true;
    }

    public void clearIngredients() {
        ingredients.replaceAll(i -> ItemStack.EMPTY);
    }

    public boolean addIngredient(ItemStack stack) {
        if (level == null || level.isClientSide) return false;

        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).isEmpty()) {
                ingredients.set(i, stack.copyWithCount(1));

                // reset parcial de progresión si cambias receta
                grindProgress = 0;
                bowls = 0;
                requiredGrinds = 0;
                recipeId = null;
                currentRecipe = null;
                result = ItemStack.EMPTY;

                sync();
                return true;
            }
        }
        return false;
    }

    public boolean removeIngredient(Player player) {
        if (level == null || level.isClientSide) return false;

        for (int i = ingredients.size() - 1; i >= 0; i--) {
            ItemStack stack = ingredients.get(i);

            if (!stack.isEmpty()) {
                ingredients.set(i, ItemStack.EMPTY);
                player.addItem(stack);

                // importante: invalidar receta al cambiar inputs
                grindProgress = 0;
                bowls = 0;
                requiredGrinds = 0;

                recipeId = null;
                currentRecipe = null;

                result = ItemStack.EMPTY;

                sync();
                return true;
            }
        }
        return false;
    }

    // ----------------------------
    // RECIPE
    // ----------------------------

    private void resolveRecipe() {
        if (level == null || recipeId == null) return;

        if (currentRecipe != null) return;

        var opt = level.getRecipeManager().byKey(recipeId);

        if (opt.isPresent() && opt.get() instanceof MortarRecipe mr) {
            currentRecipe = mr;
        }
    }

    // ----------------------------
    // GRINDING
    // ----------------------------

    public void grind() {
        if (level == null || level.isClientSide) return;

        resolveRecipe();

        if (currentRecipe == null) {
            var opt = level.getRecipeManager().getRecipeFor(
                    ModRecipeTypes.MORTAR,
                    new MortarContainer(ingredients),
                    level
            );

            if (opt.isEmpty()) return;

            currentRecipe = opt.get();
            recipeId = currentRecipe.getId();

            result = currentRecipe.getResult().copy();

            requiredGrinds = currentRecipe.getGrinds();
            bowls = currentRecipe.getBowls();
            powderColor = ColorUtils.fromHex(currentRecipe.getColor());
            grindProgress = 0;
        }

        grindProgress++;

        if (grindProgress >= requiredGrinds) {
            ingredients.replaceAll(i -> ItemStack.EMPTY);
        }

        sync();
    }

    public boolean isFinished() {
        return requiredGrinds > 0
                && grindProgress >= requiredGrinds;
    }

    public ItemStack takeBowl() {

        if (!isFinished())
            return ItemStack.EMPTY;

        ItemStack stack = result.copy();

        bowls--;

        if (bowls <= 0) {
            clear();
        } else {
            sync();
        }

        return stack;
    }

    public void clear() {

        ingredients.replaceAll(i -> ItemStack.EMPTY);

        currentRecipe = null;
        recipeId = null;

        result = ItemStack.EMPTY;

        grindProgress = 0;
        bowls = 0;
        requiredGrinds = 0;

        sync();
    }

    // ----------------------------
    // NBT
    // ----------------------------

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        ContainerHelper.saveAllItems(tag, ingredients);

        tag.putInt("GrindProgress", grindProgress);
        tag.putInt("Bowls", bowls);
        tag.putInt("RequiredGrinds", requiredGrinds);
        tag.putInt("PowderColor", powderColor);
        if (recipeId != null) {
            tag.putString("RecipeId", recipeId.toString());
        }

        if (!result.isEmpty()) {
            tag.put("Result", result.save(new CompoundTag()));
        }
    }


    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        ingredients.clear();
        ContainerHelper.loadAllItems(tag, ingredients);

        grindProgress = tag.getInt("GrindProgress");
        bowls = tag.getInt("Bowls");
        requiredGrinds = tag.getInt("RequiredGrinds");

        recipeId = null;
        currentRecipe = null;
        powderColor = tag.getInt("PowderColor");
        if (tag.contains("RecipeId")) {
            recipeId = ResourceLocation.parse(tag.getString("RecipeId"));
        }

        result = ItemStack.EMPTY;

        if (tag.contains("Result")) {
            result = ItemStack.of(tag.getCompound("Result"));
        }
    }

    // ----------------------------
    // SYNC
    // ----------------------------

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void sync() {
        setChanged();

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public ResourceLocation getRecipeId() {
        return recipeId;
    }
    public ItemStack getResult() {
        return result;
    }

    public int getPowderColor() {
        return powderColor;
    }
}