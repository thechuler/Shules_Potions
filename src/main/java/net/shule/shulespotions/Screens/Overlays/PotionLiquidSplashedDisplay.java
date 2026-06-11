package net.shule.shulespotions.Screens.Overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(modid = ShulesPotions.MODID, value = Dist.CLIENT)
public class PotionLiquidSplashedDisplay {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) {
            return;
        }

        if (!mc.player.hasEffect(ModMobEffects.POTION_SPLASHED.get())) {
            return;
        }

        renderOverlay(event.getGuiGraphics());
    }

    private static void renderOverlay(GuiGraphics guiGraphics) {
        Minecraft mc = Minecraft.getInstance();

        int color = mc.player.getPersistentData()
                .getInt("PotionSplashColor");

        float r = ((color >> 16) & 255) / 255f;
        float g = ((color >> 8) & 255) / 255f;
        float b = (color & 255) / 255f;

        RenderSystem.enableBlend();

        guiGraphics.setColor(r, g, b, 0.6F);

        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                "shulespotions",
                "textures/misc/covered_potion_liquid_screen.png"
        );

        guiGraphics.blit(
                texture,
                0,
                0,
                0,
                0,
                mc.getWindow().getGuiScaledWidth(),
                mc.getWindow().getGuiScaledHeight(),
                mc.getWindow().getGuiScaledWidth(),
                mc.getWindow().getGuiScaledHeight()
        );

        guiGraphics.setColor(1F, 1F, 1F, 1F);

        RenderSystem.disableBlend();
    }
}