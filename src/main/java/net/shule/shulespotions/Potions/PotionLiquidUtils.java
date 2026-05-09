package net.shule.shulespotions.Potions;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.ArrayList;
import java.util.List;

public class PotionLiquidUtils {


    public static List<MobEffect> resolve (PotionLiquid pl){
        List<MobEffect> effects = new ArrayList<>();

        if (pl.getStats().getPurity()> 70) {
            effects.add(MobEffects.REGENERATION);
        }

        if (pl.getStats().getVitality() < -10) {
            effects.add(MobEffects.POISON);
        }

        if (pl.getStats().getStability() > 55 && pl.getStats().getPurity() > 30) {
            effects.add(MobEffects.NIGHT_VISION);
        }

        return effects;

    }
}
