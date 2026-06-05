package net.shule.shulespotions.Renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.util.ColorUtils;

public class CoveredOnPotionLiquidLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    "shulespotions",
                    "textures/misc/covered_potion_liquid.png"
            );
    public CoveredOnPotionLiquidLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {



        float[] color = ColorUtils.intToRGB(entity.getPersistentData().getInt("PotionSplashColor"));


        VertexConsumer vc = buffer.getBuffer(RenderType.entityCutout(TEXTURE));

        this.getParentModel().renderToBuffer(
                poseStack,
                vc,
                light,
                OverlayTexture.NO_OVERLAY,
                color[0], color[1], color[2], 0.6f
        );
    }
}