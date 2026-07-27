package net.shule.shulespotions.Screens.codex;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public abstract class CodexSpread {
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
}
