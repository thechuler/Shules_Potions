package net.shule.shulespotions.Screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class IngredientButton extends Button {

    private final ItemStack stack;
    private long hoverStartTime = 0L;
    private boolean lastHovered = false;
    private static final long POP_DURATION = 180; // ms

    public IngredientButton(int x, int y, int size, ItemStack stack, OnPress onPress) {
        super(x, y, size, size, Component.empty(), onPress, DEFAULT_NARRATION);
        this.stack = stack;
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        boolean hovered = isHovered();

        if (hovered && !lastHovered) {
            hoverStartTime = System.currentTimeMillis();
        }

        lastHovered = hovered;
        float iconScale = width / 18F;

        if (hovered) {
            long elapsed = System.currentTimeMillis() - hoverStartTime;
            float progress = Math.min(1F, elapsed / (float) POP_DURATION);
            float extraScale;

            if (progress < 0.7F) {
                extraScale = (progress / 0.7F) * 0.20F;
            } else {
                float t = (progress - 0.7F) / 0.3F;
                extraScale = 0.20F - (0.10F * t);
            }
            iconScale *= (1F + extraScale);
        }

        graphics.pose().pushPose();
        graphics.pose().translate(getX() + width / 2F, getY() + height / 2F, hovered ? 200 : 0);
        graphics.pose().scale(iconScale, iconScale, 1F);
        

        graphics.pose().translate(-8, -8, 0); 
        
        graphics.renderItem(stack, 0, 0);
        graphics.pose().popPose();
    }

    public void renderToolTip(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.renderTooltip(Minecraft.getInstance().font, stack, mouseX, mouseY);
    }
}
