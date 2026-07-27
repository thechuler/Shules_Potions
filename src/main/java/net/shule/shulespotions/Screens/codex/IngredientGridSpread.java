package net.shule.shulespotions.Screens.codex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.components.ImageButton;
import net.shule.shulespotions.Screens.IngredientButton;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Potions.ItemStatRegistry;

import java.util.ArrayList;
import java.util.List;

public class IngredientGridSpread extends CodexSpread {

    private final List<Item> ingredients;
    private static final int ITEM_SIZE = 28;
    private static final int ITEM_SPACING_X = 34;
    private static final int ITEM_SPACING_Y = 34;
    private static final int COLUMNS = 4;
    private static final int ROWS = 5;
    private static final int ITEMS_PER_PAGE = COLUMNS * ROWS * 2;

    private int currentPage = 0;
    private Button nextButton;
    private Button previousButton;
    private final List<Button> itemButtons = new ArrayList<>();

    public IngredientGridSpread() {
        this.ingredients = ItemStatRegistry.getEntries().keySet().stream().toList();
    }

    @Override
    public void init(int x, int y) {
        ResourceLocation backTex = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/button_back.png");
        ResourceLocation prevTex = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/button_previous.png");
        ResourceLocation nextTex = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/button_next.png");

        previousButton = parent.addSpreadWidget(
                new ImageButton(x + 45, y + BaseCodexScreen.GUI_HEIGHT - 15, 20, 20, 0, 0, 20, prevTex, 20, 40, b -> previousPage())
        );

        nextButton = parent.addSpreadWidget(
                new ImageButton(x + BaseCodexScreen.GUI_WIDTH - 65, y + BaseCodexScreen.GUI_HEIGHT - 15, 20, 20, 0, 0, 20, nextTex, 20, 40, b -> nextPage())
        );

        parent.addSpreadWidget(
                new ImageButton(x + 105, y + BaseCodexScreen.GUI_HEIGHT - 15, 20, 20, 0, 0, 20, backTex, 20, 40, b -> parent.popSpread())
        );
        updateButtons();
        rebuildItemButtons(x, y);
    }

    private void rebuildItemButtons(int x, int y) {
        for (Button button : itemButtons) {
            parent.removeSpreadWidget(button);
        }
        itemButtons.clear();

        int startIndex = currentPage * ITEMS_PER_PAGE;
        int pageWidth = BaseCodexScreen.GUI_WIDTH / 2;
        int gridWidth = (COLUMNS - 1) * ITEM_SPACING_X + ITEM_SIZE;
        int gridHeight = (ROWS - 1) * ITEM_SPACING_Y + ITEM_SIZE;

        int leftPageStart = x + (pageWidth - gridWidth) / 2;
        int rightPageStart = x + pageWidth + (pageWidth - gridWidth) / 2;
        int topStart = y + (BaseCodexScreen.GUI_HEIGHT - gridHeight) / 2;

        for (int i = 0; i < ITEMS_PER_PAGE; i++) {
            int itemIndex = startIndex + i;
            if (itemIndex >= ingredients.size()) break;

            Item item = ingredients.get(itemIndex);
            ItemStack stack = new ItemStack(item);
            
            boolean rightPage = i >= (COLUMNS * ROWS);
            int localIndex = rightPage ? i - (COLUMNS * ROWS) : i;

            int column = localIndex % COLUMNS;
            int row = localIndex / COLUMNS;

            int iconX = (rightPage ? rightPageStart : leftPageStart) + column * ITEM_SPACING_X;
            int iconY = topStart + row * ITEM_SPACING_Y;

            IngredientButton button = new IngredientButton(iconX, iconY, ITEM_SIZE, stack, b -> {
                parent.pushSpread(new IngredientInfoSpread(item));
            });

            itemButtons.add(button);
            parent.addSpreadWidget(button);
        }
    }

    private void updateButtons() {
        previousButton.active = currentPage > 0;
        nextButton.active = currentPage < getMaxPages() - 1;
    }

    private void nextPage() {
        if (currentPage < getMaxPages() - 1) {
            currentPage++;
            int x = (parent.width - BaseCodexScreen.GUI_WIDTH) / 2;
            int y = (parent.height - BaseCodexScreen.GUI_HEIGHT) / 2;
            rebuildItemButtons(x, y);
            updateButtons();
        }
    }

    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            int x = (parent.width - BaseCodexScreen.GUI_WIDTH) / 2;
            int y = (parent.height - BaseCodexScreen.GUI_HEIGHT) / 2;
            rebuildItemButtons(x, y);
            updateButtons();
        }
    }

    private int getMaxPages() {
        if (ingredients.isEmpty()) return 1;
        return (ingredients.size() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y) {
        graphics.drawCenteredString(
                Minecraft.getInstance().font,
                "Page " + (currentPage + 1) + "/" + getMaxPages(),
                x + BaseCodexScreen.GUI_WIDTH / 2,
                y + BaseCodexScreen.GUI_HEIGHT - 35,
                0x5E4A32
        );

        for (Button btn : itemButtons) {
            if (btn instanceof IngredientButton ib && ib.isHovered()) {
                ib.renderToolTip(graphics, mouseX, mouseY);
            }
        }
    }
}
