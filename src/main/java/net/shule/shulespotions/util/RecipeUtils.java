package net.shule.shulespotions.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.shule.shulespotions.Recipes.Potion.PotionRecipe;

public class RecipeUtils {

    public static MobEffect resolveMobEffect(Level level, String recipe) {
        //si mas adelante se agregan mobeffect custom que esten en otro registry, habra que filtrar
        //aca segun el prefijo del effectId para ver en que registro buscar
        return level.registryAccess().registryOrThrow(Registries.MOB_EFFECT).get(ResourceLocation.parse(recipe));
    }

}
