package net.shule.shulespotions.Potions;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.shule.shulespotions.Fluids.PotionFluidHelper;


public class PotionLiquid {
    private IngredientStat iStats;


    public PotionLiquid(IngredientStat pistats) {

        this.iStats = pistats != null ? pistats : new IngredientStat();
    }

    public PotionLiquid() {
        this(new IngredientStat());
    }


    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.put("spistats", this.iStats.save());

        return tag;
    }


    public static PotionLiquid load(CompoundTag tag) {
        IngredientStat stats = new IngredientStat();

        if (tag.contains("spistats")) {
            stats = IngredientStat.load(tag.getCompound("spistats"));
        }

        return new PotionLiquid(stats);
    }



    public IngredientStat getStats() {
        return iStats;
    }

    public void addStats(IngredientStat other) {
        this.iStats.add(other);
    }






}
