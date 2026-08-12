package net.shule.shulespotions.Potions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.shule.shulespotions.util.ColorUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static net.shule.shulespotions.util.ColorUtils.mixColors;

public class IngredientStat {

    public static final Codec<IngredientStat> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(

                            Codec.INT.optionalFieldOf("duration", 0)
                                    .forGetter(IngredientStat::getDurationSeconds),

                            Codec.STRING.optionalFieldOf("color", "#3F76E4")
                                    .xmap(
                                            ColorUtils::fromHex,
                                            ColorUtils::toHex
                                    )
                                    .forGetter(IngredientStat::getColor),

                            Codec.INT.optionalFieldOf("purity", 0)
                                    .forGetter(IngredientStat::getPurity),

                            Codec.INT.optionalFieldOf("vitality", 0)
                                    .forGetter(IngredientStat::getVitality),

                            Codec.INT.optionalFieldOf("flavor", 0)
                                    .forGetter(IngredientStat::getFlavor),

                            Codec.INT.optionalFieldOf("stability", 0)
                                    .forGetter(IngredientStat::getStability),

                            Codec.unboundedMap(
                                            ResourceLocation.CODEC,
                                            Codec.INT
                                    ).optionalFieldOf("effects", Map.of())
                                    .forGetter(IngredientStat::getEffectWeights)

                    ).apply(instance, IngredientStat::new)
            );

    private int purity;
    private int vitality;
    private int flavor;
    private int stability;
    private int color;
    private int duration;
    private Map<ResourceLocation, Integer> effectWeights = new HashMap<>();

    public IngredientStat() {
        this.effectWeights = new HashMap<>();
        this.color = -1;
    }

    public IngredientStat(IngredientStat other) {
        this.purity = other.purity;
        this.vitality = other.vitality;
        this.flavor = other.flavor;
        this.stability = other.stability;
        this.color = other.color;
        this.duration = other.duration;
        this.effectWeights = new HashMap<>(other.effectWeights);
    }


    IngredientStat(int duration, int color, int purity, int vitality, int flavor, int stability,
            Map<ResourceLocation, Integer> effectWeights) {
        this.duration =duration;
        this.color = color;
        this.purity = purity;
        this.vitality = vitality;
        this.flavor = flavor;
        this.stability = stability;
        this.effectWeights = new HashMap<>(effectWeights);
    }

    public void add(IngredientStat other) {

        this.purity = Math.max(0, Math.min(100, this.purity + other.purity));

        this.vitality = Math.max(-100, Math.min(100, this.vitality + other.vitality));

        this.flavor = Math.max(-100, Math.min(100, this.flavor + other.flavor));

        this.stability = Math.max(0, Math.min(100, this.stability + other.stability));

        this.duration += other.duration;
        if (this.color == -1) {
            this.color = other.color;
        } else {
            this.color = mixColors(this.color, other.color);
        }
        other.effectWeights.forEach((effect, value) -> {
            int newValue = this.effectWeights.getOrDefault(effect, 0) + value;
            if (newValue == 0) {
                this.effectWeights.remove(effect);
            } else {
                this.effectWeights.put(
                        effect,
                        Math.max(-100, Math.min(100, newValue))
                );
            }
        });
    }

    public CompoundTag save() {

        CompoundTag tag = new CompoundTag();

        tag.putInt("purity", purity);
        tag.putInt("vitality", vitality);
        tag.putInt("flavor", flavor);
        tag.putInt("stability", stability);
        tag.putInt("color", color);
        tag.putInt("duration", duration);
        CompoundTag effectsTag = new CompoundTag();

        effectWeights.forEach((effectId, weight) -> {
            effectsTag.putInt(effectId.toString(), weight);
        });

        tag.put("effects", effectsTag);

        return tag;
    }

    public static IngredientStat load(CompoundTag tag) {

        Map<ResourceLocation, Integer> effects = new HashMap<>();

        CompoundTag effectsTag = tag.getCompound("effects");

        for (String key : effectsTag.getAllKeys()) {

            ResourceLocation effectId = ResourceLocation.tryParse(key);

            if (effectId != null) {
                effects.put(effectId, effectsTag.getInt(key));
            }
        }

        return new IngredientStat(
                tag.getInt("duration"),
                tag.getInt("color"),
                tag.getInt("purity"),
                tag.getInt("vitality"),
                tag.getInt("flavor"),
                tag.getInt("stability"),
                effects
        );
    }

    public void setPurity(int purity) {
        this.purity = purity;
    }

    public int getPurity() {
        return purity;
    }

    public void setStability(int stability) {
        this.stability = stability;
    }

    public int getStability() {
        return stability;
    }

    public void setFlavor(int flavor) {
        this.flavor = flavor;
    }

    public int getFlavor() {
        return flavor;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
    }

    public int getVitality() {
        return vitality;
    }

    public void setColor(int Color) {
        this.color = Color;
    }

    public int getColor() {
        return color;
    }

    public void setDurationSeconds(int Duration) {
        this.duration = Duration;
    }
    public void setDurationTicks(int Duration) {
        this.duration = Duration / 20;
    }

    public int getDurationTicks() {
        return this.duration * 20;

    }

    public int getDurationSeconds() {
        return this.duration;
    }

    public String getDurationFormatted() {
        int hours = duration / 3600;
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;

        return hours > 0
                ? String.format("%02d:%02d:%02d", hours, minutes, seconds)
                : String.format("%02d:%02d", minutes, seconds);
    }


    public Map<ResourceLocation, Integer> getEffectWeights() {
        return Collections.unmodifiableMap(effectWeights);
    }

    public void addEffectWeight(ResourceLocation effect, int amount) {
        int newValue = effectWeights.getOrDefault(effect, 0) + amount;

        if (newValue == 0) {
            effectWeights.remove(effect);
        } else {
            effectWeights.put(effect, Math.max(-100, Math.min(100, newValue)));
        }
    }

    public int getEffectWeight(ResourceLocation effect) {
        return effectWeights.getOrDefault(effect, 0);
    }

    public void setEffectWeight(ResourceLocation effect, int weight) {
        if (weight == 0) {
            effectWeights.remove(effect);
        } else {
            effectWeights.put(effect, Math.max(-100, Math.min(weight, 100)));
        }
    }
}