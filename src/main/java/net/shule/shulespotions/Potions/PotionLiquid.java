package net.shule.shulespotions.Potions;


import net.minecraft.nbt.CompoundTag;


public class PotionLiquid {
    private int duration;
    private int power;
    private IngredientStat iStats;
    private int color;


    public PotionLiquid(int pduration,int ppower,int pcolor,IngredientStat pistats) {
        duration = pduration;
        power = ppower;
        color = pcolor;
        this.iStats = pistats != null ? pistats : new IngredientStat();
    }

    public PotionLiquid() {
        this(0, 0, 0, new IngredientStat());
    }


    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("spduration", this.getDuration());
        tag.putInt("sppower", this.getPower());
        tag.putInt("spcolor", this.getColor());
        tag.put("spistats", this.iStats.save());

        return tag;
    }


    public static PotionLiquid load(CompoundTag tag) {
        int duration = tag.getInt("spduration");
        int power = tag.getInt("sppower");
        int color = tag.getInt("spcolor");
        IngredientStat stats = new IngredientStat();

        if (tag.contains("spistats")) {
            stats = IngredientStat.load(tag.getCompound("spistats"));
        }

        return new PotionLiquid(duration, power, color, stats);
    }



    public IngredientStat getStats() {
        return iStats;
    }

    public void addStats(IngredientStat other) {
        this.iStats.add(other);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }


    public void setPower(int power) {
        this.power = power;
    }

    public int getPower() {
        return power;
    }


    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }




}
