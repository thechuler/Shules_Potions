package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.shule.shulespotions.Blocks.Entities.ElectionAncientPedestalBE;
import net.shule.shulespotions.Particles.ModParticles;
import org.jetbrains.annotations.Nullable;

public class ElectionAncientPedestal extends AncientPedestal {
    protected static final VoxelShape ELECTION_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 18.0D, 13.0D);

    public ElectionAncientPedestal(Properties pProperties) {
        super(pProperties, null);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return ELECTION_SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ElectionAncientPedestalBE(pPos, pState);
    }

    @Override
    public net.minecraft.world.InteractionResult use(BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand, net.minecraft.world.phys.BlockHitResult hit) {
        if (hand != net.minecraft.world.InteractionHand.MAIN_HAND) {
            return net.minecraft.world.InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return net.minecraft.world.InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof net.shule.shulespotions.Blocks.Entities.ElectionAncientPedestalBE pedestal)) {
            return net.minecraft.world.InteractionResult.PASS;
        }

        if (pedestal.hasItem()) {
            net.minecraft.world.item.ItemStack removed = pedestal.removeItem();
            if (!removed.isEmpty()) {
                if (!player.addItem(removed)) {
                    player.drop(removed, false);
                }
                return net.minecraft.world.InteractionResult.CONSUME;
            }
        } else if (player.isCreative() && !player.getItemInHand(hand).isEmpty()) {
            net.minecraft.world.item.ItemStack handItem = player.getItemInHand(hand).copy();
            handItem.setCount(1);
            if (pedestal.setItem(handItem)) {
                return net.minecraft.world.InteractionResult.CONSUME;
            }
        }

        return net.minecraft.world.InteractionResult.PASS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof ElectionAncientPedestalBE pedestal && pedestal.hasItem()) {
            if (random.nextFloat() < 0.85F) {
                double targetX = pos.getX() + 0.5D;
                double targetY = pos.getY() + 1D;
                double targetZ = pos.getZ() + 0.5D;

                double radius = 0.45D + (random.nextDouble() * 0.15D);
                double startAngle = random.nextDouble() * Math.PI * 2.0D;
                double speed = 0.06D + (random.nextDouble() * 0.03D);

                level.addParticle(
                    ModParticles.ANCIENT_PARTICLE.get(),
                    targetX,
                    targetY,
                    targetZ,
                    radius,
                    startAngle,
                    speed
                );
            }
        }
    }
}