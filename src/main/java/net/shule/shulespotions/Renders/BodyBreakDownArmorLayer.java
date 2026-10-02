package net.shule.shulespotions.Renders;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BodyBreakDownArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>>
        extends HumanoidArmorLayer<T, M, A> {

    private T currentEntity;

    public BodyBreakDownArmorLayer(RenderLayerParent<T, M> renderer, A innerModel, A outerModel, ModelManager modelManager) {
        super(renderer, innerModel, outerModel, modelManager);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.currentEntity = entity;
        super.render(poseStack, buffer, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        this.currentEntity = null;
    }

    @Override
    protected void setPartVisibility(A model, EquipmentSlot slot) {
        super.setPartVisibility(model, slot);
        applyMissingLimbsVisibility(model, this.currentEntity);
    }

    @Override
    protected Model getArmorModelHook(T entity, ItemStack itemStack, EquipmentSlot slot, A model) {
        Model armorModel = super.getArmorModelHook(entity, itemStack, slot, model);
        if (armorModel instanceof HumanoidModel<?> humanoidArmor) {
            applyMissingLimbsVisibility(humanoidArmor, entity);
        }
        return armorModel;
    }

    public static void applyMissingLimbsVisibility(HumanoidModel<?> model, LivingEntity entity) {
        if (entity != null && entity.getPersistentData().contains("shulespotions:missing_limbs")) {
            byte mask = entity.getPersistentData().getByte("shulespotions:missing_limbs");
            if ((mask & 1) != 0) {
                model.head.visible = false;
                model.hat.visible = false;
            }
            if ((mask & 2) != 0) {
                model.rightArm.visible = false;
            }
            if ((mask & 4) != 0) {
                model.leftArm.visible = false;
            }
            if ((mask & 8) != 0) {
                model.rightLeg.visible = false;
            }
            if ((mask & 16) != 0) {
                model.leftLeg.visible = false;
            }
        }
    }
}
