package net.shule.shulespotions.Items.custom.Spoons;

import net.shule.shulespotions.Potions.IngredientStat;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.util.CauldronActions.CauldronContext;

public class StoneSpoon extends SpoonItem {
    public StoneSpoon(Properties properties, String tooltipId) {
        super(properties, tooltipId);
    }


    @Override
    protected void performSpoonAction(CauldronContext cauldron) {
        PotionLiquid liquid = cauldron.getCauldron().getPotionLiquid();
        IngredientStat stats = liquid.getStats();

        int sec = stats.getDurationSeconds();



        stats.setDurationSeconds(sec + 20);



        cauldron.getCauldron().setPotionLiquid(liquid);

        super.performSpoonAction(cauldron);
    }
}
