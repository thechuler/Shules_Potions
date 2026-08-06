package net.shule.shulespotions.Screens.codex.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DoubleTextSpread extends CodexSpread {

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(
                    "shulespotions",
                    "textures/gui/effect_codex_screen.png"
            );

    protected final Component leftTitle;
    protected final Component leftText;
    protected final Component rightTitle;
    protected final Component rightText;

    private int guiX, guiY;

    public DoubleTextSpread(Component leftTitle, Component leftText, Component rightTitle, Component rightText) {
        this.leftTitle = leftTitle;
        this.leftText = leftText;
        this.rightTitle = rightTitle;
        this.rightText = rightText;
    }

    @Override
    public ResourceLocation getBackground() {
        return BACKGROUND;
    }

    @Override
    public void init(int x, int y) {
        this.guiX = x;
        this.guiY = y;
        addBackButton(x, y);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Check left page
        int leftPageCenter = getLeftPageCenter(guiX);
        int titleOffsetX = 10;
        int textYLeft = guiY + 18 + ((leftTitle != null) ? 25 : 10);
        int descWidthLeft = 130;
        int startXLeft = leftPageCenter - descWidthLeft / 2 + titleOffsetX + 5;
        if (handleTextClick(leftText, startXLeft, textYLeft, descWidthLeft, mouseX, mouseY)) {
            return true;
        }

        // Check right page
        int rightPageCenter = getRightPageCenter(guiX);
        int textYRight = guiY + 18 + ((rightTitle != null) ? 25 : 10);
        int descWidthRight = 140;
        int startXRight = rightPageCenter - descWidthRight / 2;
        if (handleTextClick(rightText, startXRight, textYRight, descWidthRight, mouseX, mouseY)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y) {
        renderLeftPage(graphics, x, y);
        renderRightPage(graphics, x, y);

        // Render tooltips
        int leftPageCenter = getLeftPageCenter(guiX);
        int titleOffsetX = 10;
        int textYLeft = guiY + 18 + ((leftTitle != null) ? 25 : 10);
        int descWidthLeft = 130;
        int startXLeft = leftPageCenter - descWidthLeft / 2 + titleOffsetX + 5;
        renderTextHover(graphics, leftText, startXLeft, textYLeft, descWidthLeft, mouseX, mouseY);

        int rightPageCenter = getRightPageCenter(guiX);
        int textYRight = guiY + 18 + ((rightTitle != null) ? 25 : 10);
        int descWidthRight = 140;
        int startXRight = rightPageCenter - descWidthRight / 2;
        renderTextHover(graphics, rightText, startXRight, textYRight, descWidthRight, mouseX, mouseY);
    }

    protected void renderLeftPage(GuiGraphics graphics, int guiX, int guiY) {
        int leftPageCenter = getLeftPageCenter(guiX);
        int textY = guiY + 18;
        int titleOffsetX = 10;

        if (leftTitle != null) {
            drawCenteredScaledString(graphics, leftTitle, leftPageCenter + titleOffsetX, guiY + 18, 1.5F, COLOR_TITLE);
            textY += 25;
        } else {
            textY += 10;
        }

        if (leftText != null) {
            int descriptionWidth = 130;
            graphics.drawWordWrap(
                    Minecraft.getInstance().font,
                    leftText,
                    leftPageCenter - descriptionWidth / 2 + titleOffsetX + 5,
                    textY,
                    descriptionWidth,
                    COLOR_TEXT
            );
        }
    }

    protected void renderRightPage(GuiGraphics graphics, int guiX, int guiY) {
        int rightPageCenter = getRightPageCenter(guiX);
        int textY = guiY + 18;
        
        if (rightTitle != null) {
            drawCenteredScaledString(graphics, rightTitle, rightPageCenter, guiY + 18, 1.5F, COLOR_TITLE);
            textY += 25;
        } else {
            textY += 10;
        }

        if (rightText != null) {
            int descriptionWidth = 140;
            graphics.drawWordWrap(
                    Minecraft.getInstance().font,
                    rightText,
                    rightPageCenter - descriptionWidth / 2,
                    textY,
                    descriptionWidth,
                    COLOR_TEXT
            );
        }
    }
}
