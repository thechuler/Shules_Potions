package net.shule.shulespotions.Screens.codex;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.shule.shulespotions.Screens.codex.base.DoubleTextSpread;
import net.shule.shulespotions.Screens.codex.base.MenuSpread;
import net.shule.shulespotions.Screens.codex.base.TextImageSpread;

public class GuideindexSpread extends MenuSpread {

    public GuideindexSpread() {
        super("shulespotions.codex.guide", 72);
    }

    @Override
    public void init(int x, int y) {
        super.init(x, y);
        addBackButton(x, y);
    }

    @Override
    protected void registerOptions() {

        Component potion_cauldronlink = Component.translatable("block.shulespotions.potion_cauldron")
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.DARK_PURPLE)
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("shulespotions.codex.see_more")))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "inspection:potion_cauldron")));

       Component spoon_link = Component.translatable("shulespotions.codex.spoon")
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.DARK_PURPLE)
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Component.translatable("shulespotions.codex.see_more")))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "inspection:wooden_spoon")));




        addMenuOption("shulespotions.codex.epilogue", () -> parent.pushSpread(
                new DoubleTextSpread(
                      Component.translatable("shulespotions.codex.epilogue.title"),
                       Component.translatable("shulespotions.codex.epilogue.text1"),
                        null,
                       Component.translatable("shulespotions.codex.epilogue.text2")
                )));


        Component scroll_link = Component.translatable("item.shulespotions.recipe_scroll")
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.DARK_PURPLE)
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("shulespotions.codex.see_more")))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "inspection:recipe_scroll")));

        addMenuOption("shulespotions.codex.chapter1", () -> parent.pushSpread(
                new DoubleTextSpread(
                        Component.translatable("shulespotions.codex.chapter1.title"),
                        Component.translatable("shulespotions.codex.chapter1.text1", potion_cauldronlink, potion_cauldronlink),
                        null,
                        Component.translatable("shulespotions.codex.chapter1.text2")
                )));

        addMenuOption("shulespotions.codex.chapter2", () -> parent.pushSpread(
                new DoubleTextSpread(
                        Component.translatable("shulespotions.codex.chapter2.title"),
                        Component.translatable("shulespotions.codex.chapter2.text1", spoon_link),
                        null,
                        Component.translatable("shulespotions.codex.chapter2.text2")
                )));

        addMenuOption("shulespotions.codex.chapter3", () -> parent.pushSpread(
                new DoubleTextSpread(
                        Component.translatable("shulespotions.codex.chapter3.title"),
                       Component.translatable("shulespotions.codex.chapter3.text1"),
                        null,
                       Component.translatable("shulespotions.codex.chapter3.text2", scroll_link)
                )));
    }
}
