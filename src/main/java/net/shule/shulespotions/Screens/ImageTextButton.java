package net.shule.shulespotions.Screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ImageTextButton extends Button {
    private final ResourceLocation texture;
    private final int textureWidth;
    private final int textureHeight;

    public ImageTextButton(int x, int y, int width, int height, Component message, ResourceLocation texture, int textureWidth, int textureHeight, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.texture = texture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        boolean hovered = isHovered();
        
        // Draw the background texture. 
        // If hovered, we shift the texture Y coordinate by 'height' to render the hovered state of the button
        int yOffset = hovered ? height : 0;
        
        graphics.blit(texture, getX(), getY(), 0, yOffset, width, height, textureWidth, textureHeight);
        
        // Draw the centered text on top
        int textColor = hovered ? 0xFFFFFF : 0xDDDDDD; 
        graphics.drawCenteredString(Minecraft.getInstance().font, getMessage(), getX() + width / 2, getY() + (height - 8) / 2, textColor);
    }
}
