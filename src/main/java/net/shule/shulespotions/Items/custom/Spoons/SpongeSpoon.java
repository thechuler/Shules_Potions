package net.shule.shulespotions.Items.custom.Spoons;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.shule.shulespotions.Potions.IngredientStat;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.util.CauldronActions.CauldronContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpongeSpoon extends SpoonItem {
    public SpongeSpoon(Properties properties, String tooltipId) {
        super(properties, tooltipId);
    }


    @Override
    protected void performSpoonAction(CauldronContext cauldron) {
        PotionLiquid liquid = cauldron.getCauldron().getPotionLiquid();
        IngredientStat stats = liquid.getStats();

        int purity = stats.getPurity();

        Map<ResourceLocation, Integer> effects = stats.getEffectWeights();
        List<ResourceLocation> keys = new ArrayList<>(effects.keySet());
        ResourceLocation selected = keys.get(RandomSource.create().nextInt(keys.size()));
        int weight = stats.getEffectWeight(selected);
        stats.addEffectWeight(selected, -weight);


        stats.setPurity(purity + 2);


        cauldron.getCauldron().setPotionLiquid(liquid);

        super.performSpoonAction(cauldron);
    }
}
