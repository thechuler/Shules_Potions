package net.shule.shulespotions.Blocks.Renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Blocks.Custom.SpoonRack;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;
import net.shule.shulespotions.Blocks.Entities.SpoonRackBE;

public class SpoonRackRenderer implements BlockEntityRenderer<SpoonRackBE> {

    private final ItemRenderer itemRenderer;

    public SpoonRackRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(
            SpoonRackBE be,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay
    ) {

        Direction facing = be.getBlockState().getValue(SpoonRack.FACING);

        double wallOffset = 0.25;

        double centerX = 0.5 - facing.getStepX() * wallOffset;
        double centerZ = 0.5 - facing.getStepZ() * wallOffset;

        float rotation = switch (facing) {
            case SOUTH -> 180f;
            case WEST  -> 90f;
            case NORTH -> 0f;
            case EAST  -> 270f;
            default -> 0f;
        };

        // Dirección lateral al rack
        Direction side = facing.getClockWise();

        for (int slot = 0; slot < 3; slot++) {

            ItemStack spoon = be.getSpoon(slot);

            if (spoon.isEmpty())
                continue;

            float spacing = 0.28f;

            float localOffset = switch (slot) {
                case 0 -> -spacing;
                case 1 -> 0f;
                case 2 -> spacing;
                default -> 0f;
            };

            poseStack.pushPose();

            poseStack.translate(
                    centerX + side.getStepX() * localOffset,
                    0.75,
                    centerZ + side.getStepZ() * localOffset
            );

            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

            poseStack.scale(0.7f, 0.7f, 0.7f);

            itemRenderer.renderStatic(
                    spoon,
                    ItemDisplayContext.FIXED,
                    packedLight,
                    packedOverlay,
                    poseStack,
                    buffer,
                    be.getLevel(),
                    slot
            );

            poseStack.popPose();
        }
    }
}