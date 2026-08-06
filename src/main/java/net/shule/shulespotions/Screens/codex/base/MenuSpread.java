package net.shule.shulespotions.Screens.codex.base;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.shule.shulespotions.Screens.ImageTextButton;
import java.util.ArrayList;
import java.util.List;

public abstract class MenuSpread extends CodexSpread {

    private final String titleKey;
    private final List<MenuOption> options = new ArrayList<>();
    private final int startYOffset;

    public MenuSpread(String titleKey, int startYOffset) {
        this.titleKey = titleKey;
        this.startYOffset = startYOffset;
    }

    protected void addMenuOption(String translationKey, Runnable action) {
        options.add(new MenuOption(Component.translatable(translationKey), action));
    }

    protected abstract void registerOptions();

    @Override
    public void init(int x, int y) {
        options.clear();
        registerOptions();
        
        int leftPageCenter = getLeftPageCenter(x);
        int buttonWidth = 100;
        int buttonHeight = 20;

        ResourceLocation bgTex = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/button_background.png");

        int yOffset = startYOffset;
        for (MenuOption option : options) {
            parent.addSpreadWidget(new ImageTextButton(
                    leftPageCenter - buttonWidth / 2, y + yOffset, buttonWidth, buttonHeight,
                    option.title, bgTex, buttonWidth, buttonHeight * 2,
                    b -> option.action.run()
            ));
            yOffset += 25;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y) {
        drawCenteredScaledString(graphics, Component.translatable(titleKey), getLeftPageCenter(x), y + 22, 1.5F, COLOR_TITLE);
    }

    private static class MenuOption {
        final Component title;
        final Runnable action;

        MenuOption(Component title, Runnable action) {
            this.title = title;
            this.action = action;
        }
    }
}
