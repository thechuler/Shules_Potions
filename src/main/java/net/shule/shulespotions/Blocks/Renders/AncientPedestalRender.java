package net.shule.shulespotions.Blocks.Renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Blocks.Entities.AncientPedestalBE;
import org.joml.Random;

public class AncientPedestalRender<T extends AncientPedestalBE> implements BlockEntityRenderer<T> {

    private final ItemRenderer itemRenderer;
    private final double customHeight;

    public AncientPedestalRender(BlockEntityRendererProvider.Context context) {
        this(context, -1.0D);
    }

    public AncientPedestalRender(BlockEntityRendererProvider.Context context, double customHeight) {
        this.itemRenderer = context.getItemRenderer();
        this.customHeight = customHeight;
    }

    @Override
    public void render(T pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        renderItem(pBlockEntity, pPartialTick, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
    }

    private void renderItem(T be, float partialTicks, PoseStack poseStack,
                            MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemStack itemsToRender = be.getItem();
        if (itemsToRender.isEmpty()) return;

        long time = be.getLevel() != null ? be.getLevel().getGameTime() : 0;
        float timeF = time + partialTicks;

        float yOffset = (float) Math.sin(timeF * 0.05f) * 0.05f;
        double baseHeight = customHeight >= 0 ? customHeight : be.getItemRenderYOffset();

        poseStack.pushPose();
        poseStack.translate(0.5D, baseHeight + yOffset, 0.5D);

        float scale = 0.7f;
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(timeF * 3.0f));

        int count = itemsToRender.getCount();
        int renderCount = 1;
        if (count > 48) {
            renderCount = 5;
        } else if (count > 32) {
            renderCount = 4;
        } else if (count > 16) {
            renderCount = 3;
        } else if (count > 1) {
            renderCount = 2;
        }

       Random random = new Random(be.getBlockPos().asLong());

        for (int i = 0; i < renderCount; i++) {
            poseStack.pushPose();
            if (i > 0) {
                float dx = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                float dy = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                float dz = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                poseStack.translate(dx, dy, dz);
            }

            itemRenderer.renderStatic(
                    itemsToRender,
                    ItemDisplayContext.GROUND,
                    packedLight,
                    packedOverlay,
                    poseStack,
                    buffer,
                    be.getLevel(),
                    (int) be.getBlockPos().asLong() + i
            );
            poseStack.popPose();
        }

        poseStack.popPose();
    }
}