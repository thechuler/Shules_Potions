package net.shule.shulespotions.Items.custom.Spoons;

import net.minecraft.resources.ResourceLocation;
import net.shule.shulespotions.Potions.IngredientStat;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.util.CauldronActions.CauldronContext;

import java.util.Map;

public class BastionSpoon extends SpoonItem{
    public BastionSpoon(Properties properties, String tooltipId) {
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

        Map.Entry<ResourceLocation, Integer> weakest =
                stats.getEffectWeights()
                        .entrySet()
                        .stream()
                        .min(Map.Entry.comparingByValue())
                        .orElse(null);

        if (strongest != null && weakest != null
                && !strongest.getKey().equals(weakest.getKey())) {

            ResourceLocation strongestEffect = strongest.getKey();
            ResourceLocation weakestEffect = weakest.getKey();

            int weakestWeight = weakest.getValue();

            stats.addEffectWeight(weakestEffect, -weakestWeight);

            stats.addEffectWeight(strongestEffect, 100);
        }


        super.performSpoonAction(cauldron);
    }
}
