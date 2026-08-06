package net.shule.shulespotions.Screens.codex.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.shule.shulespotions.Screens.codex.BaseCodexScreen;

import java.util.List;

/**
 * A generic codex spread that supports multiple pages of text.
 * Useful for long lore entries that require pagination.
 */
public class PagedTextSpread extends CodexSpread {

    protected final List<PageData> pages;
    protected int currentPage = 0;
    
    private Button nextButton;
    private Button previousButton;
    private int guiX, guiY;

    public PagedTextSpread(List<PageData> pages) {
        this.pages = pages;
    }

    @Override
    public void init(int x, int y) {
        this.guiX = x;
        this.guiY = y;
        ResourceLocation prevTex = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/button_previous.png");
        ResourceLocation nextTex = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/button_next.png");

        previousButton = parent.addSpreadWidget(
                new ImageButton(x + 45, y + BaseCodexScreen.GUI_HEIGHT - 15, 20, 20, 0, 0, 20, prevTex, 20, 40, b -> previousPage())
        );

        nextButton = parent.addSpreadWidget(
                new ImageButton(x + BaseCodexScreen.GUI_WIDTH - 65, y + BaseCodexScreen.GUI_HEIGHT - 15, 20, 20, 0, 0, 20, nextTex, 20, 40, b -> nextPage())
        );

        addBackButton(x, y);
        updateButtons();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (pages.isEmpty()) return false;
        PageData currentData = pages.get(currentPage);

        // Check left page
        int leftPageCenter = getLeftPageCenter(guiX);
        int titleOffsetX = 10;
        int textYLeft = guiY + 18 + ((currentData.leftTitle != null) ? 25 : 10);
        int descWidthLeft = 130;
        int startXLeft = leftPageCenter - descWidthLeft / 2 + titleOffsetX + 5;
        if (handleTextClick(currentData.leftText, startXLeft, textYLeft, descWidthLeft, mouseX, mouseY)) {
            return true;
        }

        // Check right page
        int rightPageCenter = getRightPageCenter(guiX);
        int textYRight = guiY + 18 + ((currentData.rightTitle != null) ? 25 : 10);
        int descWidthRight = 140;
        int startXRight = rightPageCenter - descWidthRight / 2;
        if (handleTextClick(currentData.rightText, startXRight, textYRight, descWidthRight, mouseX, mouseY)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void updateButtons() {
        if (previousButton != null) previousButton.active = currentPage > 0;
        if (nextButton != null) nextButton.active = currentPage < pages.size() - 1;
    }

    private void nextPage() {
        if (currentPage < pages.size() - 1) {
            currentPage++;
            updateButtons();
        }
    }

    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            updateButtons();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y) {
        if (pages.isEmpty()) return;
        
        PageData currentData = pages.get(currentPage);
        renderLeftPage(graphics, currentData, x, y);
        renderRightPage(graphics, currentData, x, y);

        graphics.drawCenteredString(
                Minecraft.getInstance().font,
                "Page " + (currentPage + 1) + "/" + pages.size(),
                x + BaseCodexScreen.GUI_WIDTH / 2,
                y + BaseCodexScreen.GUI_HEIGHT - 35,
                COLOR_TITLE
        );

        // Render tooltips
        int leftPageCenter = getLeftPageCenter(guiX);
        int titleOffsetX = 10;
        int textYLeft = guiY + 18 + ((currentData.leftTitle != null) ? 25 : 10);
        int descWidthLeft = 130;
        int startXLeft = leftPageCenter - descWidthLeft / 2 + titleOffsetX + 5;
        renderTextHover(graphics, currentData.leftText, startXLeft, textYLeft, descWidthLeft, mouseX, mouseY);

        int rightPageCenter = getRightPageCenter(guiX);
        int textYRight = guiY + 18 + ((currentData.rightTitle != null) ? 25 : 10);
        int descWidthRight = 140;
        int startXRight = rightPageCenter - descWidthRight / 2;
        renderTextHover(graphics, currentData.rightText, startXRight, textYRight, descWidthRight, mouseX, mouseY);
    }

    protected void renderLeftPage(GuiGraphics graphics, PageData data, int guiX, int guiY) {
        int leftPageCenter = getLeftPageCenter(guiX);
        int textY = guiY + 18;
        int titleOffsetX = 10;
        
        if (data.leftTitle != null) {
            drawCenteredScaledString(graphics, data.leftTitle, leftPageCenter + titleOffsetX, guiY + 18, 1.5F, COLOR_TITLE);
            textY += 25;
        } else {
            textY += 10;
        }

        if (data.leftText != null) {
            int descriptionWidth = 130;
            graphics.drawWordWrap(
                    Minecraft.getInstance().font,
                    data.leftText,
                    leftPageCenter - descriptionWidth / 2 + titleOffsetX + 5,
                    textY,
                    descriptionWidth,
                    COLOR_TEXT
            );
        }
    }

    protected void renderRightPage(GuiGraphics graphics, PageData data, int guiX, int guiY) {
        int rightPageCenter = getRightPageCenter(guiX);
        int textY = guiY + 18;

        if (data.rightTitle != null) {
            drawCenteredScaledString(graphics, data.rightTitle, rightPageCenter, guiY + 18, 1.5F, COLOR_TITLE);
            textY += 25;
        } else {
            textY += 10;
        }

        if (data.rightText != null) {
            int descriptionWidth = 140;
            graphics.drawWordWrap(
                    Minecraft.getInstance().font,
                    data.rightText,
                    rightPageCenter - descriptionWidth / 2,
                    textY,
                    descriptionWidth,
                    COLOR_TEXT
            );
        }
    }

    public static class PageData {
        public final Component leftTitle;
        public final Component leftText;
        public final Component rightTitle;
        public final Component rightText;

        public PageData(Component leftTitle, Component leftText, Component rightTitle, Component rightText) {
            this.leftTitle = leftTitle;
            this.leftText = leftText;
            this.rightTitle = rightTitle;
            this.rightText = rightText;
        }
    }
}
