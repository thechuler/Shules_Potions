package net.shule.shulespotions.Player;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public interface IPlayerRecipes {
    Set<ResourceLocation> getRecipes();
    boolean hasRecipe(ResourceLocation id);
    void addRecipe(ResourceLocation id);
}
