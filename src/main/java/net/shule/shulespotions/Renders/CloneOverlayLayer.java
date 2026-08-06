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

public class CloneOverlayLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    "shulespotions",
                    "textures/misc/cloned_overlay.png"
            );
            
    public CloneOverlayLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {


        // Solo aplicamos el overlay si la entidad tiene la etiqueta de ser un clon
        if (!entity.getPersistentData().getBoolean("shulespotions:is_clone"))
            return;

        // Color neutro (1.0) para que respete el verde original de tu PNG
        float r = 1.0f;
        float g = 1.0f;
        float b = 1.0f;
        float alpha = 1.0f; // Respetamos el alfa original de tu imagen

        // IMPORTANTE: RenderType.eyes() ignora la oscuridad y hace que brille (emissive)
        VertexConsumer vc = buffer.getBuffer(RenderType.eyes(TEXTURE));

        this.getParentModel().renderToBuffer(
                poseStack,
                vc,
                15728880, // Forzar nivel de luz máxima (block light 15, sky light 15) para que brille
                OverlayTexture.NO_OVERLAY,
                r,
                g,
                b,
                alpha
        );
    }
}
