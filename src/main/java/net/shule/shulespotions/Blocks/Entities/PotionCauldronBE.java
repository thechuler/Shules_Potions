package net.shule.shulespotions.Blocks.Entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.shule.shulespotions.Blocks.ModBlockEntities;
import net.shule.shulespotions.Items.custom.PotionRecipeItem;
import net.shule.shulespotions.Recipes.Potion.PotionRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Random;


public class PotionCauldronBE extends BlockEntity {
    private float liquidLevel;
    private PotionRecipe recipe;
    private int currentRecipeAction;
    private int liquidColor;

    public PotionCauldronBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POTION_CAULDRON_BE.get(), pos, state);
        currentRecipeAction = 0;
        liquidColor =0x3F76E4;
        liquidLevel = 0f;
    }


    public void HandleActions(Player player) {
        liquidLevel = currentRecipeAction > 0 ? 1: 0;
        if (recipe.getActions().size() <= currentRecipeAction) return;

        boolean flag = recipe.getActions().get(currentRecipeAction).PerformAction(this, player);
        if (flag) {
            currentRecipeAction++;
            if(currentRecipeAction == recipe.getActions().size()) {
                MobEffect resultEffect = resolveMobEffect(player.level());
                //aca se asignaria el efecto a la instancia de potion liquid

                setLiquidColor(resultEffect.getColor());
                player.addEffect(new MobEffectInstance(resultEffect));
            }else
                    setLiquidColor(0xFF000000 | new Random().nextInt(0xFFFFFF));
        } else {
            currentRecipeAction = 0;
            setLiquidColor(0xFFFF0000);
        }


    }

    private MobEffect resolveMobEffect(Level level){
        return level.registryAccess()
                .registryOrThrow(Registries.MOB_EFFECT)
                .get(ResourceLocation.parse(recipe.getEffectId()));
    }

    public int getLiquidColor() {
        return liquidColor;
    }

    public void setLiquidColor(int liquidColor) {
        this.liquidColor = liquidColor;
    }

    public void setLiquidLevel(float liquidLevel) {
        this.liquidLevel = liquidLevel;
    }

    public float getLiquidLevel() {
        return liquidLevel;
    }

    public PotionRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(PotionRecipe recipe) {
        this.recipe = recipe;
    }

    public int getCurrentRecipeAction() {
        return currentRecipeAction;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putFloat("SPLiquidLevel", liquidLevel);
        pTag.putInt("SPCurrentState", currentRecipeAction);
        pTag.putInt("SPLiquidColor",liquidColor);
        if(recipe != null){
            pTag.putString("SPRecipeId", recipe.getId().toString());
        }
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        currentRecipeAction = pTag.getInt("SPCurrentState");
        liquidLevel = pTag.getFloat("SPLiquidLevel");
        liquidColor = pTag.getInt("SPLiquidColor");
        //recipe = PotionRecipeItem.getRecipe(tryParse(pTag.getString("SPRecipeId")), this.level);
    }


    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        assert pkt.getTag() != null;
        this.load(pkt.getTag());
    }


}

