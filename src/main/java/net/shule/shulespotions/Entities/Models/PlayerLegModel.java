package net.shule.shulespotions.Entities.Models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.shule.shulespotions.Entities.entity.PlayerLegEntity;
import net.shule.shulespotions.ShulesPotions;

public class PlayerLegModel<T extends PlayerLegEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID, "player_leg"), "main");

    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public PlayerLegModel(ModelPart root) {
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();


        partdefinition.addOrReplaceChild("right_leg",
                CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 32).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
                PartPose.offset(0.0F, 22.0F, 0.0F)
        );


        partdefinition.addOrReplaceChild("left_leg",
                CubeListBuilder.create()
                        .texOffs(16, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
                PartPose.offset(0.0F, 22.0F, 0.0F)
        );

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean isLeft = entity.isLeftLeg();
        this.rightLeg.visible = !isLeft;
        this.leftLeg.visible = isLeft;


        float lyingPitch = (float) (Math.PI / 2.0);
        this.rightLeg.xRot = lyingPitch;
        this.rightLeg.yRot = 0.0F;
        this.rightLeg.zRot = 0.0F;

        this.leftLeg.xRot = lyingPitch;
        this.leftLeg.yRot = 0.0F;
        this.leftLeg.zRot = 0.0F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (rightLeg.visible) {
            rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }
        if (leftLeg.visible) {
            leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }
}
