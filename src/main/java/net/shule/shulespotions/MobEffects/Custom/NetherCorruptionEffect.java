package net.shule.shulespotions.MobEffects.Custom;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.shule.shulespotions.util.NetherConversionRule;
import net.shule.shulespotions.Particles.ModParticles;

import java.util.List;
import java.util.Map;

public class NetherCorruptionEffect extends MobEffect {

    public static final List<NetherConversionRule> CONVERSIONS = List.of(
            new NetherConversionRule(BlockTags.LOGS, Blocks.CRIMSON_STEM),
            new NetherConversionRule(BlockTags.LEAVES, Blocks.NETHER_WART_BLOCK),
            new NetherConversionRule(BlockTags.DIRT, Blocks.CRIMSON_NYLIUM),
            new NetherConversionRule(BlockTags.BASE_STONE_OVERWORLD, Blocks.NETHERRACK),
            new NetherConversionRule(Blocks.GRAVEL, Blocks.MAGMA_BLOCK),
            new NetherConversionRule(Blocks.GRASS, Blocks.CRIMSON_ROOTS),
            new NetherConversionRule(Blocks.WATER, Blocks.MAGMA_BLOCK),
            new NetherConversionRule(BlockTags.SAND, Blocks.SOUL_SAND),
            new NetherConversionRule(BlockTags.STONE_ORE_REPLACEABLES, Blocks.NETHER_QUARTZ_ORE),
            new NetherConversionRule(BlockTags.FLOWERS, Blocks.CRIMSON_FUNGUS),
            new NetherConversionRule(Blocks.STONE, Blocks.BLACKSTONE),
            new NetherConversionRule(Blocks.COBBLESTONE, Blocks.BLACKSTONE),
            new NetherConversionRule(Blocks.CLAY, Blocks.SOUL_SOIL),
            new NetherConversionRule(BlockTags.PLANKS, Blocks.CRIMSON_PLANKS),
            new NetherConversionRule(Blocks.BRICKS, Blocks.NETHER_BRICKS),
            new NetherConversionRule(BlockTags.WOODEN_FENCES, Blocks.NETHER_BRICK_FENCE),
            new NetherConversionRule(Blocks.VINE, Blocks.WEEPING_VINES),
            new NetherConversionRule(Blocks.SUGAR_CANE, Blocks.BONE_BLOCK),
            new NetherConversionRule(Blocks.ICE, Blocks.MAGMA_BLOCK),
            new NetherConversionRule(BlockTags.WOODEN_STAIRS, Blocks.CRIMSON_STAIRS),
            new NetherConversionRule(BlockTags.STAIRS, Blocks.NETHER_BRICK_STAIRS)
            );



    public NetherCorruptionEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }


    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);

        if (!(pLivingEntity.level() instanceof ServerLevel level))
            return;
        if (level.dimension() == Level.NETHER)
            return;

        convertNearbyBlocks(level, pLivingEntity.blockPosition(), pAmplifier);
    }

    private void convertNearbyBlocks(ServerLevel level, BlockPos center, int amplifier) {
        int radius = amplifier + 2; 
        int attempts = 60 + (amplifier * 60);

        for (int i = 0; i < attempts; i++) {
            int dx = level.random.nextInt(radius * 2 + 1) - radius;
            int dy = level.random.nextInt(radius * 2 + 1) - radius;
            int dz = level.random.nextInt(radius * 2 + 1) - radius;
            
            BlockPos pos = center.offset(dx, dy, dz);
            

            if (pos.distSqr(center) > radius * radius) continue;

            BlockState state = level.getBlockState(pos);
            Block replacement = getReplacement(state);

            if (replacement != null && replacement != state.getBlock()) {
                
                if (pos.distSqr(center) <= 4.0 || isTouchingNetherBlock(level, pos)) {
                    level.setBlock(pos, replacement.defaultBlockState(), 2);
                    level.sendParticles(ModParticles.CORRUPTION_FIRE.get(), pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 1, 0.2, 0.2, 0.2, 0.0);
                }
            }
        }
    }

    private boolean isTouchingNetherBlock(ServerLevel level, BlockPos pos) {
        BlockPos[] neighbors = new BlockPos[]{
            pos.above(), pos.below(), pos.north(), pos.south(), pos.east(), pos.west()
        };
        for (BlockPos neighbor : neighbors) {
            BlockState adjState = level.getBlockState(neighbor);
            if (isNetherBlock(adjState.getBlock())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isNetherBlock(Block block) {
        for (NetherConversionRule rule : CONVERSIONS) {
            if (block == rule.getReplacement()) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 5 == 0; // cada 5 ticks
    }


    private Block getReplacement(BlockState state) {

        for (NetherConversionRule rule : CONVERSIONS) {
            if (rule.matches(state)) {
                return rule.getReplacement();
            }
        }

        return null;
    }
}
