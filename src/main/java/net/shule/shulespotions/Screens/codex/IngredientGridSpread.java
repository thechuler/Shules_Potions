package net.shule.shulespotions.Screens.codex;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Potions.ItemStatRegistry;
import net.shule.shulespotions.Screens.IngredientButton;
import net.shule.shulespotions.Screens.codex.base.PagedGridSpread;

public class IngredientGridSpread extends PagedGridSpread<Item> {

    private static final int ITEM_SIZE = 28;
    private static final int ITEM_SPACING_X = 34;
    private static final int ITEM_SPACING_Y = 34;
    private static final int COLUMNS = 4;
    private static final int ROWS = 5;

    public IngredientGridSpread() {
        super(ItemStatRegistry.getEntries().keySet().stream().toList(), COLUMNS, ROWS);
    }

    @Override
    protected void buildPageWidgets(int startIndex, int guiX, int guiY) {
        int pageWidth = BaseCodexScreen.GUI_WIDTH / 2;
        int gridWidth = (COLUMNS - 1) * ITEM_SPACING_X + ITEM_SIZE;
        int gridHeight = (ROWS - 1) * ITEM_SPACING_Y + ITEM_SIZE;

        int leftPageStart = guiX + (pageWidth - gridWidth) / 2;
        int rightPageStart = guiX + pageWidth + (pageWidth - gridWidth) / 2;
        int topStart = guiY + (BaseCodexScreen.GUI_HEIGHT - gridHeight) / 2;

        for (int i = 0; i < itemsPerPage; i++) {
            int itemIndex = startIndex + i;
            if (itemIndex >= items.size()) break;

            Item item = items.get(itemIndex);
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

            pageButtons.add(button);
            parent.addSpreadWidget(button);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y) {
        super.render(graphics, mouseX, mouseY, partialTick, x, y);
        for (Button btn : pageButtons) {
            if (btn instanceof IngredientButton ib && ib.isHovered()) {
                ib.renderToolTip(graphics, mouseX, mouseY);
            }
        }
    }
}
