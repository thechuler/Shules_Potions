package net.shule.shulespotions.Blocks.Renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.shule.shulespotions.Blocks.Custom.PotionCauldron;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.Potions.IngredientStat;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.util.CauldronActions.CauldronAction;
import net.shule.shulespotions.util.CauldronActions.AddIngredientAction;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import java.util.ArrayList;
import java.util.List;

public class PotionCauldronRenderer implements BlockEntityRenderer<PotionCauldronBE> {

    private final ItemRenderer itemRenderer;

    public PotionCauldronRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@NotNull PotionCauldronBE be, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int light, int overlay) {
        renderItems(be, partialTicks, poseStack, buffer, light, overlay);
        renderLiquid(be, poseStack, buffer, light);
        

        if (!be.getTank().isEmpty() && be.getState() == PotionCauldronBE.CauldronState.BREWING && be.getTank().getFluid().getFluid() != Fluids.WATER) {
            renderBeams(be, poseStack, buffer);
        }
    }





    private void renderLiquid(PotionCauldronBE be, PoseStack poseStack, MultiBufferSource buffer, int light) {
        if (be.getTank().isEmpty()) return;

        int color;
        if (be.getTank().getFluid().getFluid() == Fluids.WATER) {
            assert be.getLevel() != null;
            color = BiomeColors.getAverageWaterColor(be.getLevel(), be.getBlockPos());
        }else{
            color= be.getRenderColor();
        }

        float a = 1.0f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;




        int emissiveLight = 15728880;

        poseStack.pushPose();

        int amount = be.getTank().getFluidAmount();

        float minY = 0.2f;
        float maxY = 0.9f;
        float min = 0.1f;
        float max = 0.9f;
        float dropOffset = 0.35f;

        if (be.getBlockState().getBlock() instanceof PotionCauldron cauldronBlock) {
            minY = cauldronBlock.getLiquidMinY();
            maxY = cauldronBlock.getLiquidMaxY();
            dropOffset = cauldronBlock.getLiquidDropOffset();
            min = cauldronBlock.getLiquidWidthMin();
            max = cauldronBlock.getLiquidWidthMax();
        }

        float fill = amount / (float) be.getTank().getCapacity();
        float maxLevelForFluid = minY + (maxY - minY) * fill;

        int maxIngredients = be.getMaxIngredients();
        float itemFill = maxIngredients > 0 ? (float) be.getActions().size() / maxIngredients : 0f;

        float offset = dropOffset * (1.0f - itemFill);
        float y = Math.max(minY, maxLevelForFluid - offset);


        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ResourceLocation.parse("minecraft:block/water_still"));

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();


        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucentCull(net.minecraft.world.inventory.InventoryMenu.BLOCK_ATLAS));

        Matrix4f matrix = poseStack.last().pose();

        vc.vertex(matrix, min, y, min).color(r, g, b, a).uv(u0, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(emissiveLight).normal(0, 1, 0).endVertex();

        vc.vertex(matrix, min, y, max).color(r, g, b, a).uv(u0, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(emissiveLight).normal(0, 1, 0).endVertex();

        vc.vertex(matrix, max, y, max).color(r, g, b, a).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(emissiveLight).normal(0, 1, 0).endVertex();

        vc.vertex(matrix, max, y, min).color(r, g, b, a).uv(u1, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(emissiveLight).normal(0, 1, 0).endVertex();

        poseStack.popPose();
    }

    private void renderItems(PotionCauldronBE be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if (be.getActions().isEmpty()) return;
        if(be.getState() != PotionCauldronBE.CauldronState.BREWING) return;
        List<ItemStack> itemsToRender = new ArrayList<>();
        for (CauldronAction action : be.getActions()) {
            if (action instanceof AddIngredientAction addAction) {
                itemsToRender.add(new ItemStack(addAction.getItem()));
            }
        }

        if (itemsToRender.isEmpty()) return;

        long time = be.getLevel() != null ? be.getLevel().getGameTime() : 0;
        float timeF = time + partialTicks;

        float minY = 0.2f;
        float maxY = 0.9f;
        float radius = 0.25f;
        float dropOffset = 0.35f;

        if (be.getBlockState().getBlock() instanceof PotionCauldron cauldronBlock) {
            minY = cauldronBlock.getLiquidMinY();
            maxY = cauldronBlock.getLiquidMaxY();
            dropOffset = cauldronBlock.getLiquidDropOffset();
            radius = cauldronBlock.getItemRotationRadius();
        }

        float fill = be.getTank().getFluidAmount() / (float) be.getTank().getCapacity();
        float maxLevelForFluid = minY + (maxY - minY) * fill;
        int maxIngredients = be.getMaxIngredients();
        float itemFill = maxIngredients > 0 ? (float) be.getActions().size() / maxIngredients : 0f;
        float offset = dropOffset * (1.0f - itemFill);
        float liquidY = Math.max(minY, maxLevelForFluid - offset);

        int count = itemsToRender.size();
        for (int i = 0; i < count; i++) {
            ItemStack stack = itemsToRender.get(i);
            
            float angle = (i * ((float)Math.PI * 2f) / count) + (timeF * 0.03f);
            
            float x = 0.5f + (float)Math.cos(angle) * radius;
            float z = 0.5f + (float)Math.sin(angle) * radius;

            float y = liquidY - 0.01f + (float)Math.sin(timeF * 0.05f + i) * 0.015f;

            poseStack.pushPose();
            poseStack.translate(x, y, z);
            
            float scale = 0.65f;
            poseStack.scale(scale, scale, scale);
            poseStack.mulPose(Axis.YP.rotationDegrees(timeF * 3f + i * 45f));
            poseStack.mulPose(Axis.XP.rotationDegrees(75f));

            int emissiveLight = 15728880;
            itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, emissiveLight, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, poseStack, buffer, be.getLevel(), i);
            
            poseStack.popPose();
        }
    }

    private void renderBeams(PotionCauldronBE be,PoseStack poseStack,MultiBufferSource buffer) {
        int color = be.getRenderColor();
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        float minY = 0.2f;
        float maxY = 0.9f;
        float w = 0.35f;
        float dropOffset = 0.35f;
        float length = 4.5f;

        if (be.getBlockState().getBlock() instanceof PotionCauldron cauldronBlock) {
            minY = cauldronBlock.getLiquidMinY();
            maxY = cauldronBlock.getLiquidMaxY();
            dropOffset = cauldronBlock.getLiquidDropOffset();
            w = cauldronBlock.getBeamWidth();
            length = cauldronBlock.getBeamLength();
        }

        float fill = be.getTank().getFluidAmount() / (float) be.getTank().getCapacity();
        float maxLevelForFluid = minY + (maxY - minY) * fill;
        int maxIngredients = be.getMaxIngredients();
        float itemFill = maxIngredients > 0 ? (float) be.getActions().size() / maxIngredients : 0f;
        float offset = dropOffset * (1.0f - itemFill);
        float liquidY = Math.max(minY, maxLevelForFluid - offset);

        poseStack.pushPose();
        
        poseStack.translate(0.5D, liquidY, 0.5D);

        VertexConsumer vc = buffer.getBuffer(RenderType.beaconBeam(ResourceLocation.parse("minecraft:textures/entity/beacon_beam.png"), true));
        
        org.joml.Matrix4f matrix = poseStack.last().pose();

        float alphaBase = 0.5f;
        float alphaTip = 0.0f;

        int emissiveLight = 15728880;
        int overlay = OverlayTexture.NO_OVERLAY;


        vc.vertex(matrix, -w, 0.0F, -w).color(r, g, b, alphaBase).uv(0, 1).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();
        vc.vertex(matrix, w, 0.0F, -w).color(r, g, b, alphaBase).uv(1, 1).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();
        vc.vertex(matrix, w, length, -w).color(r, g, b, alphaTip).uv(1, 0).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();
        vc.vertex(matrix, -w, length, -w).color(r, g, b, alphaTip).uv(0, 0).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();


        vc.vertex(matrix, w, 0.0F, w).color(r, g, b, alphaBase).uv(0, 1).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();
        vc.vertex(matrix, -w, 0.0F, w).color(r, g, b, alphaBase).uv(1, 1).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();
        vc.vertex(matrix, -w, length, w).color(r, g, b, alphaTip).uv(1, 0).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();
        vc.vertex(matrix, w, length, w).color(r, g, b, alphaTip).uv(0, 0).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();


        vc.vertex(matrix, w, 0.0F, -w).color(r, g, b, alphaBase).uv(0, 1).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();
        vc.vertex(matrix, w, 0.0F, w).color(r, g, b, alphaBase).uv(1, 1).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();
        vc.vertex(matrix, w, length, w).color(r, g, b, alphaTip).uv(1, 0).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();
        vc.vertex(matrix, w, length, -w).color(r, g, b, alphaTip).uv(0, 0).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();


        vc.vertex(matrix, -w, 0.0F, w).color(r, g, b, alphaBase).uv(0, 1).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();
        vc.vertex(matrix, -w, 0.0F, -w).color(r, g, b, alphaBase).uv(1, 1).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();
        vc.vertex(matrix, -w, length, -w).color(r, g, b, alphaTip).uv(1, 0).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();
        vc.vertex(matrix, -w, length, w).color(r, g, b, alphaTip).uv(0, 0).overlayCoords(overlay).uv2(emissiveLight).normal(0, 1, 0).endVertex();

        poseStack.popPose();
    }


}



