package net.shule.shulespotions.Screens.codex.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.shule.shulespotions.Screens.codex.BaseCodexScreen;

public abstract class CodexSpread {
    public static final int COLOR_TITLE = 0x5E4A32;
    public static final int COLOR_TEXT = 0x3A3A3A;

    protected BaseCodexScreen parent;

    public final void setParent(BaseCodexScreen parent) {
        this.parent = parent;
    }

    public abstract void init(int x, int y);

    public abstract void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y);

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return false;
    }

    public ResourceLocation getBackground() {
        return ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/effect_codex_screen.png");
    }

    public void onClose() {}

    protected int getLeftPageCenter(int guiX) {
        return guiX + 96;
    }

    protected int getRightPageCenter(int guiX) {
        return guiX + 288;
    }

    protected void drawCenteredScaledString(GuiGraphics graphics, Component text, int centerX, int y, float scale, int color) {
        int textWidth = Minecraft.getInstance().font.width(text);
        float maxAllowedWidth = 155.0F;
        
        if (textWidth * scale > maxAllowedWidth) {
            scale = maxAllowedWidth / (float) textWidth;
        }

        graphics.pose().pushPose();
        graphics.pose().translate(centerX, y, 0);
        graphics.pose().scale(scale, scale, 1.0F);
        graphics.drawString(Minecraft.getInstance().font, text, -textWidth / 2, 0, color, false);
        graphics.pose().popPose();
    }

    protected void addBackButton(int guiX, int guiY) {
        ResourceLocation backTex = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/button_back.png");
        parent.addSpreadWidget(
                new ImageButton(guiX + 105, guiY + BaseCodexScreen.GUI_HEIGHT - 15, 20, 20, 0, 0, 20, backTex, 20, 40, b -> parent.popSpread())
        );
    }

    protected net.minecraft.network.chat.Style getStyleAt(Component text, int startX, int startY, int maxWidth, double mouseX, double mouseY) {
        if (text == null || mouseX < startX || mouseX > startX + maxWidth || mouseY < startY) return null;
        
        var font = Minecraft.getInstance().font;
        var lines = font.getSplitter().splitLines(text, maxWidth, net.minecraft.network.chat.Style.EMPTY);
        
        int lineIndex = (int) (mouseY - startY) / font.lineHeight;
        if (lineIndex >= 0 && lineIndex < lines.size()) {
            net.minecraft.network.chat.FormattedText lineText = lines.get(lineIndex);
            net.minecraft.util.FormattedCharSequence line = net.minecraft.locale.Language.getInstance().getVisualOrder(lineText);
            int width = font.width(line);
            if (mouseX <= startX + width) {
                return font.getSplitter().componentStyleAtWidth(line, (int)(mouseX - startX));
            }
        }
        return null;
    }

    protected boolean handleTextClick(Component text, int startX, int startY, int maxWidth, double mouseX, double mouseY) {
        net.minecraft.network.chat.Style style = getStyleAt(text, startX, startY, maxWidth, mouseX, mouseY);
        if (style != null && style.getClickEvent() != null) {
            if (parent != null) {
                parent.handleHyperlink(style.getClickEvent().getValue());
                return true;
            }
        }
        return false;
    }

    protected void renderTextHover(GuiGraphics graphics, Component text, int startX, int startY, int maxWidth, int mouseX, int mouseY) {
        net.minecraft.network.chat.Style style = getStyleAt(text, startX, startY, maxWidth, mouseX, mouseY);
        if (style != null && style.getHoverEvent() != null) {
            graphics.renderComponentHoverEffect(Minecraft.getInstance().font, style, mouseX, mouseY);
        } else if (style != null && style.getClickEvent() != null) {
            graphics.renderTooltip(Minecraft.getInstance().font, Component.literal("Click to open"), mouseX, mouseY);
        }
    }
}
