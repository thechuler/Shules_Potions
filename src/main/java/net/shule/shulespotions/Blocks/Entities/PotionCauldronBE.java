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
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.Recipes.Potion.PotionRecipe;
import net.shule.shulespotions.util.CauldronActions.CauldronAction;
import net.shule.shulespotions.util.CauldronActions.CauldronActionContext;
import net.shule.shulespotions.util.CauldronActions.CauldronActionTrigger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class PotionCauldronBE extends BlockEntity {
    private float liquidLevel;

    private ResourceLocation recipeId;
    private int currentRecipeAction;

    private PotionLiquid liquid;

    public PotionCauldronBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POTION_CAULDRON_BE.get(), pos, state);
        currentRecipeAction = 0;
        liquidLevel = 0f;
        liquid = new PotionLiquid();
    }


    public void handleTrigger(CauldronActionTrigger trigger, CauldronActionContext ctx) {
        assert ctx.cauldron.level != null;
        PotionRecipe recipe = PotionRecipeItem.getRecipe(ctx.cauldron.level, this.recipeId).orElseThrow();
        if (recipe == null) return;

        if (currentRecipeAction >= recipe.getActions().size()) return;


        CauldronAction action = recipe.getActions().get(currentRecipeAction);

        if (action.getTrigger() != trigger) return;

        if (action.canTrigger(ctx)) {
            action.execute(ctx);
            currentRecipeAction++;
            if(currentRecipeAction > 0)  this.liquid.setColor(0x8A2BE2);
            if (currentRecipeAction == recipe.getActions().size()) {

                MobEffect resultEffect = resolveMobEffect(ctx.cauldron.level, recipe);
                this.setPotionLiquid(new PotionLiquid(400, recipe.getActions().size(), 10.5f, 100.0f, List.of(resultEffect), resultEffect.getColor()));
            }
            setChanged();
        }
    }


    private MobEffect resolveMobEffect(Level level, PotionRecipe recipe) {
        //si mas adelante se agregan mobeffect custom que esten en otro registry, habra que filtrar
        //aca segun el prefijo del effectId para ver en que registro buscar
        return level.registryAccess().registryOrThrow(Registries.MOB_EFFECT).get(ResourceLocation.parse(recipe.getEffectId()));
    }


    public void setLiquidLevel(float liquidLevel) {
        this.liquidLevel = liquidLevel;
    }

    public float getLiquidLevel() {
        return liquidLevel;
    }

    public PotionRecipe getRecipe(Level level) {
        if (recipeId != null) return PotionRecipeItem.getRecipe(level, this.recipeId).orElseThrow();
        else return null;
    }

    public ResourceLocation getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(ResourceLocation recipeId) {
        this.recipeId = recipeId;
        setChanged();
    }

    public void setCurrentRecipeAction(int value) {
        this.currentRecipeAction = value;
        setChanged();
    }

    public int getCurrentRecipeAction() {
        return currentRecipeAction;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putFloat("SPLiquidLevel", liquidLevel);
        pTag.putInt("SPCurrentState", currentRecipeAction);
        if (recipeId != null && recipeId.getNamespace().compareTo("shulespotions") == 0) {
            pTag.putString("SPRecipeId", recipeId.toString());
        }

        if (liquid != null) {
            pTag.put("SPPotionLiquid", liquid.save());
        }

    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        currentRecipeAction = pTag.getInt("SPCurrentState");
        liquidLevel = pTag.getFloat("SPLiquidLevel");
        recipeId = ResourceLocation.tryParse(pTag.getString("SPRecipeId"));
        if (pTag.contains("SPPotionLiquid")) {
            liquid = PotionLiquid.load(pTag.getCompound("SPPotionLiquid"));
        }
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


    public void setPotionLiquid(PotionLiquid liquid) {
        this.liquid = liquid;
        setChanged();
        sync();
    }

    public PotionLiquid getPotionLiquid() {
        return liquid;
    }

    private void sync() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public boolean hasPotionLiquid() {
        return liquid != null;
    }


}

