package net.shule.shulespotions.Items.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.shule.shulespotions.util.CauldronActions.CauldronAction;
import net.shule.shulespotions.util.PotionRecipe;

import java.util.List;

public class PotionRecipeItem extends Item {
    private PotionRecipe recipe;

    public PotionRecipeItem(Properties pProperties, List<CauldronAction> pActions, MobEffect pMobEffect) {
        super(pProperties);

        recipe = new PotionRecipe(pActions,pMobEffect);
    }

    public PotionRecipe getRecipe() {
        return recipe;
    }
}
