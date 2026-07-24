package net.shule.shulespotions.Screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.shule.shulespotions.Potions.IngredientStat;
import net.shule.shulespotions.Potions.ItemStatRegistry;

import java.util.List;
import java.util.Map;

public class EffectInfoScreen extends Screen {

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(
                    "shulespotions",
                    "textures/gui/effect_codex_screen_effect.png"
            );
    private static final ResourceLocation CHECK_ICON =
            ResourceLocation.fromNamespaceAndPath(
                    "shulespotions",
                    "textures/gui/check.png");

    private static final ResourceLocation CROSS_ICON =
            ResourceLocation.fromNamespaceAndPath(
                    "shulespotions",
                    "textures/gui/cross.png");


    private static final int GUI_WIDTH = 384;
    private static final int GUI_HEIGHT = 240;

    private final MobEffect effect;
    private final Screen parent;

    public EffectInfoScreen(Screen parent, MobEffect effect) {
        super(Component.translatable(effect.getDescriptionId()));
        this.parent = parent;
        this.effect = effect;
    }

    @Override
    protected void init() {

        int x = (this.width - GUI_WIDTH) / 2;
        int y = (this.height - GUI_HEIGHT) / 2;

        addRenderableWidget(
                Button.builder(Component.literal("←"), b ->
                                Minecraft.getInstance().setScreen(parent))
                        .bounds(x + 15, y + GUI_HEIGHT - 25, 20, 20)
                        .build()
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        renderBackground(graphics);

        int x = (this.width - GUI_WIDTH) / 2;
        int y = (this.height - GUI_HEIGHT) / 2;

        graphics.blit(
                BACKGROUND,
                x,
                y,
                0,
                0,
                GUI_WIDTH,
                GUI_HEIGHT,
                GUI_WIDTH,
                GUI_HEIGHT
        );

        renderEffectIcon(graphics, x, y);
        renderIngredients(graphics, x, y,mouseX,mouseY);
        renderEffectInfo(graphics, x, y);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderEffectIcon(GuiGraphics graphics, int guiX, int guiY) {

        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);

        if (id == null)
            return;

        ResourceLocation texture =
                ResourceLocation.fromNamespaceAndPath(
                        id.getNamespace(),
                        "textures/mob_effect/" + id.getPath() + ".png"
                );

        int iconX = guiX + 74;
        int iconY = guiY + 35;

        graphics.pose().pushPose();

        graphics.pose().translate(iconX, iconY, 0);


        float scale = 64F / 18F;

        graphics.pose().scale(scale, scale, 1F);

        graphics.blit(
                texture,
                0,
                0,
                0,
                0,
                18,
                18,
                18,
                18
        );

        graphics.pose().popPose();
    }

    private void renderEffectInfo(GuiGraphics graphics, int guiX, int guiY) {

        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);

        int leftPageCenter = guiX + 96;

        float scale = 1.5F;
        int titleOffsetX = 10;

        int textX = guiX + 28 + titleOffsetX;
        int textY = guiY + 110;

        // ===== Title =====

        Component title = Component.translatable(effect.getDescriptionId());

        int titleX = leftPageCenter - Math.round(font.width(title) * scale / 2.0F) + titleOffsetX;
        int titleY = guiY + 22;

        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, 1.0F);

        graphics.drawString(font, title,
                Math.round(titleX / scale),
                Math.round(titleY / scale),
                effect.getColor(),
                false);

        graphics.pose().popPose();

        // ===== Source =====

        graphics.drawString(font,
                Component.translatable("shulespotions.screen.effect_codex.source"),
                textX + 25, textY, 0x7A6A52, false);

        graphics.drawString(font,
                id != null ? id.getNamespace() : "-",
                textX + 25 + 45, textY, 0x3A3A3A, false);

          textY += 18;



        graphics.drawString(font,
                Component.translatable("shulespotions.screen.effect_codex.category"),
                textX + 20, textY, 0x7A6A52, false);

