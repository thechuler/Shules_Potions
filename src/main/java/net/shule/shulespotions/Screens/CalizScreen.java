package net.shule.shulespotions.Screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.shule.shulespotions.Potions.PotionLiquid;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CalizScreen extends Screen {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/moclegui.png");
    private static final ResourceLocation OVERLAY =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/potionoverlay.png");

    private static final int GUI_WIDTH = 90;
    private static final int GUI_HEIGHT = 170;

    private final PotionLiquid liquid;

    public CalizScreen(PotionLiquid liquid) {
        super(Component.literal("Alchemist Caliz"));
        this.liquid = liquid;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        
        int screenWidth = this.width;
        int screenHeight = this.height;

        int x = screenWidth / 2 - GUI_WIDTH / 2;
        int y = screenHeight / 2 - GUI_HEIGHT / 2;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        graphics.blit(TEXTURE, x, y, 0, 0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT);

        if (liquid.getStats().getColor() != -1) {
            int color = liquid.getStats().getColor();
            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8) & 0xFF) / 255f;
            float b = (color & 0xFF) / 255f;

            RenderSystem.setShaderColor(r, g, b, 1.0f);
            graphics.blit(OVERLAY, x, y, 0, 0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT);
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }

        RenderSystem.disableBlend();

        Minecraft mc = Minecraft.getInstance();

        int textX = x + 8;
        int textY = y + 8;
        int spacing = 12;

        graphics.drawString(mc.font, "Sample Info", textX, textY, 0xFFD700);
        
        int currentY = textY + 18;

        if (liquid.getStats().getPurity() != 0) {
            graphics.drawString(mc.font, "Purity: " + liquid.getStats().getPurity(), textX, currentY, 0xFFFFFF);
            currentY += spacing;
        }

        if (liquid.getStats().getVitality() != 0) {
            graphics.drawString(mc.font, "Vitality: " + liquid.getStats().getVitality(), textX, currentY, 0xFF5555);
            currentY += spacing;
        }

        if (liquid.getStats().getFlavor() != 0) {
            graphics.drawString(mc.font, "Flavor: " + liquid.getStats().getFlavor(), textX, currentY, 0xAA0000);
            currentY += spacing;
        }

        if (liquid.getStats().getStability() != 0) {
            graphics.drawString(mc.font, "Stability: " + liquid.getStats().getStability(), textX, currentY, 0x55FFFF);
            currentY += spacing;
        }

        if (liquid.getStats().getDurationSeconds() != 0) {
            graphics.drawString(mc.font, "Duration: " + liquid.getStats().getDurationFormatted(), textX, currentY, 0x55FF55);
            currentY += spacing + 5;
        }

        if (!liquid.getStats().getEffectWeights().isEmpty()) {
            graphics.drawString(mc.font, "Effects:", textX, currentY, 0xFF55FF);
            currentY += spacing + 4;
            int shown = 0;

            for (Map.Entry<ResourceLocation, Integer> entry : liquid.getStats().getEffectWeights().entrySet()) {
                if (shown >= 3) break;

                MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(entry.getKey());
                if (effect == null) continue;

                ResourceLocation icon = ResourceLocation.fromNamespaceAndPath(
                        "minecraft",
                        "textures/mob_effect/" + BuiltInRegistries.MOB_EFFECT.getKey(effect).getPath() + ".png"
                );

                graphics.blit(icon, textX, currentY, 0, 0, 18, 18, 18, 18);
                graphics.drawString(mc.font, entry.getValue() + "%", textX + 22, currentY + 5, 0xFFFFFF);

                currentY += 22;
                shown++;
            }
        }
        
        super.render(graphics, mouseX, mouseY, partialTicks);
    }
}
