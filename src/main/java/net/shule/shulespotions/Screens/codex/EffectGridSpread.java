package net.shule.shulespotions.Screens.codex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;
import net.shule.shulespotions.Screens.EffectButton;

import java.util.ArrayList;
import java.util.List;

public class EffectGridSpread extends CodexSpread {

    private final List<MobEffect> effects;
    private static final int EFFECT_SIZE = 28;
    private static final int EFFECT_SPACING_X = 34;
    private static final int EFFECT_SPACING_Y = 34;
    private static final int COLUMNS = 4;
    private static final int ROWS = 5;
    private static final int EFFECTS_PER_PAGE = COLUMNS * ROWS * 2;

    private int currentPage = 0;
    private Button nextButton;
    private Button previousButton;
    private final List<EffectButton> effectButtons = new ArrayList<>();

    public EffectGridSpread() {
        this.effects = ForgeRegistries.MOB_EFFECTS.getValues().stream().toList();
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

        // Back button to return to index
        parent.addSpreadWidget(
                new ImageButton(x + 105, y + BaseCodexScreen.GUI_HEIGHT - 15, 20, 20, 0, 0, 20, backTex, 20, 40, b -> parent.popSpread())
        );

        updateButtons();
        rebuildEffectButtons(x, y);
    }

    private void rebuildEffectButtons(int x, int y) {
        for (EffectButton button : effectButtons) {
            parent.removeSpreadWidget(button);
        }
        effectButtons.clear();

        int startIndex = currentPage * EFFECTS_PER_PAGE;
        int pageWidth = BaseCodexScreen.GUI_WIDTH / 2;
        int gridWidth = (COLUMNS - 1) * EFFECT_SPACING_X + EFFECT_SIZE;
        int gridHeight = (ROWS - 1) * EFFECT_SPACING_Y + EFFECT_SIZE;

        int leftPageStart = x + (pageWidth - gridWidth) / 2;
        int rightPageStart = x + pageWidth + (pageWidth - gridWidth) / 2;
        int topStart = y + (BaseCodexScreen.GUI_HEIGHT - gridHeight) / 2;

        for (int i = 0; i < EFFECTS_PER_PAGE; i++) {
            int effectIndex = startIndex + i;
            if (effectIndex >= effects.size()) break;

            MobEffect effect = effects.get(effectIndex);
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

            effectButtons.add(button);
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
            rebuildEffectButtons(x, y);
            updateButtons();
        }
    }

    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            int x = (parent.width - BaseCodexScreen.GUI_WIDTH) / 2;
            int y = (parent.height - BaseCodexScreen.GUI_HEIGHT) / 2;
            rebuildEffectButtons(x, y);
            updateButtons();
        }
    }

    private int getMaxPages() {
        return (effects.size() + EFFECTS_PER_PAGE - 1) / EFFECTS_PER_PAGE;
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

    }
}
