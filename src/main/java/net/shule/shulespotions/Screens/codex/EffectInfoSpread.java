package net.shule.shulespotions.Screens.codex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.shule.shulespotions.Potions.IngredientStat;
import net.shule.shulespotions.Potions.ItemStatRegistry;
import net.shule.shulespotions.Screens.IngredientButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EffectInfoSpread extends CodexSpread {

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(
                    "shulespotions",
                    "textures/gui/effect_codex_screen_effect.png"
            );

    private final MobEffect effect;
    private int guiX, guiY;
    private double scrollOffset = 0;
    private int maxScroll = 0;
    private final List<Button> itemButtons = new ArrayList<>();
    private List<Item> sortedIngredients = new ArrayList<>();

    public EffectInfoSpread(MobEffect effect) {
        this.effect = effect;
    }

    @Override
    public ResourceLocation getBackground() {
        return BACKGROUND;
    }

    @Override
    public void init(int x, int y) {
        this.guiX = x;
        this.guiY = y;
        ResourceLocation backTex = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/button_back.png");

        parent.addSpreadWidget(
                new ImageButton(x + 105, y + BaseCodexScreen.GUI_HEIGHT - 15, 20, 20, 0, 0, 20, backTex, 20, 40, b -> parent.popSpread())
        );
        
        ResourceLocation effectId = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        if (effectId != null) {
            sortedIngredients = ItemStatRegistry.getEntries()
                    .entrySet().stream()
                    .filter(e -> e.getValue().getEffectWeight(effectId) != 0)
                    .sorted((a, b) -> Integer.compare(
                            b.getValue().getEffectWeight(effectId),
                            a.getValue().getEffectWeight(effectId)
                    ))
                    .map(Map.Entry::getKey)
                    .toList();
        }

        rebuildItemButtons(x, y);
    }

    private void rebuildItemButtons(int x, int y) {
        itemButtons.clear();

        int columns = 4;
        int iconSize = 28;
        int spacing = iconSize + 6;

        int rows = (int) Math.ceil(sortedIngredients.size() / (double) columns);
        int totalContentHeight = rows * spacing;
        int visibleHeight = 150;
        maxScroll = Math.max(0, totalContentHeight - visibleHeight);
        scrollOffset = 0;

        int gridWidth = columns * spacing - 6;
        int rightPageStart = x + 288 - gridWidth / 2;
        int startY = y + 55;

        for (int i = 0; i < sortedIngredients.size(); i++) {
            Item item = sortedIngredients.get(i);
            ItemStack stack = new ItemStack(item);

            int col = i % columns;
            int row = i / columns;

            int btnX = rightPageStart + col * spacing;
            int btnY = startY + row * spacing - (int) scrollOffset;

            IngredientButton btn = new IngredientButton(btnX, btnY, iconSize, stack, b -> {
                parent.pushSpread(new IngredientInfoSpread(item));
            });
            itemButtons.add(btn);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (sortedIngredients.isEmpty()) return false;

        int columns = 4;
        int spacing = 34;
        int gridWidth = columns * spacing - 6;
        int rightPageStart = guiX + 288 - gridWidth / 2;
        int startY = guiY + 55;
        int visibleHeight = 150;

        if (mouseX >= rightPageStart - 5 && mouseX <= rightPageStart + gridWidth + 5 &&
            mouseY >= startY - 5 && mouseY <= startY + visibleHeight) {
            
            for (Button btn : itemButtons) {
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
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y) {
        renderEffectIcon(graphics, x, y);
        renderEffectInfo(graphics, x, y);
        renderIngredients(graphics, x, y, mouseX, mouseY, partialTick);
    }

    private void renderEffectIcon(GuiGraphics graphics, int guiX, int guiY) {
        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        if (id == null) return;

        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                id.getNamespace(),
                "textures/mob_effect/" + id.getPath() + ".png"
        );

        int iconX = guiX + 74;
        int iconY = guiY + 35;

        graphics.pose().pushPose();
        graphics.pose().translate(iconX, iconY, 0);

        float scale = 64F / 18F;
        graphics.pose().scale(scale, scale, 1F);
        graphics.blit(texture, 0, 0, 0, 0, 18, 18, 18, 18);
        graphics.pose().popPose();
    }

    private void renderEffectInfo(GuiGraphics graphics, int guiX, int guiY) {
        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        int leftPageCenter = guiX + 96;
        float scale = 1.5F;
        int titleOffsetX = 10;
        int textX = guiX + 28 + titleOffsetX;
        int textY = guiY + 110;

        Component title = Component.translatable(effect.getDescriptionId());
        int titleX = leftPageCenter - Math.round(Minecraft.getInstance().font.width(title) * scale / 2.0F) + titleOffsetX;
        int titleY = guiY + 22;

        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, 1.0F);
        graphics.drawString(Minecraft.getInstance().font, title,
                Math.round(titleX / scale),
                Math.round(titleY / scale),
                effect.getColor(),
                false);
        graphics.pose().popPose();

        graphics.drawString(Minecraft.getInstance().font,
                Component.translatable("shulespotions.screen.effect_codex.source"),
                textX + 25, textY, 0x7A6A52, false);
        graphics.drawString(Minecraft.getInstance().font,
                id != null ? id.getNamespace() : "-",
                textX + 25 + 45, textY, 0x3A3A3A, false);

        textY += 18;

        graphics.drawString(Minecraft.getInstance().font,
                Component.translatable("shulespotions.screen.effect_codex.category"),
                textX + 20, textY, 0x7A6A52, false);
        graphics.drawString(Minecraft.getInstance().font,
                Component.translatable(effect.isBeneficial()
                        ? "shulespotions.screen.effect_codex.beneficial"
                        : "shulespotions.screen.effect_codex.harmful"),
                textX + 20 + 55, textY, effect.isBeneficial() ? 0x00640b : 0xc10000, false);

        textY += 25;

        Component descTitle = Component.translatable("shulespotions.screen.effect_codex.description");
        graphics.drawString(Minecraft.getInstance().font,
                descTitle,
                leftPageCenter - Minecraft.getInstance().font.width(descTitle) / 2 + titleOffsetX,
                textY,
                0x5E4A32,
                false);

        textY += 15;
        int descriptionWidth = 130;

        String forcedDescKey = "effect." + id.getNamespace() + "." + id.getPath() + ".description";

        graphics.drawWordWrap(
                Minecraft.getInstance().font,
                Component.translatableWithFallback(
                        forcedDescKey,
                        Component.translatable("shulespotions.screen.effect_codex.missing_description").getString()
                ),
                leftPageCenter - descriptionWidth / 2 + titleOffsetX + 5,
                textY,
                descriptionWidth,
                0x3A3A3A
        );
    }

    private void renderIngredients(GuiGraphics graphics, int guiX, int guiY, int mouseX, int mouseY, float partialTick) {
        if (sortedIngredients.isEmpty()) return;

        Component title = Component.translatable("shulespotions.screen.effect_codex.ingredients");
        float scaleText = 1.5F;
        int scaledWidth = Math.round(Minecraft.getInstance().font.width(title) * scaleText);
        
        graphics.pose().pushPose();
        graphics.pose().scale(scaleText, scaleText, 1.0F);

        int rightPageCenter = guiX + 288;
        int titleX = rightPageCenter - scaledWidth / 2;
        int titleY = guiY + 18;
        graphics.drawString(Minecraft.getInstance().font, title, Math.round(titleX / scaleText), Math.round(titleY / scaleText), 0x5E4A32, false);
        
        graphics.pose().popPose();

        int columns = 4;
        int spacing = 34;
        int gridWidth = columns * spacing - 6;
        int rightPageStart = rightPageCenter - gridWidth / 2;
        int startY = guiY + 55;
        int visibleHeight = 150;

        graphics.enableScissor(rightPageStart - 5, startY - 5, rightPageStart + gridWidth + 5, startY + visibleHeight);

        boolean mouseInBox = mouseX >= rightPageStart - 5 && mouseX <= rightPageStart + gridWidth + 5 &&
                             mouseY >= startY - 5 && mouseY <= startY + visibleHeight;
        
        int renderMouseX = mouseInBox ? mouseX : -1;
        int renderMouseY = mouseInBox ? mouseY : -1;

        for (int i = 0; i < itemButtons.size(); i++) {
            Button btn = itemButtons.get(i);
            int row = i / columns;
            btn.setY(startY + row * spacing - (int) scrollOffset);
            btn.render(graphics, renderMouseX, renderMouseY, partialTick);
        }

        graphics.disableScissor();

        if (mouseInBox) {
            for (Button btn : itemButtons) {
                if (btn instanceof IngredientButton ib && ib.isHovered()) {
                    ib.renderToolTip(graphics, mouseX, mouseY);
                }
            }
        }

        if (maxScroll > 0) {
            int scrollBarX = rightPageStart + gridWidth + 8;
            int scrollBarY = startY;
            int scrollBarHeight = visibleHeight - 5;
            int thumbHeight = Math.max(10, scrollBarHeight * visibleHeight / (visibleHeight + maxScroll));
            int thumbY = scrollBarY + (int) ((scrollBarHeight - thumbHeight) * (scrollOffset / (float) maxScroll));
            
            graphics.fill(scrollBarX, scrollBarY, scrollBarX + 2, scrollBarY + scrollBarHeight, 0x44000000);
            graphics.fill(scrollBarX, thumbY, scrollBarX + 2, thumbY + thumbHeight, 0xFF5E4A32);
        }
    }
}
