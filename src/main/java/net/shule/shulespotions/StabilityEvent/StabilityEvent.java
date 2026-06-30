package net.shule.shulespotions.StabilityEvent;

import net.shule.shulespotions.util.CauldronActions.CauldronContext;

public interface StabilityEvent {
    boolean canTrigger(int stability);
    void trigger(CauldronContext ctx);
    float getChance(CauldronContext ctx);
}
