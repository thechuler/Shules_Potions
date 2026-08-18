package net.shule.shulespotions.Events;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(modid = ShulesPotions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ButterFingersClientEvents {

    @SubscribeEvent
    public static void onRenderArm(RenderArmEvent event) {
        Player player = event.getPlayer();
        if (player.getPersistentData().getBoolean("shulespotions:has_butter_fingers")) {
            
          ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/misc/butter_fingers_overlay.png");
           VertexConsumer vc = event.getMultiBufferSource().getBuffer(RenderType.entityTranslucent(TEXTURE));

            EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);

            if (renderer instanceof PlayerRenderer playerRenderer) {
                if (event.getArm() == HumanoidArm.RIGHT) {
                    playerRenderer.getModel().rightArm.render(event.getPoseStack(), vc, event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                    playerRenderer.getModel().rightSleeve.render(event.getPoseStack(), vc, event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                } else {
                    playerRenderer.getModel().leftArm.render(event.getPoseStack(), vc, event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                    playerRenderer.getModel().leftSleeve.render(event.getPoseStack(), vc, event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }
    }
}
