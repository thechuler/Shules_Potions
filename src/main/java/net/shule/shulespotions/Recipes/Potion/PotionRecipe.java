package net.shule.shulespotions.Recipes.Potion;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.shule.shulespotions.Recipes.ModRecipes;
import net.shule.shulespotions.util.CauldronActions.CauldronAction;

import java.util.List;

public class PotionRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final List<CauldronAction> actions;
    private final String effectId;



    public PotionRecipe(ResourceLocation id, List<CauldronAction> actions, String effectId) {
        this.id = id;
        this.actions = actions;
        this.effectId = effectId;
    }


    public List<CauldronAction> getActions() {
        return actions;
    }

    public String getEffectId() {
        return effectId;
    }


    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.POTION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.POTION_TYPE.get();
    }
}
