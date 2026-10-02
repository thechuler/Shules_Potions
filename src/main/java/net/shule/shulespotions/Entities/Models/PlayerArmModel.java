package net.shule.shulespotions.Entities.Models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.shule.shulespotions.Entities.entity.PlayerArmEntity;
import net.shule.shulespotions.ShulesPotions;

public class PlayerArmModel<T extends PlayerArmEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID, "player_arm"), "main");

    private final ModelPart rightArm;
    private final ModelPart leftArm;

    public PlayerArmModel(ModelPart root) {
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();


        partdefinition.addOrReplaceChild("right_arm",
                CubeListBuilder.create()
                        .texOffs(40, 16).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(40, 32).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
                PartPose.offset(0.0F, 22.0F, 0.0F)
        );


        partdefinition.addOrReplaceChild("left_arm",
                CubeListBuilder.create()
                        .texOffs(32, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
                PartPose.offset(0.0F, 22.0F, 0.0F)
        );

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean isLeft = entity.isLeftArm();
        this.rightArm.visible = !isLeft;
        this.leftArm.visible = isLeft;


        float lyingPitch = (float) (Math.PI / 2.0);
        this.rightArm.xRot = lyingPitch;
        this.rightArm.yRot = 0.0F;
        this.rightArm.zRot = 0.0F;

        this.leftArm.xRot = lyingPitch;
        this.leftArm.yRot = 0.0F;
        this.leftArm.zRot = 0.0F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (rightArm.visible) {
            rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }
        if (leftArm.visible) {
            leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }
}
