package net.shule.shulespotions.Renders;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.world.entity.LivingEntity;

public class BodyBreakDownCustomHeadLayer<T extends LivingEntity, M extends EntityModel<T> & HeadedModel>
        extends CustomHeadLayer<T, M> {

    public BodyBreakDownCustomHeadLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet, ItemInHandRenderer itemInHandRenderer) {
        super(renderer, modelSet, itemInHandRenderer);
    }

    public BodyBreakDownCustomHeadLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet, float scaleX, float scaleY, float scaleZ, ItemInHandRenderer itemInHandRenderer) {
        super(renderer, modelSet, scaleX, scaleY, scaleZ, itemInHandRenderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity != null && entity.getPersistentData().contains("shulespotions:missing_limbs")) {
            byte mask = entity.getPersistentData().getByte("shulespotions:missing_limbs");
            if ((mask & 1) != 0) {
                return;
            }
        }
        super.render(poseStack, buffer, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }
}
