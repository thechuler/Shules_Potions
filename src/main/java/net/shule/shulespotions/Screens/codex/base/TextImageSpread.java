package net.shule.shulespotions.Screens.codex.base;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TextImageSpread extends CodexSpread {

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(
                    "shulespotions",
                    "textures/gui/effect_codex_screen.png"
            );

    protected final Component leftTitle;
    protected final Component leftText;
    protected final ResourceLocation rightImage;
    protected final int imageWidth;
    protected final int imageHeight;
    protected final Component rightImageCaption;

    private int guiX, guiY;

    public TextImageSpread(Component leftTitle, Component leftText, ResourceLocation rightImage, int imageWidth, int imageHeight, Component rightImageCaption) {
        this.leftTitle = leftTitle;
        this.leftText = leftText;
        this.rightImage = rightImage;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.rightImageCaption = rightImageCaption;
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

        // Check right caption
        if (rightImageCaption != null) {
            int rightPageCenter = getRightPageCenter(guiX);
            int imageY = guiY + 35;
            int captionY = imageY + imageHeight + 10;
            int captionWidth = 140;
            int startXRight = rightPageCenter - captionWidth / 2;
            if (handleTextClick(rightImageCaption, startXRight, captionY, captionWidth, mouseX, mouseY)) {
                return true;
            }
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

        if (rightImageCaption != null) {
            int rightPageCenter = getRightPageCenter(guiX);
            int imageY = guiY + 35;
            int captionY = imageY + imageHeight + 10;
            int captionWidth = 140;
            int startXRight = rightPageCenter - captionWidth / 2;
            renderTextHover(graphics, rightImageCaption, startXRight, captionY, captionWidth, mouseX, mouseY);
        }
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
        
        if (rightImage != null) {
            int imageX = rightPageCenter - (imageWidth / 2);
            int imageY = guiY + 35;
            
            RenderSystem.enableBlend();
            graphics.blit(rightImage, imageX, imageY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
            RenderSystem.disableBlend();
            
            if (rightImageCaption != null) {
                int captionY = imageY + imageHeight + 10;
                int captionWidth = 140;
                
                graphics.drawWordWrap(
                        Minecraft.getInstance().font,
                        rightImageCaption,
                        rightPageCenter - captionWidth / 2,
                        captionY,
                        captionWidth,
                        0x7A6A52
                );
            }
        }
    }
}
