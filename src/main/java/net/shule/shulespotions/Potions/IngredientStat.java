package net.shule.shulespotions.Potions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;

public class IngredientStat {

    public static final Codec<IngredientStat> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.INT.optionalFieldOf("purity", 0)
                                    .forGetter(IngredientStat::getPurity),

                            Codec.INT.optionalFieldOf("vitality", 0)
                                    .forGetter(IngredientStat::getVitality),

                            Codec.INT.optionalFieldOf("flavor", 0)
                                    .forGetter(IngredientStat::getFlavor),

                            Codec.INT.optionalFieldOf("stability", 0)
                                    .forGetter(IngredientStat::getStability)

                    ).apply(instance, IngredientStat::new)
            );

    private int purity;
    private int vitality;
    private int flavor;
    private int stability;

    public IngredientStat() {}

    public IngredientStat(int purity, int vitality, int flavor, int stability) {
        this.purity = purity;
        this.vitality = vitality;
        this.flavor = flavor;
        this.stability = stability;
    }

    public void add(IngredientStat other) {
        this.purity += other.purity;
        this.vitality += other.vitality;
        this.flavor += other.flavor;
        this.stability += other.stability;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("purity", purity);
        tag.putInt("vitality", vitality);
        tag.putInt("flavor", flavor);
        tag.putInt("stability", stability);
        return tag;
    }

    public static IngredientStat load(CompoundTag tag) {
        return new IngredientStat(
                tag.getInt("purity"),
                tag.getInt("vitality"),
                tag.getInt("flavor"),
                tag.getInt("stability")
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
}