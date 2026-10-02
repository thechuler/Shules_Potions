package net.shule.shulespotions.Blocks.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.shule.shulespotions.Blocks.ModBlocks;
import org.jetbrains.annotations.Nullable;

public class BigCauldronCore extends PotionCauldron {

    public BigCauldronCore(Properties properties, int maxIngredientCount, int maxEffectCount, int maxLiquidLevel, int cauldronLevel) {
        super(properties, maxIngredientCount, maxEffectCount, maxLiquidLevel, cauldronLevel);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos center = context.getClickedPos();

        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;

                    BlockPos partPos = center.offset(x, y, z);
                    if (!level.getBlockState(partPos).canBeReplaced(context)) {
                        return null;
                    }
                }
            }
        }
        return super.getStateForPlacement(context);
    }


    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;

                    BlockPos partPos = pos.offset(x, y, z);
                    BlockState partState = ModBlocks.BIG_CAULDRON_PART.get().defaultBlockState()
                            .setValue(BigCauldronPart.OFFSET_X, x + 1)
                            .setValue(BigCauldronPart.OFFSET_Y, y)
                            .setValue(BigCauldronPart.OFFSET_Z, z + 1);

                    level.setBlock(partPos, partState, 3);
                }
            }
        }
    }


    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            for (int x = -1; x <= 1; x++) {
                for (int y = 0; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;

                        BlockPos partPos = pos.offset(x, y, z);
                        if (level.getBlockState(partPos).is(ModBlocks.BIG_CAULDRON_PART.get())) {
                            level.setBlock(partPos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }
                }
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }


    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }


    @Override
    public float getLiquidMinY() { return 1.0f; }

    @Override
    public float getLiquidMaxY() { return 1.5f; }

    @Override
    public float getLiquidDropOffset() { return 0.5f; }

    @Override
    public float getLiquidWidthMin() { return -0.8f; }

    @Override
    public float getLiquidWidthMax() { return 1.8f; }

    @Override
    public float getItemRotationRadius() { return 0.8f; }

    @Override
    public float getBeamWidth() { return 0.8f; }

    @Override
    public float getBeamLength() { return 16.0f; }

    @Override
    public AABB getItemCaptureAABB(BlockPos pos) {
        return new AABB(
            pos.getX() - 0.8, pos.getY() + 1.0, pos.getZ() - 0.8,
            pos.getX() + 1.8, pos.getY() + 2.5, pos.getZ() + 1.8
        );
    }


    @Override
    public float getBubbleParticleChance() { return 0.8f; }
    
    @Override
    public float getBubbleParticleSpread() { return 1.8f; }
    
    @Override
    public float getBubbleParticleY() { return 1.5f; }

    @Override
    public int getExplosionParticleCount() { return 600; }

    @Override
    public double getExplosionParticleRadius() { return 4.5; }

    @Override
    public double getExplosionParticleHeightSpread() { return 3.5; }

    @Override
    public double getExplosionParticleY() { return 1.5; }
}
