package net.shule.shulespotions.Entities.Renders;



import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.shule.shulespotions.ShulesPotions;
import net.shule.shulespotions.Entities.Models.PotionSplashProjectileModel;
import net.shule.shulespotions.Entities.Projectile.PotionSplashProjectile;
import net.shule.shulespotions.util.ColorUtils;

public class PotionSplashProjectileRenderer extends EntityRenderer<PotionSplashProjectile> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath
            (ShulesPotions.MODID, "textures/entity/potion_splash_projectile.png");

    private final PotionSplashProjectileModel<PotionSplashProjectile> model;

    public PotionSplashProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new PotionSplashProjectileModel<>(
                context.bakeLayer(PotionSplashProjectileModel.LAYER_LOCATION));
    }

    @Override
    public void render(PotionSplashProjectile entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();
        poseStack.scale(entity.getScale(), entity.getScale(), entity.getScale());

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot())));

        poseStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(partialTick, entity.xRotO, entity.getXRot())));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
        int pcolor = entity.getPotionColor();
        float[] color = ColorUtils.intToRGB(pcolor);
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
                color[0], color[1], color[2], 1.0F);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(PotionSplashProjectile entity) {
        return TEXTURE;
    }
}