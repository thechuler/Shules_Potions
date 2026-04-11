package net.shule.shulespotions.Blocks.Renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;
import org.joml.Matrix4f;

public class PotionCauldronRenderer implements BlockEntityRenderer<PotionCauldronBE> {


    public PotionCauldronRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(PotionCauldronBE be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        renderLiquid(be,poseStack,buffer,light);
        renderFloatingIcon(be,partialTicks,poseStack,buffer,light,overlay);

    }





    private void  renderFloatingIcon(PotionCauldronBE be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay){


        poseStack.pushPose();

        //  Centro del bloque + altura
        poseStack.translate(0.5, 1.5, 0.5);

        float time = be.getLevel().getGameTime() + partialTicks;
        ItemStack stack = new ItemStack(Items.BUCKET);
        //  Flotación (sube y baja)
        float bob = (float) Math.sin(time * 0.1f) * 0.05f;
        poseStack.translate(0, bob, 0);

        // 🔄 Rotación
        float angle = time * 2f;
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(angle));

        // 📏 Tamaño
        poseStack.scale(0.4f, 0.4f, 0.4f);

        // 🖼️ TEXTURA (por ahora usamos una del atlas)
        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(ResourceLocation.fromNamespaceAndPath("shulespotions","item/cauldron_actionui1"));

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        VertexConsumer vc = buffer.getBuffer(RenderType.translucent());
        Matrix4f matrix = poseStack.last().pose();

        float size = 0.5f;

// 🔲 Quad vertical (mirando hacia un lado)
        vc.vertex(matrix, -size, -size, 0)
                .color(1f,1f,1f,0.8f)
                .uv(u0, v1)
                .uv2(light)
                .normal(0, 0, 1)
                .endVertex();

        vc.vertex(matrix, -size, size, 0)
                .color(1f,1f,1f,0.8f)
                .uv(u0, v0)
                .uv2(light)
                .normal(0, 0, 1)
                .endVertex();

        vc.vertex(matrix, size, size, 0)
                .color(1f,1f,1f,0.8f)
                .uv(u1, v0)
                .uv2(light)
                .normal(0, 0, 1)
                .endVertex();

        vc.vertex(matrix, size, -size, 0)
                .color(1f,1f,1f,0.8f)
                .uv(u1, v1)
                .uv2(light)
                .normal(0, 0, 1)
                .endVertex();

        // 🔁 Cara trasera (invertida)
        vc.vertex(matrix, size, -size, 0)
                .color(1f,1f,1f,0.8f)
                .uv(u1, v1)
                .uv2(light)
                .normal(0, 0, -1)
                .endVertex();

        vc.vertex(matrix, size, size, 0)
                .color(1f,1f,1f,0.8f)
                .uv(u1, v0)
                .uv2(light)
                .normal(0, 0, -1)
                .endVertex();

        vc.vertex(matrix, -size, size, 0)
                .color(1f,1f,1f,0.8f)
                .uv(u0, v0)
                .uv2(light)
                .normal(0, 0, -1)
                .endVertex();

        vc.vertex(matrix, -size, -size, 0)
                .color(1f,1f,1f,0.8f)
                .uv(u0, v1)
                .uv2(light)
                .normal(0, 0, -1)
                .endVertex();


        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();

// 📍 Lo movemos un poquito hacia adelante (para que no z-fight con el quad)
        poseStack.translate(0, 0, 0.01);

// 📏 Escala del item (probá valores)
        poseStack.scale(0.6f, 0.6f, 0.01f);

// 🔄 Opcional: que rote junto con el icono
// (ya está rotado por el poseStack anterior)

// 🧱 Render del item
        itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.GUI, // o GUI si querés plano
                light,
                overlay,
                poseStack,
                buffer,
                be.getLevel(),
                0
        );




        poseStack.popPose();
        poseStack.popPose();
    }

    private  void renderLiquid(PotionCauldronBE be, PoseStack poseStack, MultiBufferSource buffer, int light){
        if(be.getLiquidLevel() <= 0)
            return;

        int color = be.hasPotionLiquid() ? be.getPotionLiquid().getColor() : 0x8A2BE2;
        float a = 1.0f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

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
                .color(r,g,b,a)
                .uv(u0, v0)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();

        vc.vertex(matrix, min, y, max)
                .color(r,g,b,a)
                .uv(u0, v1)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();

        vc.vertex(matrix, max, y, max)
                .color(r,g,b,a)
                .uv(u1, v1)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();

        vc.vertex(matrix, max, y, min)
                .color(r,g,b,a)
                .uv(u1, v0)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();

        poseStack.popPose();
    }


}