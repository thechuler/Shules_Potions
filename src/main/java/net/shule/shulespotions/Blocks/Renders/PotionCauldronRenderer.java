package net.shule.shulespotions.Blocks.Renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;
import org.joml.Matrix4f;

public class PotionCauldronRenderer implements BlockEntityRenderer<PotionCauldronBE> {


    public PotionCauldronRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(PotionCauldronBE be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {

        if(be.getLiquid_Level() <= 0)
            return;

        poseStack.pushPose();

        //Altura del líquido
        float y = 0.9f;

        // Tamaño del quad
        float min = 0.1f;
        float max = 0.9f;

        //Obtener sprite del atlas
        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(ResourceLocation.parse("minecraft:block/water_still"));

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        // RenderType correcto para bloques
        VertexConsumer vc = buffer.getBuffer(RenderType.translucent());

        Matrix4f matrix = poseStack.last().pose();

        // 🔲 QUAD (orden importante)
        vc.vertex(matrix, min, y, min)
                .color(242f/255f, 66f/255f, 68f/255f, 1f)
                .uv(u0, v0)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();

        vc.vertex(matrix, min, y, max)
                .color(242f/255f, 66f/255f, 68f/255f, 1f)
                .uv(u0, v1)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();

        vc.vertex(matrix, max, y, max)
                .color(242f/255f, 66f/255f, 68f/255f, 1f)
                .uv(u1, v1)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();

        vc.vertex(matrix, max, y, min)
                .color(242f/255f, 66f/255f, 68f/255f, 1f)
                .uv(u1, v0)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();

        poseStack.popPose();
    }
}