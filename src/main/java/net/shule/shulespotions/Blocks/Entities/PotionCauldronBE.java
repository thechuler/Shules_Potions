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

import static net.shule.shulespotions.util.ColorUtils.lerpColor;
import static net.shule.shulespotions.util.ColorUtils.randomColor;


import java.util.List;


public class PotionCauldronBE extends BlockEntity {
    private float liquidLevel;
    private ResourceLocation recipeId;
    private int currentRecipeAction;
    private PotionLiquid liquid;
    private boolean isRecipeFinished;
    private int temperature;
    private int color;
    private int startColor;
    private int targetColor;
    private float lerpProgress = 1.0f;


    public PotionCauldronBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POTION_CAULDRON_BE.get(), pos, state);
        currentRecipeAction = 0;
        liquidLevel = 0f;
        liquid = new PotionLiquid();
        isRecipeFinished = false;
        color = randomColor();
        temperature = 0;
    }


    //Metodo que comprueba si una accion puede ser activada y en base a ello avanza la receta
    public void handleTrigger(CauldronActionTrigger trigger, CauldronActionContext ctx) {
        assert ctx.cauldron.level != null;
        if (!ctx.cauldron.HasRecipe()) return;
        PotionRecipe recipe = PotionRecipeItem.getRecipe(ctx.cauldron.level, this.recipeId).orElseThrow();
        if (currentRecipeAction >= recipe.getActions().size()) return;


        CauldronAction action = recipe.getActions().get(currentRecipeAction);

        if (action.getTrigger() != trigger) return;

        if (action.canTrigger(ctx)) {
            action.execute(ctx);
            currentRecipeAction++;
            //Aca pickea un color random por paso, pero se podria hacer que tenga algun patron.
            if (currentRecipeAction > 0) {
                startColorTransition(randomColor());
            }
            if (currentRecipeAction == recipe.getActions().size()) {
                MobEffect resultEffect = resolveMobEffect(ctx.cauldron.level, recipe);
                //Aca se genera el potion liquid.
                this.setPotionLiquid(new PotionLiquid(400, recipe.getActions().size(), 10.5f, 100.0f, List.of(resultEffect), resultEffect.getColor()));
                startColorTransition(resultEffect.getColor());
                isRecipeFinished = true;
                //Cleanea la receta mantiene el liquido
                removeRecipe();
                currentRecipeAction = 0;
            }
            setChanged();
            sync();
        }
    }


    private MobEffect resolveMobEffect(Level level, PotionRecipe recipe) {
        //si mas adelante se agregan mobeffect custom que esten en otro registry, habra que filtrar
        //aca segun el prefijo del effectId para ver en que registro buscar
        return level.registryAccess().registryOrThrow(Registries.MOB_EFFECT).get(ResourceLocation.parse(recipe.getEffectId()));
    }


    //Esto despues se llama en el bloque como tal xq el be no tiene metodo tick
    //de momento solo se usa para los CauldronAction de tipo TICK, y para el lerp de colores
    public static void tick(Level level, BlockPos pos, BlockState state, PotionCauldronBE be) {
        if (level.isClientSide) return;

        if (be.lerpProgress < 1.0f) {
            be.lerpProgress += 0.05f;

            if (be.lerpProgress > 1.0f) be.lerpProgress = 1.0f;

            be.color = lerpColor(be.startColor, be.targetColor, be.lerpProgress);

            be.setChanged();
            be.sync();
        }

        be.handleTrigger(CauldronActionTrigger.TICK, new CauldronActionContext(be, null, null));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putFloat("SPLiquidLevel", liquidLevel);
        pTag.putInt("SPCurrentRecipeAction", currentRecipeAction);
        pTag.putBoolean("SPrecipeFinished", isRecipeFinished);
        pTag.putInt("SPcolor", color);
        pTag.putInt("SPTemperature", temperature);

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
        currentRecipeAction = pTag.getInt("SPCurrentRecipeAction");
        liquidLevel = pTag.getFloat("SPLiquidLevel");
        isRecipeFinished = pTag.getBoolean("SPrecipeFinished");
        color = pTag.getInt("SPcolor");
        temperature = pTag.getInt("SPTemperature");
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


    public void setTemperature(int temperature) {
        this.temperature = temperature;
        setChanged();
        sync();
    }

    public int getTemperature() {
        return temperature;
    }

    public void setPotionLiquid(PotionLiquid liquid) {
        this.liquid = liquid;
        setChanged();
        sync();
    }


    public PotionLiquid getPotionLiquid() {
        return liquid;
    }

    public boolean hasPotionLiquid() {
        return liquid != null;
    }

    public void sync() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }



    public void setLiquidLevel(float liquidLevel) {
        this.liquidLevel = liquidLevel;
        setChanged();
        sync();
    }

    public float getLiquidLevel() {
        return liquidLevel;
    }

    public PotionRecipe getRecipe(Level level) {
        if (recipeId != null) return PotionRecipeItem.getRecipe(level, this.recipeId).orElseThrow();
        else return null;
    }

    public void setRecipeFinished(boolean recipeFinished) {
        isRecipeFinished = recipeFinished;
    }

    public void setRecipeId(ResourceLocation recipeId) {
        this.recipeId = recipeId;
        setChanged();
        sync();
    }

    public void removeRecipe() {
        if (!this.HasRecipe()) return;
        this.recipeId = null;
        this.setChanged();
    }

    public void setCurrentRecipeAction(int value) {
        this.currentRecipeAction = value;
        setChanged();
        sync();
    }

    public int getCurrentRecipeAction() {
        return currentRecipeAction;
    }

    public boolean HasRecipe() {
        return recipeId != null && recipeId.getNamespace().compareTo("shulespotions") == 0;
    }

    public boolean IsRecipeFinished() {
        return isRecipeFinished;
    }

    public void setColor(int color) {
        this.color = color;
        setChanged();
        sync();
    }

    public int getColor() {
        return color;
    }

    private void startColorTransition(int newColor) {
        this.startColor = this.color;
        this.targetColor = newColor;
        this.lerpProgress = 0.0f;
    }

}

