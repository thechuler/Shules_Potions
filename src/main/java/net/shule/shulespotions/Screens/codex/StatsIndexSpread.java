package net.shule.shulespotions.Screens.codex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class StatsIndexSpread extends CodexSpread {



    @Override
    public void init(int x, int y) {
        int leftPageCenter = x + 96;
        int rightPageCenter = x + 288;
        int yoffset = 60;
        int buttonWidth = 100;
        int buttonHeight = 20;

        ResourceLocation backTex = net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/button_back.png");
        ResourceLocation bgTex = net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/button_background.png");

        parent.addSpreadWidget(
                new net.minecraft.client.gui.components.ImageButton(
                        x + 105, y + BaseCodexScreen.GUI_HEIGHT - 15, 20, 20, 0, 0, 20, backTex, 20, 40,
                        b -> parent.popSpread()
                )
        );

        parent.addSpreadWidget(new net.shule.shulespotions.Screens.ImageTextButton(
                leftPageCenter - buttonWidth / 2, y + yoffset, buttonWidth, buttonHeight,
                Component.translatable("shulespotions.purity.name"), bgTex, buttonWidth, buttonHeight * 2,
                b -> parent.pushSpread(new StatInfoSpread("purity"))
        ));
        
        yoffset += 25;

        parent.addSpreadWidget(new net.shule.shulespotions.Screens.ImageTextButton(
                leftPageCenter - buttonWidth / 2, y + yoffset, buttonWidth, buttonHeight,
                Component.translatable("shulespotions.vitality.name"), bgTex, buttonWidth, buttonHeight * 2,
                b -> parent.pushSpread(new StatInfoSpread("vitality"))
        ));

        yoffset += 25;

        parent.addSpreadWidget(new net.shule.shulespotions.Screens.ImageTextButton(
                leftPageCenter - buttonWidth / 2, y + yoffset, buttonWidth, buttonHeight,
                Component.translatable("shulespotions.duration.name"), bgTex, buttonWidth, buttonHeight * 2,
                b -> parent.pushSpread(new StatInfoSpread("duration"))
        ));

        yoffset += 25;

        parent.addSpreadWidget(new net.shule.shulespotions.Screens.ImageTextButton(
                leftPageCenter - buttonWidth / 2, y + yoffset, buttonWidth, buttonHeight,
                Component.translatable("shulespotions.stability.name"), bgTex, buttonWidth, buttonHeight * 2,
                b -> parent.pushSpread(new StatInfoSpread("stability"))
        ));

        yoffset += 25;

        parent.addSpreadWidget(new net.shule.shulespotions.Screens.ImageTextButton(
                leftPageCenter - buttonWidth / 2, y + yoffset, buttonWidth, buttonHeight,
                Component.translatable("shulespotions.flavor.name"), bgTex, buttonWidth, buttonHeight * 2,
                b -> parent.pushSpread(new StatInfoSpread("flavor"))
        ));

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y) {

        int leftPageCenter = x + 96;
        graphics.pose().pushPose();
        graphics.pose().scale(1.5f, 1.5f, 1.0f);

        Component title = Component.translatable("shulespotions.codex.stats");
        int titleX = leftPageCenter - Math.round(Minecraft.getInstance().font.width(title) * 1.5f / 2.0f);

        graphics.drawString(Minecraft.getInstance().font, title,
                Math.round(titleX / 1.5f),
                Math.round((y + 22) / 1.5f),
                0x5E4A32, false);

        graphics.pose().popPose();
    }
}
