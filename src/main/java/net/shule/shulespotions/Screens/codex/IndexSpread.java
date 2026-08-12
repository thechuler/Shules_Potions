package net.shule.shulespotions.Screens.codex;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.Screens.codex.base.MenuSpread;

import java.util.List;

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
        addMenuOption("shulespotions.codex.items", () -> {
            List<Item> misItems = List.of(
                    ModItems.WOODEN_SPOON.get(),
                    ModItems.SPONGE_SPOON.get(),
                    ModItems.ENDER_SPOON.get(),
                    ModItems.STONE_SPOON.get(),
                    ModItems.DIAMOND_SPOON.get(),
                    ModItems.BASTION_SPOON.get(),
                    ModItems.GOLDEN_SPOON.get(),
                    ModItems.IRON_SPOON.get(),
                    ModItems.THROWABLE_POTION_BOTTLE.get(),
                    ModItems.SMALL_POTION_BOTTLE.get(),
                    ModItems.LARGE_POTION_BOTTLE.get(),
                    ModItems.BIG_POTION_BOTTLE.get(),
                    ModItems.RECIPE_SCROLL.get(),
                    ModItems.PESTLE.get(),
                    ModBlocks.COPPER_CAULDRON.get().asItem(),
                    ModBlocks.POTION_CAULDRON.get().asItem(),
                    ModBlocks.SPOON_RACK.get().asItem(),
                    ModBlocks.MORTAR.get().asItem()





                    );

            parent.pushSpread(new ItemAtlasSpread(misItems));
        });
    }
}
