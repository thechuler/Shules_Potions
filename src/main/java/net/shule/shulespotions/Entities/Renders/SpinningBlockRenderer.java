package net.shule.shulespotions.Entities.Renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.shule.shulespotions.Entities.entity.SpinningBlockEntity;

public class SpinningBlockRenderer extends EntityRenderer<SpinningBlockEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public SpinningBlockRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.shadowRadius = 0.5F;
        this.blockRenderer = pContext.getBlockRenderDispatcher();
    }

    @Override
    public void render(SpinningBlockEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        BlockState blockstate = pEntity.getBlockState();
        if (blockstate.getRenderShape() == RenderShape.MODEL) {
            pPoseStack.pushPose();
            

            pPoseStack.translate(0.0D, 0.5D, 0.0D);


            float time = (float) pEntity.tickCount + pPartialTicks;
            

            RandomSource random = RandomSource.create(pEntity.getId());
            float speedX = (random.nextFloat() * 15f) + 5f;
            float speedY = (random.nextFloat() * 15f) + 5f;
            float speedZ = (random.nextFloat() * 15f) + 5f;
            
            pPoseStack.mulPose(Axis.XP.rotationDegrees(time * speedX));
            pPoseStack.mulPose(Axis.YP.rotationDegrees(time * speedY));
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(time * speedZ));


            pPoseStack.translate(-0.5D, -0.5D, -0.5D);

            this.blockRenderer.renderSingleBlock(blockstate, pPoseStack, pBuffer, pPackedLight, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY);
            
            pPoseStack.popPose();
            super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(SpinningBlockEntity pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
