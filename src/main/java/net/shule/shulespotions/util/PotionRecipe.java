package net.shule.shulespotions.util;

import net.minecraft.world.effect.MobEffect;

import java.util.List;

public class PotionRecipe {
    private final List<CauldronAction> actions;
    private final MobEffect result;


    public PotionRecipe(List<CauldronAction> actions, MobEffect result) {
        this.actions = actions;
        this.result = result;
    }


    public List<CauldronAction> getActions() {
        return actions;
    }

    public MobEffect getResult() {
        return result;
    }



}
