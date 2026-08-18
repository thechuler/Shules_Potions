package net.shule.shulespotions.Renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.shule.shulespotions.MobEffects.ModMobEffects;

public class ButterFingersLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {


    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    "shulespotions",
                    "textures/misc/butter_fingers_overlay.png"
            );

    public ButterFingersLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {


        if (!entity.getPersistentData().getBoolean("shulespotions:has_butter_fingers")) {
            return;
        }


        float r = 1.0f;
        float g = 1.0f;
        float b = 1.0f;
        float alpha = 1.0f;

        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));

        this.getParentModel().renderToBuffer(
                poseStack,
                vc,
                light,
                OverlayTexture.NO_OVERLAY,
                r,
                g,
                b,
                alpha
        );
    }
}
