package net.shule.shulespotions.util.CauldronActions;

import net.minecraft.nbt.CompoundTag;
import net.shule.shulespotions.Potions.PotionLiquid;

public abstract class CauldronAction {
    public abstract void apply(CauldronContext ctx);
    public abstract CompoundTag save();
    public abstract String getType();
}
