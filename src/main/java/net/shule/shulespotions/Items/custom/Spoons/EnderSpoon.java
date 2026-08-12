package net.shule.shulespotions.Items.custom.Spoons;

import net.minecraft.resources.ResourceLocation;
import net.shule.shulespotions.Potions.IngredientStat;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.util.CauldronActions.CauldronContext;

import java.util.Map;

public class EnderSpoon extends SpoonItem{
    public EnderSpoon(Properties properties, String tooltipId) {
        super(properties, tooltipId);
    }


    @Override
    protected void performSpoonAction(CauldronContext cauldron) {
        PotionLiquid liquid = cauldron.getCauldron().getPotionLiquid();
        IngredientStat stats = liquid.getStats();

        Map.Entry<ResourceLocation, Integer> strongest =
                stats.getEffectWeights()
                        .entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .orElse(null);



        if (strongest != null ) {

            ResourceLocation strongestEffect = strongest.getKey();

            int strongestWeight = strongest.getValue();

            stats.addEffectWeight(strongestEffect, -strongestWeight);
        }

        stats.setStability(100);

        cauldron.getCauldron().setPotionLiquid(liquid);

        super.performSpoonAction(cauldron);
    }
}
