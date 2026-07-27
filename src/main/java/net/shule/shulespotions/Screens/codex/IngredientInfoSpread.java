package net.shule.shulespotions.Screens.codex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.shule.shulespotions.Potions.IngredientStat;
import net.shule.shulespotions.Potions.ItemStatRegistry;
import net.shule.shulespotions.Screens.EffectButton;

public class IngredientInfoSpread extends CodexSpread {

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(
                    "shulespotions",
                    "textures/gui/effect_codex_screen_effect.png"
            );

    private final Item item;

    public IngredientInfoSpread(Item item) {
        this.item = item;
    }

    @Override
    public ResourceLocation getBackground() {
        return BACKGROUND;
    }

    @Override
    public void init(int x, int y) {
        ResourceLocation backTex = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/button_back.png");
        parent.addSpreadWidget(
                new ImageButton(x + 105, y + BaseCodexScreen.GUI_HEIGHT - 15, 20, 20, 0, 0, 20, backTex, 20, 40, b -> parent.popSpread())
        );

        IngredientStat stat = ItemStatRegistry.getEntries().get(item);
        if (stat != null && !stat.getEffectWeights().isEmpty()) {
            int rightPageCenter = x + 288;
            int count = stat.getEffectWeights().size();
            int spacing = 34; 
            int columns = 4;
            int maxCols = Math.min(count, columns);
            int gridWidth = maxCols * spacing - (spacing - 28);
            int startX = rightPageCenter - gridWidth / 2;
            int startY = y + 130; 

            int i = 0;
            var effects = stat.getEffectWeights().entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .toList();

            for (var entry : effects) {
                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(entry.getKey());
                if (effect != null) {
                    int col = i % columns;
                    int row = i / columns;
                    int iconX = startX + col * spacing;
                    int iconY = startY + row * spacing;

                    EffectButton btn = new EffectButton(iconX, iconY, 28, effect, b -> {
                        parent.pushSpread(new EffectInfoSpread(effect));
                    });
                    
                    btn.setTooltip(net.minecraft.client.gui.components.Tooltip.create(
                            Component.translatable(effect.getDescriptionId())
                                     .append(" (" + entry.getValue() + "%)")
                    ));
                    
                    parent.addSpreadWidget(btn);
                }
                i++;
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y) {
        renderItemIcon(graphics, x, y);
        renderItemInfo(graphics, x, y);
        renderStats(graphics, x, y, mouseX, mouseY);
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

    private void renderItemInfo(GuiGraphics graphics, int guiX, int guiY) {
        int leftPageCenter = guiX + 96;
        float scale = 1.5F;
        int titleOffsetX = 10;
        int textY = guiY + 110;

        Component title = Component.translatable(item.getDescriptionId());
        int titleX = leftPageCenter - Math.round(Minecraft.getInstance().font.width(title) * scale / 2.0F) + titleOffsetX;
        int titleY = guiY + 18;

        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, 1.0F);
        graphics.drawString(Minecraft.getInstance().font, title,
                Math.round(titleX / scale),
                Math.round(titleY / scale),
                0x5E4A32,
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

        graphics.drawWordWrap(
                Minecraft.getInstance().font,
                Component.translatableWithFallback(
                        item.getDescriptionId() + ".description",
                        Component.translatable("shulespotions.screen.effect_codex.missing_ingredient_description").getString()
                ),
                leftPageCenter - descriptionWidth / 2 + titleOffsetX + 5,
                textY,
                descriptionWidth,
                0x3A3A3A
        );
    }

    private void renderStats(GuiGraphics graphics, int guiX, int guiY, int mouseX, int mouseY) {
        IngredientStat stat = ItemStatRegistry.getEntries().get(item);
        if (stat == null) return;
        
        int rightPageCenter = guiX + 288;
        
        Component statsTitle = Component.translatable("shulespotions.codex.stats");
        int statsTitleWidth = Minecraft.getInstance().font.width(statsTitle);
        graphics.drawString(Minecraft.getInstance().font, statsTitle, rightPageCenter - statsTitleWidth / 2, guiY + 18, 0x5E4A32, false);
        
        String[] statNames = {"vitality", "flavor", "stability", "purity", "duration"};
        int[] statValues = {stat.getVitality(), stat.getFlavor(), stat.getStability(), stat.getPurity(), stat.getDurationSeconds()};
        
        int spacing = 28;
        int startX = rightPageCenter - (5 * spacing) / 2 + 5;
        
        for (int i = 0; i < 5; i++) {
            String sName = statNames[i];
            int sVal = statValues[i];
            
            int iconX = startX + i * spacing;
            int iconY = guiY + 45;
            
            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/" + sName + "_icon.png");
            
            graphics.pose().pushPose();
            graphics.pose().translate(iconX, iconY, 0);
            graphics.blit(texture, 0, 0, 0, 0, 18, 18, 18, 18);
            graphics.pose().popPose();
            
            String valStr = sName.equals("duration") ? stat.getDurationFormatted() : String.valueOf(sVal);
            int color = 0x3A3A3A;
            if (!sName.equals("duration")) {
                if (sVal > 0) color = 0x008800; // Verde oscuro para que lea bien
                else if (sVal < 0) color = 0xAA0000; // Rojo
            }
            
            int valWidth = Minecraft.getInstance().font.width(valStr);
            graphics.drawString(Minecraft.getInstance().font, valStr, iconX + 9 - valWidth / 2, iconY + 22, color, false);
            
            if (mouseX >= iconX && mouseX < iconX + 18 && mouseY >= iconY && mouseY < iconY + 18) {
                graphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("shulespotions." + sName + ".name"), mouseX, mouseY);
            }
        }

        if (!stat.getEffectWeights().isEmpty()) {
            Component effectsTitle = Component.translatable("shulespotions.codex.effects");
            int effectsTitleWidth = Minecraft.getInstance().font.width(effectsTitle);
            graphics.drawString(Minecraft.getInstance().font, effectsTitle, rightPageCenter - effectsTitleWidth / 2, guiY + 115, 0x5E4A32, false);
        }
    }
}
