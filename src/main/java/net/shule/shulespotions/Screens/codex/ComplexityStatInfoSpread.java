package net.shule.shulespotions.Screens.codex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;
import net.shule.shulespotions.Potions.EffectLevelRegistry;
import net.shule.shulespotions.Screens.EffectButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ComplexityStatInfoSpread extends StatInfoSpread {

    private static final ResourceLocation CONTAINER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/container.png");
    private static final ResourceLocation CONTAINER_HOVER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/container_hover.png");

    private static final ResourceLocation[] LEVEL_TEXTURES = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/level_1.png"),
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/level_2.png"),
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/level_3.png"),
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/level_4.png"),
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/level_5.png"),
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/level_6.png")
    };

    private static final int SELECTOR_BUTTON_SIZE = 20;
    private static final int SELECTOR_SPACING = 22;
    private static final int SELECTOR_TOTAL_WIDTH = 6 * SELECTOR_SPACING - 2; // 130px
    private static final int SELECTOR_Y_OFFSET = 36;

    private static final int COLUMNS = 4;
    private static final int ICON_SIZE = 28;
    private static final int SPACING = 34; // 28 + 6
    private static final int GRID_WIDTH = COLUMNS * SPACING - 6; // 130px
    private static final int GRID_Y_OFFSET = 68;
    private static final int VISIBLE_HEIGHT = 142;

    private int selectedLevel = 1;
    private double scrollOffset = 0;
    private int maxScroll = 0;
    private final List<EffectButton> effectButtons = new ArrayList<>();
    private List<MobEffect> currentLevelEffects = new ArrayList<>();

    public ComplexityStatInfoSpread() {
        super("complexity");
    }

    @Override
    protected void initRightPage(int x, int y) {
        rebuildEffectList();
        rebuildEffectButtons(x, y);
    }

    private void rebuildEffectList() {
        currentLevelEffects = ForgeRegistries.MOB_EFFECTS.getValues().stream()
                .filter(effect -> EffectLevelRegistry.getLevel(effect) == selectedLevel)
                .sorted(Comparator.comparing((MobEffect e) -> {
                    ResourceLocation key = ForgeRegistries.MOB_EFFECTS.getKey(e);
                    return key != null ? key.getNamespace() : "";
                }).thenComparing(e -> {
                    ResourceLocation key = ForgeRegistries.MOB_EFFECTS.getKey(e);
                    return key != null ? key.getPath() : "";
                }))
                .toList();
    }

    private void rebuildEffectButtons(int x, int y) {
        effectButtons.clear();

        int rows = (int) Math.ceil(currentLevelEffects.size() / (double) COLUMNS);
        int totalContentHeight = rows * SPACING;
        maxScroll = Math.max(0, totalContentHeight - VISIBLE_HEIGHT);
        scrollOffset = 0;

        int rightPageStart = getRightPageCenter(x) - GRID_WIDTH / 2;
        int startY = y + GRID_Y_OFFSET;

        for (int i = 0; i < currentLevelEffects.size(); i++) {
            MobEffect effect = currentLevelEffects.get(i);
            int col = i % COLUMNS;
            int row = i / COLUMNS;

            int btnX = rightPageStart + col * SPACING;
            int btnY = startY + row * SPACING - (int) scrollOffset;

            EffectButton btn = new EffectButton(btnX, btnY, ICON_SIZE, effect, b -> {
                parent.pushSpread(new EffectInfoSpread(effect));
            });
            effectButtons.add(btn);
        }
    }

    @Override
    protected boolean handleRightPageClick(double mouseX, double mouseY, int button) {
        int rightPageCenter = getRightPageCenter(guiX);
        int selectorStartX = rightPageCenter - SELECTOR_TOTAL_WIDTH / 2;
        int selectorY = guiY + SELECTOR_Y_OFFSET;

        // Level selector clicks
        for (int i = 1; i <= 6; i++) {
            int btnX = selectorStartX + (i - 1) * SELECTOR_SPACING;
            int btnY = selectorY;
            if (mouseX >= btnX && mouseX < btnX + SELECTOR_BUTTON_SIZE &&
                mouseY >= btnY && mouseY < btnY + SELECTOR_BUTTON_SIZE) {
                if (selectedLevel != i) {
                    selectedLevel = i;
                    scrollOffset = 0;
                    rebuildEffectList();
                    rebuildEffectButtons(guiX, guiY);
                    Minecraft.getInstance().getSoundManager().play(
                            SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                    );
                }
                return true;
            }
        }

        // Effect grid clicks
        if (currentLevelEffects.isEmpty()) return false;

        int rightPageStart = rightPageCenter - GRID_WIDTH / 2;
        int startY = guiY + GRID_Y_OFFSET;

        if (mouseX >= rightPageStart - 5 && mouseX <= rightPageStart + GRID_WIDTH + 5 &&
            mouseY >= startY - 5 && mouseY <= startY + VISIBLE_HEIGHT) {

            for (EffectButton btn : effectButtons) {
                if (btn.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (maxScroll > 0) {
            scrollOffset = net.minecraft.util.Mth.clamp(scrollOffset - delta * 20, 0, maxScroll);
            return true;
        }
        return false;
    }

    @Override
    protected void renderRightPage(GuiGraphics graphics, int x, int y, int mouseX, int mouseY, float partialTick) {
        int rightPageCenter = getRightPageCenter(x);

        // Title: "Effects"
        Component title = Component.translatable("shulespotions.codex.effects");
        drawCenteredScaledString(graphics, title, rightPageCenter, y + 18, 1.5F, COLOR_TITLE);

        // Level selector buttons
        renderLevelSelector(graphics, x, y, mouseX, mouseY);

        // Effects grid
        renderEffectsGrid(graphics, x, y, mouseX, mouseY, partialTick);

        // Selector tooltips on top
        renderLevelSelectorTooltips(graphics, x, y, mouseX, mouseY);
    }

    private void renderLevelSelector(GuiGraphics graphics, int x, int y, int mouseX, int mouseY) {
        int rightPageCenter = getRightPageCenter(x);
        int selectorStartX = rightPageCenter - SELECTOR_TOTAL_WIDTH / 2;
        int selectorY = y + SELECTOR_Y_OFFSET;

        for (int i = 1; i <= 6; i++) {
            int btnX = selectorStartX + (i - 1) * SELECTOR_SPACING;
            int btnY = selectorY;
            boolean hovered = mouseX >= btnX && mouseX < btnX + SELECTOR_BUTTON_SIZE &&
                              mouseY >= btnY && mouseY < btnY + SELECTOR_BUTTON_SIZE;
            boolean isSelected = (selectedLevel == i);

            ResourceLocation containerTex = (isSelected || hovered) ? CONTAINER_HOVER_TEXTURE : CONTAINER_TEXTURE;

            // Render container
            graphics.pose().pushPose();
            graphics.pose().translate(btnX + SELECTOR_BUTTON_SIZE / 2F, btnY + SELECTOR_BUTTON_SIZE / 2F, 0);
            float containerScale = SELECTOR_BUTTON_SIZE / 22F;
            graphics.pose().scale(containerScale, containerScale, 1.0F);
            graphics.blit(containerTex, -11, -11, 0, 0, 22, 22, 22, 22);
            graphics.pose().popPose();

            // Render level icon
            ResourceLocation levelTex = LEVEL_TEXTURES[i - 1];
            graphics.pose().pushPose();
            graphics.pose().translate(btnX + SELECTOR_BUTTON_SIZE / 2F, btnY + SELECTOR_BUTTON_SIZE / 2F, 0);
            float iconScale = 14F / 18F;
            graphics.pose().scale(iconScale, iconScale, 1.0F);
            graphics.blit(levelTex, -9, -9, 0, 0, 18, 18, 18, 18);
            graphics.pose().popPose();

            // Indicator below selected button
            if (isSelected) {
                graphics.fill(btnX + 3, btnY + SELECTOR_BUTTON_SIZE, btnX + SELECTOR_BUTTON_SIZE - 3, btnY + SELECTOR_BUTTON_SIZE + 2, 0xFF5E4A32);
            }
        }
    }

    private void renderLevelSelectorTooltips(GuiGraphics graphics, int x, int y, int mouseX, int mouseY) {
        int rightPageCenter = getRightPageCenter(x);
        int selectorStartX = rightPageCenter - SELECTOR_TOTAL_WIDTH / 2;
        int selectorY = y + SELECTOR_Y_OFFSET;

        for (int i = 1; i <= 6; i++) {
            int btnX = selectorStartX + (i - 1) * SELECTOR_SPACING;
            int btnY = selectorY;
            if (mouseX >= btnX && mouseX < btnX + SELECTOR_BUTTON_SIZE &&
                mouseY >= btnY && mouseY < btnY + SELECTOR_BUTTON_SIZE) {
                graphics.renderTooltip(
                        Minecraft.getInstance().font,
                        Component.translatable("shulespotions.screen.effect_codex.level").append(String.valueOf(i)),
                        mouseX, mouseY
                );
                break;
            }
        }
    }

    private void renderEffectsGrid(GuiGraphics graphics, int guiX, int guiY, int mouseX, int mouseY, float partialTick) {
        int rightPageCenter = getRightPageCenter(guiX);
        int rightPageStart = rightPageCenter - GRID_WIDTH / 2;
        int startY = guiY + GRID_Y_OFFSET;

        if (currentLevelEffects.isEmpty()) {
            drawCenteredScaledString(graphics,
                    Component.translatableWithFallback(
                            "shulespotions.screen.effect_codex.no_effects",
                            "No effects registered for this level"
                    ),
                    rightPageCenter, startY + 40, 1.0F, COLOR_TITLE
            );
            return;
        }

        graphics.enableScissor(rightPageStart - 5, startY - 5, rightPageStart + GRID_WIDTH + 5, startY + VISIBLE_HEIGHT);

        boolean mouseInBox = mouseX >= rightPageStart - 5 && mouseX <= rightPageStart + GRID_WIDTH + 5 &&
                             mouseY >= startY - 5 && mouseY <= startY + VISIBLE_HEIGHT;

        int renderMouseX = mouseInBox ? mouseX : -1;
        int renderMouseY = mouseInBox ? mouseY : -1;

        for (int i = 0; i < effectButtons.size(); i++) {
            EffectButton btn = effectButtons.get(i);
            int row = i / COLUMNS;
            btn.setY(startY + row * SPACING - (int) scrollOffset);
            btn.render(graphics, renderMouseX, renderMouseY, partialTick);
        }

        graphics.disableScissor();

        if (maxScroll > 0) {
            int scrollBarX = rightPageStart + GRID_WIDTH + 8;
            int scrollBarY = startY;
            int scrollBarHeight = VISIBLE_HEIGHT - 5;
            int thumbHeight = Math.max(10, scrollBarHeight * VISIBLE_HEIGHT / (VISIBLE_HEIGHT + maxScroll));
            int thumbY = scrollBarY + (int) ((scrollBarHeight - thumbHeight) * (scrollOffset / (float) maxScroll));

            graphics.fill(scrollBarX, scrollBarY, scrollBarX + 2, scrollBarY + scrollBarHeight, 0x44000000);
            graphics.fill(scrollBarX, thumbY, scrollBarX + 2, thumbY + thumbHeight, 0xFF5E4A32);
        }
    }
}
