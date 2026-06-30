package net.shule.shulespotions.Blocks.Renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Blocks.Entities.MortarBE;
import net.shule.shulespotions.util.ColorUtils;
import org.joml.Matrix4f;


public class MortarRender implements BlockEntityRenderer<MortarBE> {


    private static final ResourceLocation MORTAR_DUST =
            ResourceLocation.fromNamespaceAndPath(
                    "shulespotions",
                    "textures/misc/mortar_dust.png");
    public MortarRender(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(MortarBE be, float partialTick, PoseStack pose,
                       MultiBufferSource buffer, int light, int overlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();


        if (be.isFinished()) {

            pose.pushPose();

            pose.translate(0.5, 0.3, 0.5);
            pose.scale(0.8F, 0.8F, 0.8F);


            float[]color = ColorUtils.intToRGB(be.getPowderColor());

            float r = color[0];
            float g = color[1];
            float b = color[2];
            float a = 1.0f;


            VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(MORTAR_DUST));

            Matrix4f poseMatrix = pose.last().pose();

            float size = 0.65f;
            float half = size / 2.0f;

            float min = -half;
            float max = half;
            float y = 0.0f;

            consumer.vertex(poseMatrix, min, y, min)
                    .color(r, g, b, a)
                    .uv(0.0F, 0.0F)
                    .overlayCoords(overlay)
                    .uv2(light)
                    .normal(pose.last().normal(), 0.0F, 1.0F, 0.0F)
                    .endVertex();

            consumer.vertex(poseMatrix, min, y, max)
                    .color(r, g, b, a)
                    .uv(0.0F, 1.0F)
                    .overlayCoords(overlay)
                    .uv2(light)
                    .normal(pose.last().normal(), 0.0F, 1.0F, 0.0F)
                    .endVertex();

            consumer.vertex(poseMatrix, max, y, max)
                    .color(r, g, b, a)
                    .uv(1.0F, 1.0F)
                    .overlayCoords(overlay)
                    .uv2(light)
                    .normal(pose.last().normal(), 0.0F, 1.0F, 0.0F)
                    .endVertex();

            consumer.vertex(poseMatrix, max, y, min)
                    .color(r, g, b, a)
                    .uv(1.0F, 0.0F)
                    .overlayCoords(overlay)
                    .uv2(light)
                    .normal(pose.last().normal(), 0.0F, 1.0F, 0.0F)
                    .endVertex();

            pose.popPose();
            return;
        }


        for (int i = 0; i < 3; i++) {

            ItemStack stack = be.getIngredients().get(i);

            if (stack.isEmpty())
                continue;

            pose.pushPose();

            pose.translate(0.5, 0.2, 0.4);

            switch (i) {
                case 0 -> pose.translate(-0.10, 0.00, 0.1);
                case 1 -> pose.translate( 0.00, 0.04, 0.11);
                case 2 -> pose.translate( 0.10, 0.00, 0.1);
            }

            pose.mulPose(Axis.XP.rotationDegrees(90F));
            pose.scale(0.4F, 0.4F, 0.4F);

            itemRenderer.renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    light,
                    overlay,
                    pose,
                    buffer,
                    be.getLevel(),
                    0
            );

            pose.popPose();
        }
    }
}
