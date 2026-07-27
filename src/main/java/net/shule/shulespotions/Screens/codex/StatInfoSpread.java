package net.shule.shulespotions.Screens.codex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import net.shule.shulespotions.ShulesPotions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Potions.IngredientStat;
import net.shule.shulespotions.Potions.ItemStatRegistry;
import net.shule.shulespotions.Screens.IngredientButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatInfoSpread extends CodexSpread{

    private final String STAT;
    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(
                    "shulespotions",
                    "textures/gui/effect_codex_screen_effect.png");

    private int guiX, guiY;
    private double scrollOffset = 0;
    private int maxScroll = 0;
    private final List<Button> itemButtons = new ArrayList<>();
    private List<Item> sortedIngredients = new ArrayList<>();

    public StatInfoSpread(String stat) {
        this.STAT = stat;
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
                new net.minecraft.client.gui.components.ImageButton(x + 105, y + BaseCodexScreen.GUI_HEIGHT - 15, 20, 20, 0, 0, 20, backTex, 20, 40, b -> parent.popSpread())
        );
        
        sortedIngredients = ItemStatRegistry.getEntries()
                .entrySet().stream()
                .filter(e -> getStatValue(e.getValue()) != 0)
                .sorted((a, b) -> Integer.compare(
                        getStatValue(b.getValue()),
                        getStatValue(a.getValue())
                ))
                .map(Map.Entry::getKey)
                .toList();

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
        renderStatIcon(graphics, x, y);
        renderStatInfo(graphics, x, y);
        renderIngredients(graphics, x, y, mouseX, mouseY, partialTick);
    }

    private void renderStatIcon(GuiGraphics graphics, int guiX, int guiY) {

        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,
                "textures/gui/" + STAT + "_icon" + ".png"
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


    private void renderStatInfo(GuiGraphics graphics, int guiX, int guiY) {
        int leftPageCenter = guiX + 96;
        float scale = 1.5F;
        int titleOffsetX = 10;
        int textY = guiY + 110;

        Component title = Component.translatable(ShulesPotions.MODID + "." + STAT + ".name");
        int titleX = leftPageCenter - Math.round(Minecraft.getInstance().font.width(title) * scale / 2.0F) + titleOffsetX;
        int titleY = guiY + 22;

        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, 1.0F);
        graphics.drawString(Minecraft.getInstance().font, title,
                Math.round(titleX / scale),
                Math.round(titleY / scale),
                0x7A6A52,
                false);
        graphics.pose().popPose();


        Component descTitle = Component.translatable("shulespotions.screen.effect_codex.description");
        graphics.drawString(Minecraft.getInstance().font,
                descTitle,
                leftPageCenter - Minecraft.getInstance().font.width(descTitle) / 2 + titleOffsetX,
                textY,
                0x5E4A32,
                false);
                
        textY += 15;
        int descriptionWidth = 130;

        Component descriptionText = Component.translatable("shulespotions.screen.effect_codex."+STAT+".description");
        
        graphics.drawWordWrap(
                Minecraft.getInstance().font,
                descriptionText,
                leftPageCenter - descriptionWidth / 2 + titleOffsetX + 5,
                textY,
                descriptionWidth,
                0x3A3A3A
        );
    }

    private int getStatValue(IngredientStat statObj) {
        return switch (this.STAT.toLowerCase()) {
            case "purity" -> statObj.getPurity();
            case "duration" -> statObj.getDurationSeconds();
            case "stability" -> statObj.getStability();
            case "vitality" -> statObj.getVitality();
            case "flavor" -> statObj.getFlavor();
            default -> 0;
        };
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
