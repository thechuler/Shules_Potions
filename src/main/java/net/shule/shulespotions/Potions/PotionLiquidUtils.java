package net.shule.shulespotions.Potions;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.ArrayList;
import java.util.List;

public class PotionLiquidUtils {


    public static List<MobEffect> resolve(PotionLiquid pl) {

        List<MobEffect> effects = new ArrayList<>();

        int purity = pl.getStats().getPurity();
        int vitality = pl.getStats().getVitality();
        int flavor = pl.getStats().getFlavor();
        int stability = pl.getStats().getStability();

        // POSITIVOS

        if (purity > 70) {
            effects.add(MobEffects.REGENERATION);
        }

        if (stability > 55 && purity > 30) {
            effects.add(MobEffects.NIGHT_VISION);
        }

        if (vitality > 40) {
            effects.add(MobEffects.HEALTH_BOOST);
        }

        if (flavor > 50) {
            effects.add(MobEffects.LUCK);
        }

        if (purity > 40 && vitality > 20) {
            effects.add(MobEffects.ABSORPTION);
        }

        if (stability > 40) {
            effects.add(MobEffects.FIRE_RESISTANCE);
        }

        if (purity > 20 && stability > 20 && vitality > 20) {
            effects.add(MobEffects.DAMAGE_RESISTANCE);
        }

        if (flavor > 35 && vitality > 15) {
            effects.add(MobEffects.SATURATION);
        }

        if (purity > 50 && stability < 0) {
            effects.add(MobEffects.INVISIBILITY);
        }

        if (vitality > 25 && flavor < 0) {
            effects.add(MobEffects.DAMAGE_BOOST);
        }


        // NEGATIVOS

        if (vitality < -10) {
            effects.add(MobEffects.POISON);
        }

        if (stability < -35) {
            effects.add(MobEffects.WEAKNESS);
        }

        if (purity < -20) {
            effects.add(MobEffects.BLINDNESS);
        }

        if (flavor < -15) {
            effects.add(MobEffects.HUNGER);
        }

        if (stability < -50 && purity < 0) {
            effects.add(MobEffects.WITHER);
        }

        if (vitality < -30 && flavor < 0) {
            effects.add(MobEffects.CONFUSION);
        }

        if (purity < -10 && vitality < -10) {
            effects.add(MobEffects.MOVEMENT_SLOWDOWN);
        }

        if (stability < -20 && flavor > 20) {
            effects.add(MobEffects.LEVITATION);
        }

        return effects;
    }
}
