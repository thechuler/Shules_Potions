package net.shule.shulespotions.util;

import net.minecraft.util.StringRepresentable;

public enum CauldronState implements StringRepresentable {
    BASE("base"),
    HOT("hot"),
    COLD("cold");

    private final String name;

    CauldronState(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}