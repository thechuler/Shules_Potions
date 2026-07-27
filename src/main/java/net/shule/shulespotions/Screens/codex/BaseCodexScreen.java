package net.shule.shulespotions.Screens.codex;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Stack;

public class BaseCodexScreen extends Screen {

    public static final int GUI_WIDTH = 384;
    public static final int GUI_HEIGHT = 240;

    private static final Stack<CodexSpread> history = new Stack<>();
    private static CodexSpread currentSpread;

    public BaseCodexScreen() {
        super(Component.translatable("shulespotions.items.effect_codex"));
        if (currentSpread == null) {
            pushSpread(new IndexSpread());
        } else {
            setSpread(currentSpread);
        }
    }

    public void pushSpread(CodexSpread spread) {
        if (currentSpread != null) {
            currentSpread.onClose();
            history.push(currentSpread);
        }
        setSpread(spread);
    }

    public void popSpread() {
        if (!history.isEmpty()) {
            if (currentSpread != null) {
                currentSpread.onClose();
            }
            CodexSpread previous = history.pop();
            setSpread(previous);
        } else {
            // Reset to IndexSpread if we somehow go back from the main index
            if (!(currentSpread instanceof IndexSpread)) {
                pushSpread(new IndexSpread());
            } else {
                this.onClose();
            }
        }
    }

    public void replaceSpread(CodexSpread spread) {
        if (currentSpread != null) {
            currentSpread.onClose();
        }
        setSpread(spread);
    }

    private void setSpread(CodexSpread spread) {
        currentSpread = spread;
        currentSpread.setParent(this);
        this.rebuildWidgets(); // This triggers init() again
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        if (currentSpread != null) {
            int x = (this.width - GUI_WIDTH) / 2;
            int y = (this.height - GUI_HEIGHT) / 2;
            currentSpread.init(x, y);
        }
    }

    public <T extends net.minecraft.client.gui.components.events.GuiEventListener & net.minecraft.client.gui.components.Renderable & net.minecraft.client.gui.narration.NarratableEntry> T addSpreadWidget(T widget) {
        return this.addRenderableWidget(widget);
    }

    public void removeSpreadWidget(net.minecraft.client.gui.components.events.GuiEventListener widget) {
        this.removeWidget(widget);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        int x = (this.width - GUI_WIDTH) / 2;
        int y = (this.height - GUI_HEIGHT) / 2;

        if (currentSpread != null) {
            graphics.blit(
                    currentSpread.getBackground(),
                    x, y,
                    0, 0,
                    GUI_WIDTH, GUI_HEIGHT,
                    GUI_WIDTH, GUI_HEIGHT
            );

            super.render(graphics, mouseX, mouseY, partialTick);
            currentSpread.render(graphics, mouseX, mouseY, partialTick, x, y);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean handled = false;
        if (currentSpread != null) {
            handled = currentSpread.mouseClicked(mouseX, mouseY, button);
        }
        return handled || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        boolean handled = false;
        if (currentSpread != null) {
            handled = currentSpread.mouseScrolled(mouseX, mouseY, delta);
        }
        return handled || super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
