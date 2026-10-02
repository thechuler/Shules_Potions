package net.shule.shulespotions.Blocks.Renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.shule.shulespotions.Blocks.Custom.SpitterTrap;
import net.shule.shulespotions.Blocks.Entities.SpitterTrapBE;
import net.shule.shulespotions.ShulesPotions;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import net.minecraft.client.renderer.texture.OverlayTexture;

public class SpitterTrapRenderer implements BlockEntityRenderer<SpitterTrapBE> {

    public static final ResourceLocation TRIGGERED_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ShulesPotions.MODID, "textures/block/spitter_trap_front_triggered.png");
    private static final int TOTAL_FRAMES = 11;
    private static final int TOTAL_ANIMATION_TICKS = 20;

    public SpitterTrapRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull SpitterTrapBE be, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        BlockState state = be.getBlockState();
        if (!state.hasProperty(SpitterTrap.FACING)) {
            return;
        }

        int timer = be.getAnimationTimer();
        boolean isSpitting = state.hasProperty(SpitterTrap.SPITTING) && state.getValue(SpitterTrap.SPITTING);

        // Si el temporizador terminó y ya no está en estado SPITTING, no renderizar nada
        if (timer <= 0 && !isSpitting) {
            return;
        }

        // Progreso de 0.0 (inicio) a 1.0 (final)
        float remainingTicks = Math.max(0.0F, (float) timer - partialTicks);
        float progress = timer > 0 ? (1.0F - (remainingTicks / (float) TOTAL_ANIMATION_TICKS)) : 1.0F;
        progress = Mth.clamp(progress, 0.0F, 1.0F);

        int frame = getFrameIndex(progress);
        Direction facing = state.getValue(SpitterTrap.FACING);

        // Obtener la iluminación del bloque de aire frente a la cara (no la del interior del bloque sólido)
        int faceLight = be.getLevel() != null 
                ? net.minecraft.client.renderer.LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().relative(facing))
                : packedLight;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);

        float rotationY = switch (facing) {
            case NORTH -> 180.0F;
            case SOUTH -> 0.0F;
            case EAST  -> 90.0F;
            case WEST  -> 270.0F;
            default    -> 0.0F;
        };
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationY));

        renderAnimatedFace(poseStack, buffer, frame, faceLight);

        poseStack.popPose();
    }

    /**
     * Mapeo de frames limpio:
     * - Inicia en Frame 0 (Cerrada)
     * - Pasa por los frames de apertura (1..4)
     * - Mantiene los frames de disparo / boca abierta (5 y 6) durante la ráfaga
     * - Pasa por los frames de cierre (7..10)
     * - Termina antes del tick final en Frame 0 (Cerrada) para una transición 100% invisible
     */
    private int getFrameIndex(float progress) {
        if (progress < 0.08F) return 0;  // Cerrada
        if (progress < 0.16F) return 1;  // Abriendo
        if (progress < 0.24F) return 2;  // Abriendo
        if (progress < 0.32F) return 3;  // Abriendo
        if (progress < 0.42F) return 4;  // Abierta
        if (progress < 0.62F) return 5;  // Totalmente abierta (Disparo)
        if (progress < 0.72F) return 7;  // Cerrando
        if (progress < 0.82F) return 9;  // Casi cerrada
        if (progress < 0.90F) return 10; // Casi cerrada
        return 0; // Boca cerrada antes de que el renderer termine
    }

    private void renderAnimatedFace(PoseStack poseStack, MultiBufferSource buffer, int frame, int light) {
        // Offset de 0.002 bloques (0.502F) hacia afuera para evitar z-fighting / sombras de contacto
        float z = 0.502F;
        float minX = -0.5F;
        float maxX = 0.5F;
        float minY = -0.5F;
        float maxY = 0.5F;

        float u0 = 0.0F;
        float u1 = 1.0F;
        float v0 = (float) frame / (float) TOTAL_FRAMES;
        float v1 = (float) (frame + 1) / (float) TOTAL_FRAMES;

        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normalMatrix = poseStack.last().normal();
        VertexConsumer vc = buffer.getBuffer(RenderType.entityCutout(TRIGGERED_TEXTURE));

        // NO_OVERLAY previene tintes rojos o blancos del pipeline de entidades
        int overlay = OverlayTexture.NO_OVERLAY;

        // Vértices en sentido antihorario frente al eje +Z
        vc.vertex(matrix, minX, maxY, z).color(255, 255, 255, 255).uv(u0, v0).overlayCoords(overlay).uv2(light).normal(normalMatrix, 0.0F, 0.0F, 1.0F).endVertex();
        vc.vertex(matrix, minX, minY, z).color(255, 255, 255, 255).uv(u0, v1).overlayCoords(overlay).uv2(light).normal(normalMatrix, 0.0F, 0.0F, 1.0F).endVertex();
        vc.vertex(matrix, maxX, minY, z).color(255, 255, 255, 255).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(normalMatrix, 0.0F, 0.0F, 1.0F).endVertex();
        vc.vertex(matrix, maxX, maxY, z).color(255, 255, 255, 255).uv(u1, v0).overlayCoords(overlay).uv2(light).normal(normalMatrix, 0.0F, 0.0F, 1.0F).endVertex();
    }
}
