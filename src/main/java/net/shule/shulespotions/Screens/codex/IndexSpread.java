package net.shule.shulespotions.Screens.codex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.shule.shulespotions.Screens.ImageTextButton;

public class IndexSpread extends CodexSpread {

    @Override
    public void init(int x, int y) {
        int leftPageCenter = x + 96;
        int rightPageCenter = x + 288;
        int yoffset = 72; // Centered vertically ((240 - 95) / 2)
        int buttonWidth = 100;
        int buttonHeight = 20;

        ResourceLocation bgTex = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/button_background.png");

        parent.addSpreadWidget(new ImageTextButton(
                leftPageCenter - buttonWidth / 2, y + yoffset, buttonWidth, buttonHeight,
                Component.translatable("shulespotions.codex.effects"), bgTex, buttonWidth, buttonHeight * 2,
                b -> parent.pushSpread(new EffectGridSpread())
        ));
        
        yoffset += 25;

        parent.addSpreadWidget(new ImageTextButton(
                leftPageCenter - buttonWidth / 2, y + yoffset, buttonWidth, buttonHeight,
                Component.translatable("shulespotions.codex.stats"), bgTex, buttonWidth, buttonHeight * 2,
                b -> parent.pushSpread(new StatsIndexSpread())
        ));

        yoffset += 25;

        parent.addSpreadWidget(new ImageTextButton(
                leftPageCenter - buttonWidth / 2, y + yoffset, buttonWidth, buttonHeight,
                Component.translatable("shulespotions.codex.ingredients"), bgTex, buttonWidth, buttonHeight * 2,
                b -> parent.pushSpread(new IngredientGridSpread())
        ));
/*
        yoffset += 25;

        parent.addSpreadWidget(new ImageTextButton(
                leftPageCenter - buttonWidth / 2, y + yoffset, buttonWidth, buttonHeight,
                Component.translatable("shulespotions.codex.blocks"), bgTex, buttonWidth, buttonHeight * 2,
                b -> parent.pushSpread(new AtlasSpread("Blocks"))
        ));*/
    }


    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y) {
        int leftPageCenter = x + 96;
        int rightPageCenter = x + 288;

        graphics.pose().pushPose();
        graphics.pose().scale(1.5f, 1.5f, 1.0f);
        
        Component title = Component.translatable("shulespotions.codex.index");
        int titleX = leftPageCenter - Math.round(Minecraft.getInstance().font.width(title) * 1.5f / 2.0f);
        
        graphics.drawString(Minecraft.getInstance().font, title, 
                Math.round(titleX / 1.5f), 
                Math.round((y + 22) / 1.5f), 
                0x5E4A32, false);
                
        graphics.pose().popPose();
    }
}
