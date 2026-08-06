package net.shule.shulespotions.Screens.codex.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;
import net.shule.shulespotions.Screens.codex.BaseCodexScreen;

import java.util.ArrayList;
import java.util.List;

public abstract class PagedGridSpread<T> extends CodexSpread {

    protected final List<T> items;
    protected final int itemsPerPage;
    
    protected int currentPage = 0;
    private Button nextButton;
    private Button previousButton;
    protected final List<Button> pageButtons = new ArrayList<>();

    public PagedGridSpread(List<T> items, int columns, int rows) {
        this.items = items;
        this.itemsPerPage = columns * rows * 2;
    }

    @Override
    public void init(int x, int y) {
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
        rebuildPageWidgets(x, y);
    }

    private void rebuildPageWidgets(int x, int y) {
        for (Button button : pageButtons) {
            parent.removeSpreadWidget(button);
        }
        pageButtons.clear();

        int startIndex = currentPage * itemsPerPage;
        buildPageWidgets(startIndex, x, y);
    }

    protected abstract void buildPageWidgets(int startIndex, int guiX, int guiY);

    private void updateButtons() {
        previousButton.active = currentPage > 0;
        nextButton.active = currentPage < getMaxPages() - 1;
    }

    private void nextPage() {
        if (currentPage < getMaxPages() - 1) {
            currentPage++;
            int x = (parent.width - BaseCodexScreen.GUI_WIDTH) / 2;
            int y = (parent.height - BaseCodexScreen.GUI_HEIGHT) / 2;
            rebuildPageWidgets(x, y);
            updateButtons();
        }
    }

    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            int x = (parent.width - BaseCodexScreen.GUI_WIDTH) / 2;
            int y = (parent.height - BaseCodexScreen.GUI_HEIGHT) / 2;
            rebuildPageWidgets(x, y);
            updateButtons();
        }
    }

    protected int getMaxPages() {
        if (items.isEmpty()) return 1;
        return (items.size() + itemsPerPage - 1) / itemsPerPage;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y) {
        graphics.drawCenteredString(
                Minecraft.getInstance().font,
                "Page " + (currentPage + 1) + "/" + getMaxPages(),
                x + BaseCodexScreen.GUI_WIDTH / 2,
                y + BaseCodexScreen.GUI_HEIGHT - 35,
                COLOR_TITLE
        );
    }
    @Override
    public ResourceLocation getBackground() {
        return ResourceLocation.fromNamespaceAndPath("shulespotions","textures/gui/effect_codex_screen_atlas.png");
    }
}
