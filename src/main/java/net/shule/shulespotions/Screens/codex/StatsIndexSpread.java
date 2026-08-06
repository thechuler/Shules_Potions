package net.shule.shulespotions.Screens.codex;

import net.shule.shulespotions.Screens.codex.base.MenuSpread;

public class StatsIndexSpread extends MenuSpread {

    public StatsIndexSpread() {
        super("shulespotions.codex.stats", 60);
    }

    @Override
    public void init(int x, int y) {
        super.init(x, y);
        addBackButton(x, y);
    }

    @Override
    protected void registerOptions() {
        addMenuOption("shulespotions.purity.name", () -> parent.pushSpread(new StatInfoSpread("purity")));
        addMenuOption("shulespotions.vitality.name", () -> parent.pushSpread(new StatInfoSpread("vitality")));
        addMenuOption("shulespotions.duration.name", () -> parent.pushSpread(new StatInfoSpread("duration")));
        addMenuOption("shulespotions.stability.name", () -> parent.pushSpread(new StatInfoSpread("stability")));
        addMenuOption("shulespotions.flavor.name", () -> parent.pushSpread(new StatInfoSpread("flavor")));
    }
}
