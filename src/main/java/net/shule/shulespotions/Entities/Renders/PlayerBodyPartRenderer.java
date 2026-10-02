package net.shule.shulespotions.Entities.Renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.shule.shulespotions.Entities.entity.PlayerBodyPartEntity;
import net.shule.shulespotions.Entities.entity.PlayerHeadEntity;

import java.util.UUID;

public abstract class PlayerBodyPartRenderer<E extends PlayerBodyPartEntity, M extends EntityModel<E>> extends EntityRenderer<E> {

    protected final M model;

    public PlayerBodyPartRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context);
        this.model = model;
        this.shadowRadius = shadowRadius;
    }

    @Override
    public ResourceLocation getTextureLocation(E entity) {
        UUID playerUUID = entity.getPlayerUUID();
        if (playerUUID != null) {
            if (Minecraft.getInstance().getConnection() != null) {
                PlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(playerUUID);
                if (playerInfo != null) {
                    return playerInfo.getSkinLocation();
                }
            }
            return DefaultPlayerSkin.getDefaultSkin(playerUUID);
        }
        return DefaultPlayerSkin.getDefaultSkin(Util.NIL_UUID);
    }

    @Override
    public void render(E entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        float yRot = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yRot));

        float xRot = Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot());
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0D, -1.501D, 0.0D);

        this.model.setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTicks, 0.0F, 0.0F);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
