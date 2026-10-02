package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.shule.shulespotions.Blocks.Entities.OminousPedestalBE;
import net.shule.shulespotions.Blocks.ModBlockEntities;
import net.shule.shulespotions.Particles.ModParticles;
import org.jetbrains.annotations.Nullable;

public class OminousPedestal extends AncientPedestal {
    protected static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 18.0D, 13.0D);

    public OminousPedestal(Properties pProperties, ResourceLocation lootTable) {
        super(pProperties, lootTable);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new OminousPedestalBE(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null; 
        }
        return createTickerHelper(pBlockEntityType, ModBlockEntities.OMINOUS_PEDESTAL_BE.get(),
                OminousPedestalBE::tick);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof OminousPedestalBE pedestal)) {
            return InteractionResult.PASS;
        }

        if (pedestal.hasItem()) {
            if (pedestal.isActiveEvent()) {
                return InteractionResult.CONSUME;
            } else if (pedestal.isEventCompleted()) {
                ItemStack removed = pedestal.removeItem();
                if (!removed.isEmpty()) {
                    if (!player.addItem(removed)) {
                        player.drop(removed, false);
                    }
                    pedestal.resetEvent(); 
                    return InteractionResult.CONSUME;
                }
            } else {
                ItemStack removed = pedestal.removeItem();
                if (!removed.isEmpty()) {
                    if (!player.addItem(removed)) {
                        player.drop(removed, false);
                    }
                    pedestal.resetEvent();
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof OminousPedestalBE pedestal) {
                pedestal.resetEvent(); 
            }
            super.onRemove(state, level, pos, newState, isMoving); 
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof OminousPedestalBE pedestal && pedestal.hasItem()) {
            
            if (pedestal.isEventCompleted()) {
                for (int i = 0; i < 2; i++) {
                    double angle = random.nextDouble() * Math.PI * 2.0D;
                    double radius = 0.5D;
                    double targetX = pos.getX() + 0.5D + Math.cos(angle) * radius;
                    double targetY = pos.getY() + 1.0D + (random.nextDouble() * 0.5D);
                    double targetZ = pos.getZ() + 0.5D + Math.sin(angle) * radius;

                    level.addParticle(
                            ParticleTypes.ENCHANT,
                            targetX, targetY, targetZ,
                            Math.cos(angle) * 0.5D, 0.5D, Math.sin(angle) * 0.5D
                    );
                }
            } else if (pedestal.isActiveEvent()) {
                int particleCount = 4 - pedestal.getCurrentWave();
                if (particleCount < 1) particleCount = 1;

                for (int i = 0; i < particleCount; i++) {
                    double angle = random.nextDouble() * Math.PI * 2.0D;
                    double radius = 0.4D + (random.nextDouble() * 0.2D);
                    
                    double targetX = pos.getX() + 0.5D + Math.cos(angle) * radius;
                    double targetY = pos.getY() + (random.nextDouble() * 1.2D);
                    double targetZ = pos.getZ() + 0.5D + Math.sin(angle) * radius;

                    level.addParticle(
                            ModParticles.OMINOUS_FIRE.get(),
                            targetX, targetY, targetZ,
                            0.0D, 0.0D, 0.0D
                    );
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    double angle = random.nextDouble() * Math.PI * 2.0D;
                    double radius = 0.4D + (random.nextDouble() * 0.2D);
                    
                    double targetX = pos.getX() + 0.5D + Math.cos(angle) * radius;
                    double targetY = pos.getY() + (random.nextDouble() * 1.2D);
                    double targetZ = pos.getZ() + 0.5D + Math.sin(angle) * radius;

                    level.addParticle(
                            ModParticles.OMINOUS_FIRE.get(),
                            targetX, targetY, targetZ,
                            0.0D, 0.0D, 0.0D
                    );
                }
            }
        }
    }
}
