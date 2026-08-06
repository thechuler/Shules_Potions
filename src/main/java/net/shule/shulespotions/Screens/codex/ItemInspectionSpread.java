package net.shule.shulespotions.Screens.codex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Screens.codex.base.CodexSpread;

public class ItemInspectionSpread extends CodexSpread {

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(
                    "shulespotions",
                    "textures/gui/effect_codex_screen_effect.png"
            );

    private final Item item;

    public ItemInspectionSpread(Item item) {
        this.item = item;
    }

    @Override
    public ResourceLocation getBackground() {
        return BACKGROUND;
    }

    private int guiX, guiY;

    @Override
    public void init(int x, int y) {
        this.guiX = x;
        this.guiY = y;
        addBackButton(x, y);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int leftPageCenter = getLeftPageCenter(guiX);
        int titleOffsetX = 10;
        int textYLeft = guiY + 110;
        int descWidthLeft = 130;
        int startXLeft = leftPageCenter - descWidthLeft / 2 + titleOffsetX + 5;
        Component commentary = Component.translatableWithFallback(
                item.getDescriptionId() + ".commentary",
                Component.translatable("shulespotions.codex.item.commentary").getString()
        );
        if (handleTextClick(commentary, startXLeft, textYLeft, descWidthLeft, mouseX, mouseY)) {
            return true;
        }

        int rightPageCenter = getRightPageCenter(guiX);
        int textYRight = guiY + 18 + 15;
        int descWidthRight = 140;
        int startXRight = rightPageCenter - descWidthRight / 2;
        Component description = Component.translatableWithFallback(item.getDescriptionId() + ".description", Component.translatable("shulespotions.screen.effect_codex.missing_ingredient_description").getString());
        if (handleTextClick(description, startXRight, textYRight, descWidthRight, mouseX, mouseY)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y) {
        renderItemIcon(graphics, x, y);
        renderLeftPage(graphics, x, y);
        renderRightPage(graphics, x, y);
    }

    private void renderItemIcon(GuiGraphics graphics, int guiX, int guiY) {
        ItemStack stack = new ItemStack(item);
        int iconX = guiX + 66;
        int iconY = guiY + 35;

        graphics.pose().pushPose();
        graphics.pose().translate(iconX, iconY, 0);
        float scale = 4.0F;
        graphics.pose().scale(scale, scale, 1F);
        graphics.renderItem(stack, 0, 0);
        graphics.pose().popPose();
    }

    private void renderLeftPage(GuiGraphics graphics, int guiX, int guiY) {
        int leftPageCenter = getLeftPageCenter(guiX);
        int titleOffsetX = 10;
        int textY = guiY + 110;

        Component title = Component.translatable(item.getDescriptionId());
        drawCenteredScaledString(graphics, title, leftPageCenter + titleOffsetX, guiY + 18, 1.5F, COLOR_TITLE);

        int descriptionWidth = 130;
        graphics.drawWordWrap(
                Minecraft.getInstance().font,
                Component.translatableWithFallback(item.getDescriptionId() + ".commentary", Component.translatable("shulespotions.codex.item.commentary").getString()),
                leftPageCenter - descriptionWidth / 2 + titleOffsetX + 5,
                textY,
                descriptionWidth,
                0x7A6A52
        );
    }

    private void renderRightPage(GuiGraphics graphics, int guiX, int guiY) {
        int rightPageCenter = getRightPageCenter(guiX);
        int textY = guiY + 18;

        Component descTitle = Component.translatable("shulespotions.screen.effect_codex.description");
        drawCenteredScaledString(graphics, descTitle, rightPageCenter, textY, 1.0F, COLOR_TITLE);

        textY += 15;
        int descriptionWidth = 140;

        graphics.drawWordWrap(
                Minecraft.getInstance().font,
                Component.translatableWithFallback(item.getDescriptionId() + ".description", Component.translatable("shulespotions.screen.effect_codex.missing_ingredient_description").getString()),
                rightPageCenter - descriptionWidth / 2,
                textY,
                descriptionWidth,
                COLOR_TEXT
        );
    }
}