        graphics.drawString(font,
                Component.translatable(effect.isBeneficial()
                        ? "shulespotions.screen.effect_codex.beneficial"
                        : "shulespotions.screen.effect_codex.harmful"),
                textX + 20 + 55, textY, effect.isBeneficial() ? 0x00640b : 0xc10000 , false);

        textY += 25;

        // ===== Description =====

        Component descTitle = Component.translatable("shulespotions.screen.effect_codex.description");

        graphics.drawString(font,
                descTitle,
                leftPageCenter - font.width(descTitle) / 2 + titleOffsetX,
                textY,
                0x5E4A32,
                false);

        textY += 15;

        int descriptionWidth = 130;

        graphics.drawWordWrap(
                font,
                Component.translatableWithFallback(
                        effect.getDescriptionId() + ".description",
                        Component.translatable("shulespotions.screen.effect_codex.missing_description").getString()
                ),
                leftPageCenter - descriptionWidth / 2 + titleOffsetX + 5,
                textY,
                descriptionWidth,
                0x3A3A3A
        );
    }

    private void renderIngredients(GuiGraphics graphics, int guiX, int guiY, int mouseX, int mouseY) {

        ResourceLocation effectId = ForgeRegistries.MOB_EFFECTS.getKey(effect);

        if (effectId == null)
            return;

        List<Map.Entry<Item, IngredientStat>> ingredients =
                ItemStatRegistry.getEntries()
                        .entrySet()
                        .stream()
                        .filter(e -> e.getValue().getEffectWeight(effectId) > 0)
                        .sorted((a, b) -> Integer.compare(
                                b.getValue().getEffectWeight(effectId),
                                a.getValue().getEffectWeight(effectId)
                        ))
                        .toList();

        Component title = Component.translatable("shulespotions.screen.effect_codex.ingredients");

        float scaleText = 1.5F;
        int rightPageCenter = guiX + 288;
        int scaledWidth = Math.round(font.width(title) * scaleText);
        int titleX = rightPageCenter - scaledWidth / 2;
        int titleY = guiY + 18;

        graphics.pose().pushPose();
        graphics.pose().scale(scaleText, scaleText, 1.0F);

        graphics.drawString(
                font,
                title,
                Math.round(titleX / scaleText),
                Math.round(titleY / scaleText),
                0x5E4A32,
                false
        );

        graphics.pose().popPose();

        int count = ingredients.size();

        int columns;
        float scale;

        if (count <= 4) {
            columns = 2;
            scale = 2.0F;
        } else if (count <= 9) {
            columns = 3;
            scale = 1.5F;
        } else if (count <= 20) {
            columns = 5;
            scale = 1.0F;
        } else {
            columns = 6;
            scale = 0.85F;
        }

        int rows = (int)Math.ceil(count / (double)columns);

        int iconSize = Math.round(16 * scale);
        int spacing = iconSize + 6;

        int gridWidth = columns * spacing - 6;
        int gridHeight = rows * spacing - 6;

        int startX = rightPageCenter - gridWidth / 2;
        int startY = guiY + 55 + (150 - gridHeight) / 2;

        for (int i = 0; i < ingredients.size(); i++) {

            int column = i % columns;
            int row = i / columns;

            int x = startX + column * spacing;
            int y = startY + row * spacing;

            ItemStack stack = new ItemStack(ingredients.get(i).getKey());

            boolean hovered =
                    mouseX >= x &&
                            mouseX < x + iconSize &&
                            mouseY >= y &&
                            mouseY < y + iconSize;

            float pulse = (float)Math.sin(
                    (System.currentTimeMillis() % 1000L) / 1000.0 * Math.PI * 2.0
            );

            float renderScale = hovered
                    ? scale * (1.0F + 0.15F * ((pulse + 1F) / 2F))
                    : scale;

            graphics.pose().pushPose();

            graphics.pose().translate(x, y, hovered ? 200 : 0);
            graphics.pose().scale(renderScale, renderScale, 1F);

            graphics.renderItem(stack, 0, 0);

            graphics.pose().popPose();

            if (hovered)
                graphics.renderTooltip(font, stack, mouseX, mouseY);
        }
    }
}