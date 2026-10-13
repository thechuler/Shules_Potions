package net.shule.shulespotions.Potions;

public record EffectConfig(int level, boolean hidden) {
    public static final EffectConfig DEFAULT = new EffectConfig(1, false);

}
