package net.shule.shulespotions.Potions;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.joml.Random;

import java.util.ArrayList;
import java.util.List;

public class PotionLiquidUtils {


    public static List<MobEffect> resolve(PotionLiquid pl) {

        List<MobEffect> effects = new ArrayList<>();

        int purity = pl.getStats().getPurity();
        int vitality = pl.getStats().getVitality();
        int flavor = pl.getStats().getFlavor();
        int stability = pl.getStats().getStability();


        // =====================================================
        // POSITIVOS
        // =====================================================

        // Curación y soporte

        if (purity >= 70) {
            effects.add(MobEffects.REGENERATION);
        }

        if (vitality >= 40) {
            effects.add(MobEffects.HEALTH_BOOST);
        }

        if (purity >= 40 && vitality >= 20) {
            effects.add(MobEffects.ABSORPTION);
        }

        if (flavor >= 35 && vitality >= 15) {
            effects.add(MobEffects.SATURATION);
        }

        if (purity >= 55 && vitality >= 35) {
            effects.add(MobEffects.HEAL);
        }


        // Visión y percepción

        if (stability >= 55 && purity >= 30) {
            effects.add(MobEffects.NIGHT_VISION);
        }

        if (purity >= 65 && stability >= 40) {
            effects.add(MobEffects.WATER_BREATHING);
        }

        if (purity >= 45 && stability >= 15) {
            effects.add(MobEffects.GLOWING);
        }


        // Defensa

        if (stability >= 40) {
            effects.add(MobEffects.FIRE_RESISTANCE);
        }

        if (purity >= 20 && stability >= 20 && vitality >= 20) {
            effects.add(MobEffects.DAMAGE_RESISTANCE);
        }

        if (stability >= 65 && purity >= 20) {
            effects.add(MobEffects.CONDUIT_POWER);
        }


        // Movimiento

        if (flavor >= 40 && vitality >= 10) {
            effects.add(MobEffects.MOVEMENT_SPEED);
        }

        if (vitality >= 45 && flavor >= 20) {
            effects.add(MobEffects.JUMP);
        }

        if (purity >= 35 && stability >= 35 && flavor >= 10) {
            effects.add(MobEffects.SLOW_FALLING);
        }

        if (purity >= 50 && stability < 0) {
            effects.add(MobEffects.INVISIBILITY);
        }

        if (flavor >= 65 && stability >= 25) {
            effects.add(MobEffects.DIG_SPEED);
        }

        if (flavor >= 25 && purity >= 25 && vitality >= 25) {
            effects.add(MobEffects.DOLPHINS_GRACE);
        }


        // Combate

        if (vitality >= 25 && flavor < 0) {
            effects.add(MobEffects.DAMAGE_BOOST);
        }

        if (vitality >= 70 && purity < 10) {
            effects.add(MobEffects.DAMAGE_RESISTANCE);
            effects.add(MobEffects.DAMAGE_BOOST);
        }

        if (purity >= 50 && vitality >= 50 && stability >= 50) {
            effects.add(MobEffects.HERO_OF_THE_VILLAGE);
        }

        if (purity >= 80 && stability >= 80 && vitality >= 40) {
            effects.add(MobEffects.LUCK);
        }


        // =====================================================
        // NEGATIVOS
        // =====================================================

        if (vitality <= -10) {
            effects.add(MobEffects.POISON);
        }

        if (stability <= -35) {
            effects.add(MobEffects.WEAKNESS);
        }

        if (purity <= -20) {
            effects.add(MobEffects.BLINDNESS);
        }

        if (flavor <= -15) {
            effects.add(MobEffects.HUNGER);
        }

        if (stability <= -50 && purity < 0) {
            effects.add(MobEffects.WITHER);
        }
        if (vitality <= -30 && flavor < 0) {
            effects.add(MobEffects.CONFUSION);
        }

        if (purity <= -10 && vitality <= -10) {
            effects.add(MobEffects.MOVEMENT_SLOWDOWN);
        }

        if (stability <= -20 && flavor > 20) {
            effects.add(MobEffects.LEVITATION);
        }

        if (purity <= -45 && stability <= -10) {
            effects.add(MobEffects.DARKNESS);
        }

        if (vitality <= -40 && purity <= -20) {
            effects.add(MobEffects.HARM);
        }

        if (stability <= -65) {
            effects.add(MobEffects.UNLUCK);
        }

        if (flavor <= -35 && vitality <= -20) {
            effects.add(MobEffects.DIG_SLOWDOWN);
        }

        if (purity <= -30 && flavor >= 30) {
            effects.add(MobEffects.BAD_OMEN);
        }

        if (stability <= -45 && vitality >= 20) {
            effects.add(MobEffects.WEAKNESS);
            effects.add(MobEffects.MOVEMENT_SLOWDOWN);
        }

        return effects;
    }


    public static int generatePotionColor(
            int purity,
            int vitality,
            int flavor,
            int stability
    ) {

        // -----------------------------------
        // HASH DETERMINISTA
        // -----------------------------------

        long seed = 1L;

        seed = seed * 31L + purity;
        seed = seed * 31L + vitality;
        seed = seed * 31L + flavor;
        seed = seed * 31L + stability;

        Random random = new Random(seed);

        // -----------------------------------
        // HUE
        // -----------------------------------

        // Full random procedural
        float hue = random.nextFloat();

        // -----------------------------------
        // SATURATION
        // -----------------------------------

        // Siempre colores vivos
        float saturation = 0.65f + random.nextFloat() * 0.35f;

        // -----------------------------------
        // BRIGHTNESS
        // -----------------------------------

        float brightness = 0.7f + random.nextFloat() * 0.3f;

        // -----------------------------------
        // INFLUENCIA DE STATS
        // -----------------------------------

        // Purity positiva ilumina
        brightness += Math.max(0, purity) / 300f;

        // Purity negativa ensucia
        brightness -= Math.abs(Math.min(0, purity)) / 400f;

        // Stability negativa hace colores más violentos
        saturation += Math.abs(Math.min(0, stability)) / 300f;

        // Stability positiva suaviza un poco
        saturation -= Math.max(0, stability) / 500f;

        // Flavor mueve levemente el hue
        hue += flavor / 500f;

        // Vitality aumenta energía visual
        brightness += Math.abs(vitality) / 500f;

        // -----------------------------------

        hue = (hue % 1f + 1f) % 1f;

        saturation = clamp(saturation, 0.45f, 1f);
        brightness = clamp(brightness, 0.35f, 1f);

        return java.awt.Color.HSBtoRGB(
                hue,
                saturation,
                brightness
        );
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }}
