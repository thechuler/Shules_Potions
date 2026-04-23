package net.shule.shulespotions.Items.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.shule.shulespotions.Recipes.Potion.PotionRecipe;

import java.util.Optional;


public class RecipeSheetItem extends Item {
    private final ResourceLocation recipeId;
    private final int color;
    public RecipeSheetItem(Properties pProperties, ResourceLocation recipeId, int color) {
        super(pProperties);
        this.recipeId = recipeId;
        this.color = color;
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

    public int getColor() {
        return color;
    }
}
