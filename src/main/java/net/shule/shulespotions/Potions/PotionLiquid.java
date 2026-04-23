package net.shule.shulespotions.Potions;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public  class PotionLiquid {
    private int duration;
    private  int complexity;
    private float power;
    private  float purity;
    private  int color;
    private List<MobEffect> effect;


    public PotionLiquid(int pduration, int pcomplexity, float ppower, float ppurity, List<MobEffect> peffect, int pcolor){
        duration = pduration;
        complexity = pcomplexity;
        power = ppower;
        purity = ppurity;
        effect = peffect;
        color = pcolor;
    }

    public  PotionLiquid(){
        duration = 0;
        complexity = 0;
        power = 0;
        purity = 0;
        effect = null;
        color = 0;
    }




    public CompoundTag save(){
        CompoundTag tag = new CompoundTag();
        tag.putInt("spduration",this.getDuration());
        tag.putFloat("sppower",this.getPower());
        tag.putInt("spcomplexity",this.getComplexity());
        tag.putFloat("sppurity",this.getPurity());
        tag.putInt("spcolor",this.getColor());

        ListTag effectList = new ListTag();
        if(this.getEffect() != null) {
            for (MobEffect effect : this.getEffect()) {
                CompoundTag eTag = new CompoundTag();

                ResourceLocation id = BuiltInRegistries.MOB_EFFECT.getKey(effect);
                if (id != null) {
                    eTag.putString("Id", id.toString());
                }

                effectList.add(eTag);
            }

        }
        tag.put("speffects", effectList);

        return tag;
    }



    public static PotionLiquid load(CompoundTag tag) {
        int duration = tag.getInt("spduration");
        int complexity = tag.getInt("spcomplexity");
        float power = tag.getFloat("sppower");
        float purity = tag.getFloat("sppurity");
        int color = tag.getInt("spcolor");

        List<MobEffect> effects = new ArrayList<>();

        ListTag list = tag.getList("speffects", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag effectTag = list.getCompound(i);

            ResourceLocation id = ResourceLocation.parse(effectTag.getString("Id"));
            MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(id);

            if (effect != null) {
                effects.add(effect);
            }
        }

        return new PotionLiquid(duration, complexity, power, purity, effects, color);
    }




    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setEffect(List<MobEffect> effect) {
        this.effect = effect;
    }

    public List<MobEffect> getEffect() {
        return effect;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public float getPower() {
        return power;
    }


    public void setPurity(float purity) {
        this.purity = purity;
    }

    public float getPurity() {
        return purity;
    }


    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    public int getComplexity() {
        return complexity;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PotionLiquid liquid = (PotionLiquid) o;
        return duration == liquid.duration && complexity == liquid.complexity && Float.compare(power, liquid.power) == 0 && Float.compare(purity, liquid.purity) == 0 && color == liquid.color && Objects.deepEquals(effect, liquid.effect);
    }




}
