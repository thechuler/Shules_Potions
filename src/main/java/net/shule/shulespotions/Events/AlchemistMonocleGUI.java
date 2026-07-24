package net.shule.shulespotions.Events;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.ShulesPotions;

import java.util.Map;

@Mod.EventBusSubscriber(modid = ShulesPotions.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AlchemistMonocleGUI {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/moclegui.png");
    private static final ResourceLocation OVERLAY =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/potionoverlay.png");


    private static final int GUI_WIDTH = 90;
    private static final int GUI_HEIGHT = 170;



    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (!hasAlchemistMonocle(mc.player))
            return;

        if (!(mc.hitResult instanceof BlockHitResult blockHit))
            return;

        BlockEntity be = mc.level.getBlockEntity(blockHit.getBlockPos());

        if (!(be instanceof PotionCauldronBE cauldron))
            return;

        PotionLiquid liquid = cauldron.getPotionLiquid();
        renderGui(event, liquid);
        if(liquid.getStats().getColor() == -1)
            return;
        renderPotionOverlay(event, liquid);


    }

    private static boolean hasAlchemistMonocle(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(ModItems.ALCHEMIST_MONOCLE.get())) {
                return true;
            }
        }

        return false;
    }
    private static void renderGui(RenderGuiOverlayEvent.Post event, PotionLiquid liquid) {

        GuiGraphics graphics = event.getGuiGraphics();

        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();

        int x = screenWidth / 2 + 78;
        int y = screenHeight / 2 - GUI_HEIGHT / 2;


        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        graphics.blit(
                TEXTURE,
                x,
                y,
                0,
                0,
                GUI_WIDTH,
                GUI_HEIGHT,
                GUI_WIDTH,
                GUI_HEIGHT
        );

        RenderSystem.disableBlend();


        Minecraft mc = Minecraft.getInstance();


        int textX = x + 8;
        int textY = y + 8;

        int spacing = 12;



        graphics.drawString(
                mc.font,
                "Healing Potion",
                textX,
                textY,
                0xFFD700
        );


        int currentY = textY + 18;



        if (liquid.getStats().getPurity() != 0) {

            graphics.drawString(
                    mc.font,
                    "Purity: " + liquid.getStats().getPurity(),
                    textX,
                    currentY,
                    0xFFFFFF
            );

            currentY += spacing;
        }



        if (liquid.getStats().getVitality() != 0) {

            graphics.drawString(
                    mc.font,
                    "Vitality: " + liquid.getStats().getVitality(),
                    textX,
                    currentY,
                    0xFF5555
            );

            currentY += spacing;
        }



        if (liquid.getStats().getFlavor() != 0) {

            graphics.drawString(
                    mc.font,
                    "Flavor: " + liquid.getStats().getFlavor(),
                    textX,
                    currentY,
                    0xAA0000
            );

            currentY += spacing;
        }



        if (liquid.getStats().getStability() != 0) {

            graphics.drawString(
                    mc.font,
                    "Stability: " + liquid.getStats().getStability(),
                    textX,
                    currentY,
                    0x55FFFF
            );

            currentY += spacing;
        }


        if (liquid.getStats().getDurationSeconds() != 0) {

            graphics.drawString(
                    mc.font,
                    "Duration: " + liquid.getStats().getDurationFormatted(),
                    textX,
                    currentY,
                    0x55FF55
            );

            currentY += spacing + 5;
        }



        if (!liquid.getStats().getEffectWeights().isEmpty()) {


            graphics.drawString(
                    mc.font,
                    "Effects:",
                    textX,
                    currentY,
                    0xFF55FF
            );


            currentY += spacing + 4;


            int shown = 0;


            for (Map.Entry<ResourceLocation, Integer> entry :
                    liquid.getStats().getEffectWeights().entrySet()) {


                if (shown >= 3)
                    break;


                MobEffect effect =
                        BuiltInRegistries.MOB_EFFECT.get(entry.getKey());


                if (effect == null)
                    continue;


                ResourceLocation icon =
                        ResourceLocation.fromNamespaceAndPath(
                                "minecraft",
                                "textures/mob_effect/"
                                        + BuiltInRegistries.MOB_EFFECT
                                        .getKey(effect)
                                        .getPath()
                                        + ".png"
                        );


                // Icono
                graphics.blit(
                        icon,
                        textX,
                        currentY,
                        0,
                        0,
                        18,
                        18,
                        18,
                        18
                );


                // Porcentaje
                graphics.drawString(
                        mc.font,
                        entry.getValue() + "%",
                        textX + 22,
                        currentY + 5,
                        0xFFFFFF
                );


                currentY += 22;
                shown++;
            }
        }
    }

    private static void renderPotionOverlay(RenderGuiOverlayEvent.Post event, PotionLiquid liquid) {

        GuiGraphics graphics = event.getGuiGraphics();

        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();

        int x = screenWidth / 2 + 78;
        int y = screenHeight / 2 - GUI_HEIGHT / 2;


        int color = liquid.getStats().getColor();

        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;


        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();


        // Aplicar color de la poción
        RenderSystem.setShaderColor(r, g, b, 1.0f);


        graphics.blit(
                OVERLAY,
                x,
                y,
                0,
                0,
                GUI_WIDTH,
                GUI_HEIGHT,
                GUI_WIDTH,
                GUI_HEIGHT
        );


        // Restaurar color normal
        RenderSystem.setShaderColor(1, 1, 1, 1);


        RenderSystem.disableBlend();
    }

}
