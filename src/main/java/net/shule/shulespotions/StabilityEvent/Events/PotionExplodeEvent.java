package net.shule.shulespotions.StabilityEvent.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.shule.shulespotions.Blocks.Entities.PotionSplashBE;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.StabilityEvent.StabilityEvent;
import net.shule.shulespotions.util.CauldronActions.CauldronContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PotionExplodeEvent implements StabilityEvent {
    @Override
    public boolean canTrigger(CauldronContext ctx){
        return ctx.getCauldron().getPotionLiquid().getStats().getStability() <= 30;
    }

    @Override
    public void trigger(CauldronContext ctx) {
        Level level = ctx.getCauldron().getLevel();

        if(level == null) return;

        BlockPos pos = ctx.getCauldron().getBlockPos();


        level.explode(
                null,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                2.5f,
                Level.ExplosionInteraction.NONE
        );

        spawnPotionSplashes(level, pos, 40,ctx.getCauldron().getPotionLiquid().getColor());


    }

    @Override
    public float getChance(CauldronContext ctx){
        int stability = ctx.getCauldron()
                        .getPotionLiquid()
                        .getStats()
                        .getStability();

        return (30 - stability) / 30f;
    }


    public static void spawnPotionSplashes(Level level, BlockPos cauldronPos, int maxSplashes, int color) {

        List<BlockPos> candidates = new ArrayList<>();

        Vec3 origin = Vec3.atCenterOf(cauldronPos).add(0, 1.2, 0);

        for (BlockPos pos : BlockPos.withinManhattan(cauldronPos, 4, 4, 4)) {

            if (pos.equals(cauldronPos))
                continue;

            if (!level.getBlockState(pos).isAir())
                continue;

            boolean hasSurface = false;

            for (Direction dir : Direction.values()) {

                BlockPos adjacent = pos.relative(dir);
                BlockState adjacentState = level.getBlockState(adjacent);

                if (adjacentState.isFaceSturdy(level, adjacent, dir.getOpposite())) {
                    hasSurface = true;
                    break;
                }
            }

            if (!hasSurface)
                continue;


            BlockHitResult hit = level.clip(
                    new ClipContext(
                            origin,
                            Vec3.atCenterOf(pos),
                            ClipContext.Block.COLLIDER,
                            ClipContext.Fluid.NONE,
                            null
                    )
            );

            if (hit.getType() != HitResult.Type.MISS &&
                    !hit.getBlockPos().equals(pos)) {
                continue;
            }

            candidates.add(pos.immutable());
        }

        if (candidates.isEmpty())
            return;

        candidates.sort((a, b) -> {
            double distA = a.distSqr(cauldronPos);
            double distB = b.distSqr(cauldronPos);
            return Double.compare(distA, distB);
        });

        List<BlockPos> selected = new ArrayList<>();
        RandomSource random = level.getRandom();

        for (BlockPos candidate : candidates) {

            double distance = Math.sqrt(candidate.distSqr(cauldronPos));
            double chance = Math.max(0.1, 1.0 - (distance / 4.0));

            if (random.nextDouble() < chance) {
                selected.add(candidate);
            }

            if (selected.size() >= maxSplashes)
                break;
        }


        for (BlockPos splashPos : selected) {



            for (Direction dir : Direction.values()) {

                BlockPos supportPos = splashPos.relative(dir);
                BlockState supportState = level.getBlockState(supportPos);

                if (!supportState.isFaceSturdy(level, supportPos, dir.getOpposite()))
                    continue;

                if (!level.getBlockState(splashPos).isAir())
                    break;

                BlockState splashState =
                        ModBlocks.POTION_SPLASH.get()
                                .defaultBlockState()
                                .setValue(
                                        MultifaceBlock.getFaceProperty(dir),
                                        true
                                );

                level.setBlock(splashPos, splashState, Block.UPDATE_ALL);
                if (level.getBlockEntity(splashPos) instanceof PotionSplashBE be) {
                    be.setColor(color);
                }
                break;
            }

        }
    }
}
