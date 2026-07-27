package net.shule.shulespotions.Screens.codex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AtlasSpread extends CodexSpread {

    private final String atlasTitle;

    public AtlasSpread(String title) {
        this.atlasTitle = title;
    }

    @Override
    public void init(int x, int y) {
        ResourceLocation backTex = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/button_back.png");
        parent.addSpreadWidget(
                new ImageButton(x + 105, y + BaseCodexScreen.GUI_HEIGHT - 15, 20, 20, 0, 0, 20, backTex, 20, 40, b -> parent.popSpread())
        );
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y) {
        int leftPageCenter = x + 96;
        graphics.drawString(Minecraft.getInstance().font, atlasTitle + " Placeholder", leftPageCenter - 40, y + 50, 0x5E4A32, false);
    }
}
