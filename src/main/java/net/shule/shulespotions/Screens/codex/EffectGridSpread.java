package net.shule.shulespotions.Screens.codex;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;
import net.shule.shulespotions.Screens.EffectButton;
import net.shule.shulespotions.Screens.codex.base.PagedGridSpread;

public class EffectGridSpread extends PagedGridSpread<MobEffect> {

    private static final int EFFECT_SIZE = 28;
    private static final int EFFECT_SPACING_X = 34;
    private static final int EFFECT_SPACING_Y = 34;
    private static final int COLUMNS = 4;
    private static final int ROWS = 5;

    public EffectGridSpread() {
        super(ForgeRegistries.MOB_EFFECTS.getValues().stream().toList(), COLUMNS, ROWS);
    }

    @Override
    protected void buildPageWidgets(int startIndex, int guiX, int guiY) {
        int pageWidth = BaseCodexScreen.GUI_WIDTH / 2;
        int gridWidth = (COLUMNS - 1) * EFFECT_SPACING_X + EFFECT_SIZE;
        int gridHeight = (ROWS - 1) * EFFECT_SPACING_Y + EFFECT_SIZE;

        int leftPageStart = guiX + (pageWidth - gridWidth) / 2;
        int rightPageStart = guiX + pageWidth + (pageWidth - gridWidth) / 2;
        int topStart = guiY + (BaseCodexScreen.GUI_HEIGHT - gridHeight) / 2;

        for (int i = 0; i < itemsPerPage; i++) {
            int effectIndex = startIndex + i;
            if (effectIndex >= items.size()) break;

            MobEffect effect = items.get(effectIndex);
            boolean rightPage = i >= (COLUMNS * ROWS);
            int localIndex = rightPage ? i - (COLUMNS * ROWS) : i;

            int column = localIndex % COLUMNS;
            int row = localIndex / COLUMNS;

            int iconX = (rightPage ? rightPageStart : leftPageStart) + column * EFFECT_SPACING_X;
            int iconY = topStart + row * EFFECT_SPACING_Y;

            EffectButton button = new EffectButton(
                    iconX, iconY, EFFECT_SIZE, effect,
                    b -> parent.pushSpread(new EffectInfoSpread(effect))
            );

            pageButtons.add(button);
            parent.addSpreadWidget(button);
        }
    }
}
