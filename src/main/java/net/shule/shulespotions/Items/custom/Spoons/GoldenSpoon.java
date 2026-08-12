package net.shule.shulespotions.Items.custom.Spoons;

import net.shule.shulespotions.Potions.IngredientStat;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.util.CauldronActions.CauldronContext;

public class GoldenSpoon extends SpoonItem {
    public GoldenSpoon(Properties properties, String tooltipId) {
        super( properties, tooltipId);
    }

    @Override
    protected void performSpoonAction(CauldronContext cauldron) {
       PotionLiquid liquid = cauldron.getCauldron().getPotionLiquid();
       IngredientStat stats = liquid.getStats();

        int sec = stats.getDurationSeconds();
        int purity = stats.getPurity();


        stats.setDurationSeconds(sec * 2);
        stats.setPurity(Math.min(100, purity * 2));


        cauldron.getCauldron().setPotionLiquid(liquid);

        super.performSpoonAction(cauldron);
    }
}
