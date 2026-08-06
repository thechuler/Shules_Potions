package net.shule.shulespotions.Screens.codex;

import net.minecraft.ChatFormatting;
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

        net.minecraft.network.chat.Component potion_cauldronlink = net.minecraft.network.chat.Component.translatable("block.shulespotions.potion_cauldron")
                .withStyle(net.minecraft.network.chat.Style.EMPTY
                        .withColor(ChatFormatting.DARK_PURPLE)
                        .withHoverEvent(new net.minecraft.network.chat.HoverEvent(net.minecraft.network.chat.HoverEvent.Action.SHOW_TEXT, net.minecraft.network.chat.Component.translatable("shulespotions.codex.see_more")))
                        .withClickEvent(new net.minecraft.network.chat.ClickEvent(net.minecraft.network.chat.ClickEvent.Action.CHANGE_PAGE, "inspection:potion_cauldron")));

        net.minecraft.network.chat.Component spoon_link = net.minecraft.network.chat.Component.translatable("shulespotions.codex.spoon")
                .withStyle(net.minecraft.network.chat.Style.EMPTY
                        .withColor(ChatFormatting.DARK_PURPLE)
                        .withHoverEvent(new net.minecraft.network.chat.HoverEvent(net.minecraft.network.chat.HoverEvent.Action.SHOW_TEXT, net.minecraft.network.chat.Component.translatable("shulespotions.codex.see_more")))
                        .withClickEvent(new net.minecraft.network.chat.ClickEvent(net.minecraft.network.chat.ClickEvent.Action.CHANGE_PAGE, "inspection:wooden_spoon")));




        addMenuOption("shulespotions.codex.epilogue", () -> parent.pushSpread(
                new DoubleTextSpread(
                        net.minecraft.network.chat.Component.translatable("shulespotions.codex.epilogue.title"),
                        net.minecraft.network.chat.Component.translatable("shulespotions.codex.epilogue.text1"),
                        null,
                        net.minecraft.network.chat.Component.translatable("shulespotions.codex.epilogue.text2")
                )));


        addMenuOption("shulespotions.codex.chapter1", () -> parent.pushSpread(
                new DoubleTextSpread(
                        net.minecraft.network.chat.Component.translatable("shulespotions.codex.chapter1.title"),
                        net.minecraft.network.chat.Component.translatable("shulespotions.codex.chapter1.text1", potion_cauldronlink, potion_cauldronlink),
                        null,
                        net.minecraft.network.chat.Component.translatable("shulespotions.codex.chapter1.text2", spoon_link)
                )));

        addMenuOption("shulespotions.codex.chapter2", () -> parent.pushSpread(
                new DoubleTextSpread(
                        net.minecraft.network.chat.Component.translatable("shulespotions.codex.chapter2.title"),
                        net.minecraft.network.chat.Component.translatable("shulespotions.codex.chapter1.text1", potion_cauldronlink, potion_cauldronlink),
                        null,
                        net.minecraft.network.chat.Component.translatable("shulespotions.codex.chapter1.text2", spoon_link)
                )));
    }
}
