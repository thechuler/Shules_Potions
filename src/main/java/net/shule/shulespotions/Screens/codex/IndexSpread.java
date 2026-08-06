package net.shule.shulespotions.Screens.codex;

import net.minecraft.resources.ResourceLocation;
import net.shule.shulespotions.Screens.codex.base.MenuSpread;

public class IndexSpread extends MenuSpread {

    public IndexSpread() {
        super("shulespotions.codex.index", 72);
    }

    @Override
    public ResourceLocation getBackground() {
        return ResourceLocation.fromNamespaceAndPath("shulespotions","textures/gui/effect_codex_screen_index.png");
    }

    @Override
    protected void registerOptions() {
        addMenuOption("shulespotions.codex.guide", () -> parent.pushSpread(new GuideindexSpread()));
        addMenuOption("shulespotions.codex.effects", () -> parent.pushSpread(new EffectGridSpread()));
        addMenuOption("shulespotions.codex.stats", () -> parent.pushSpread(new StatsIndexSpread()));
        addMenuOption("shulespotions.codex.ingredients", () -> parent.pushSpread(new IngredientGridSpread()));
        addMenuOption("shulespotions.codex.blocks", () -> parent.pushSpread(new AtlasSpread("Blocks")));
    }
}
