package net.shule.shulespotions.StabilityEvent.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.shule.shulespotions.Blocks.Entities.PotionSplashBE;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.Entities.ModEntities;
import net.shule.shulespotions.Entities.Projectile.PotionSplashProjectile;
import net.shule.shulespotions.Messages.ModMessages;
import net.shule.shulespotions.Messages.SyncPotionSplashColorPacket;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.Particles.ModParticles;
import net.shule.shulespotions.StabilityEvent.StabilityEvent;
import net.shule.shulespotions.util.CauldronActions.CauldronContext;
import net.shule.shulespotions.util.ColorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PotionExplodeEvent implements StabilityEvent {
    @Override
    public boolean canTrigger(CauldronContext ctx) {
        return ctx.getCauldron().getPotionLiquid().getStats().getStability() <= -15;
    }

    @Override
    public void trigger(CauldronContext ctx) {
        Level level = ctx.getCauldron().getLevel();

        if (level == null) return;

        BlockPos pos = ctx.getCauldron().getBlockPos();

        level.playSound(ctx.getPlayer(), ctx.getPos(), SoundEvents.SLIME_DEATH, SoundSource.BLOCKS);

        int color = ctx.getCauldron()
                .getPotionLiquid()
                .getColor();


        ctx.getCauldron().triggerExplosionParticles(color);
        spawnPotionSplashes(level, pos, 40, color);
        spawnPotionProjectiles(level, color, ctx.getCauldron(), 30);
        applyEffectInArea(level,pos,color);

    }

    @Override
    public float getChance(CauldronContext ctx) {
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



    private static void spawnPotionProjectiles(Level level, int color, BlockEntity cauldron, int amount) {

        Vec3 center = new Vec3(
                cauldron.getBlockPos().getX() + 0.5,
                cauldron.getBlockPos().getY() + 1.0,
                cauldron.getBlockPos().getZ() + 0.5
        );

        RandomSource random = level.getRandom();

        for (int i = 0; i < amount; i++) {

            PotionSplashProjectile projectile =
                    new PotionSplashProjectile(
                            ModEntities.POTION_SPLASH_PROJECTILE.get(),
                            level
                    );

            projectile.setScale(0.5f + random.nextFloat() * 0.5f);
            projectile.setPotionColor(color);
            projectile.setPos(center);

            double angle = random.nextDouble() * Math.PI * 2.0;

            double horizontalSpeed = 0.15 + random.nextDouble() * 0.25;

            double xMotion = Math.cos(angle) * horizontalSpeed;
            double zMotion = Math.sin(angle) * horizontalSpeed;

            double yMotion = 0.25 + random.nextDouble() * 0.25;

            projectile.setDeltaMovement(
                    xMotion,
                    yMotion,
                    zMotion
            );

            level.addFreshEntity(projectile);
        }
    }

    private static void applyEffectInArea(Level level, BlockPos pos, int color) {
        AABB area = new AABB(pos).inflate(4);

        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area)) {
            entity.getPersistentData().putInt("PotionSplashColor", color);
            entity.addEffect(new MobEffectInstance(ModMobEffects.POTION_SPLASHED.get(),
                    20 * 15, 0, false, false));
            ModMessages.INSTANCE.send(
                    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                    new SyncPotionSplashColorPacket(entity.getId(), color)
            );
        }
    }
}