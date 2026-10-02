package net.shule.shulespotions.Potions;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class PotionLiquid {
    private IngredientStat iStats;
    private boolean finished;
    private List<MobEffect> resolvedEffects;

    public PotionLiquid(IngredientStat pistats) {
        this.iStats = pistats != null ? pistats : new IngredientStat();
        this.finished = false;
        this.resolvedEffects = new ArrayList<>();
    }

    public PotionLiquid() {
        this(new IngredientStat());
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.put("spistats", this.iStats.save());
        tag.putBoolean("finished", this.finished);
        
        if (!this.resolvedEffects.isEmpty()) {
            ListTag effectsTag = new ListTag();
            for (MobEffect effect : this.resolvedEffects) {
                ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);
                if (id != null) {
                    effectsTag.add(StringTag.valueOf(id.toString()));
                }
            }
            tag.put("resolvedEffects", effectsTag);
        }

        return tag;
    }

    public static PotionLiquid load(CompoundTag tag) {
        IngredientStat stats = new IngredientStat();

        if (tag.contains("spistats")) {
            stats = IngredientStat.load(tag.getCompound("spistats"));
        }

        PotionLiquid pl = new PotionLiquid(stats);
        pl.setFinished(tag.getBoolean("finished"));
        
        if (tag.contains("resolvedEffects", Tag.TAG_LIST)) {
            ListTag effectsTag = tag.getList("resolvedEffects", Tag.TAG_STRING);
            for (int i = 0; i < effectsTag.size(); i++) {
                ResourceLocation id = ResourceLocation.parse(effectsTag.getString(i));
                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(id);
                if (effect != null) {
                    pl.getResolvedEffects().add(effect);
                }
            }
        }

        return pl;
    }

    public IngredientStat getStats() {
        return iStats;
    }

    public void addStats(IngredientStat other) {
        this.iStats.add(other);
    }
    
    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public List<MobEffect> getResolvedEffects() {
        return resolvedEffects;
    }

    public void setResolvedEffects(List<MobEffect> resolvedEffects) {
        this.resolvedEffects = resolvedEffects;
    }
}
