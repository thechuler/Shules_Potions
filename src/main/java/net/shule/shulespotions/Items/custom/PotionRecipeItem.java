package net.shule.shulespotions.Items.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.shule.shulespotions.Recipes.ModRecipes;
import net.shule.shulespotions.util.CauldronActions.CauldronAction;
import net.shule.shulespotions.Recipes.Potion.PotionRecipe;

import java.util.List;
import java.util.Optional;


public class PotionRecipeItem extends Item {
    private final ResourceLocation recipeId;

    public PotionRecipeItem(Properties pProperties, ResourceLocation recipeId) {
        super(pProperties);
        this.recipeId = recipeId;
    }

    public ResourceLocation getRecipeId(){
        return recipeId;
    }

    public Optional<PotionRecipe> getRecipe(Level level) {
        return  getRecipe(level, this.recipeId);
    }

    public static Optional<PotionRecipe> getRecipe(Level level, ResourceLocation recipeId) {
        return level.getRecipeManager()
                .byKey(recipeId)
                .filter(r -> r instanceof PotionRecipe)
                .map(r -> (PotionRecipe) r);

    }
}
