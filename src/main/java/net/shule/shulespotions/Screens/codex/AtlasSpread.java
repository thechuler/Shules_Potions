package net.shule.shulespotions.Screens.codex;

import net.minecraft.client.gui.GuiGraphics;
import net.shule.shulespotions.Screens.codex.base.CodexSpread;

public class AtlasSpread extends CodexSpread {

    private final String atlasTitle;

    public AtlasSpread(String title) {
        this.atlasTitle = title;
    }

    @Override
    public void init(int x, int y) {
        addBackButton(x, y);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y) {
        drawCenteredScaledString(graphics, net.minecraft.network.chat.Component.literal(atlasTitle + " Placeholder"), getLeftPageCenter(x), y + 50, 1.5f, COLOR_TITLE);
    }
}
