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

import java.util.List;
import java.util.Map;

public class NetherCorruptionEffect extends MobEffect {

    public static final List<NetherConversionRule> CONVERSIONS = List.of(
            new NetherConversionRule(BlockTags.LOGS, Blocks.CRIMSON_STEM),
            new NetherConversionRule(BlockTags.LEAVES, Blocks.NETHER_WART_BLOCK),
            new NetherConversionRule(BlockTags.DIRT, Blocks.CRIMSON_NYLIUM),
            new NetherConversionRule(BlockTags.BASE_STONE_OVERWORLD, Blocks.NETHERRACK),
            new NetherConversionRule(Blocks.GRAVEL, Blocks.MAGMA_BLOCK),
            new NetherConversionRule( Blocks.GRASS, Blocks.CRIMSON_ROOTS),
            new NetherConversionRule(Blocks.WATER, Blocks.LAVA),
            new NetherConversionRule(BlockTags.STONE_ORE_REPLACEABLES,Blocks.NETHER_QUARTZ_ORE)
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
        int radius = amplifier + 1;
        int height = 8;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -1; y <= height; y++) {
                for (int z = -radius; z <= radius; z++) {

                    BlockPos pos = center.offset(x, y, z);

                    BlockState state = level.getBlockState(pos);

                    Block replacement = getReplacement(state);

                    if (replacement != null) {
                        level.setBlock(pos, replacement.defaultBlockState(), 3);
                    }
                }
            }
        }
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
