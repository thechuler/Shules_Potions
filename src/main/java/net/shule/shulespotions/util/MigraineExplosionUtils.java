package net.shule.shulespotions.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.Messages.ModMessages;
import net.shule.shulespotions.Messages.SyncPotionSplashColorPacket;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;

import net.shule.shulespotions.ShulesPotions;
import net.shule.shulespotions.Entities.ModEntities;
import net.shule.shulespotions.Entities.Projectile.PotionSplashProjectile;
import net.shule.shulespotions.Particles.ModParticles;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.Blocks.Entities.PotionSplashBE;
import net.shule.shulespotions.Sounds.ModSounds;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ClipContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MigraineExplosionUtils {

    public static final ResourceKey<DamageType> MIGRAINE_EXPLOSION = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID, "migraine_explosion"));

    public static void explode(Level level, LivingEntity sourceEntity, float damage, int color) {
        BlockPos pos = sourceEntity.blockPosition();



        double radius = Math.min(30.0, 3.0 + (damage * 0.05));
        int particleAmount = Math.min(4000, (int) (200 + (damage * 5)));
        int projectileAmount = Math.min(40, (int) (5 + (damage * 0.1)));
        int splashAmount = Math.min(100, (int) (10 + (damage * 0.3)));

        if (level instanceof ServerLevel serverLevel) {

            serverLevel.playSound(null, pos, ModSounds.HEAD_EXPLODE.get(), net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
            serverLevel.playSound(null, pos, SoundEvents.SLIME_DEATH, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 0.5F);

            float[] rgb = ColorUtils.intToRGB(color);
            RandomSource random = serverLevel.getRandom();



            List<Vec3> gaps = new ArrayList<>();
            int numGaps = 3 + random.nextInt(4);
            double gapRadiusSqr = (radius * 0.35) * (radius * 0.35);
            
            for(int i = 0; i < numGaps; i++) {
                double gu = random.nextDouble() * 2.0 - 1.0;
                double gTheta = random.nextDouble() * 2.0 * Math.PI;
                double gDist = radius * Math.cbrt(random.nextDouble());
                double gR = Math.sqrt(1.0 - gu * gu) * gDist;
                gaps.add(new Vec3(gR * Math.cos(gTheta), gu * gDist, gR * Math.sin(gTheta)));
            }

            for (int i = 0; i < particleAmount; i++) {

                double u = random.nextDouble() * 2.0 - 1.0; 
                double theta = random.nextDouble() * 2.0 * Math.PI; 
                double distance = radius * Math.cbrt(random.nextDouble());
                
                double r = Math.sqrt(1.0 - u * u) * distance;
                
                double offsetX = r * Math.cos(theta);
                double offsetY = u * distance;
                double offsetZ = r * Math.sin(theta);
                

                boolean inGap = false;
                for (Vec3 gap : gaps) {
                    if (gap.distanceToSqr(offsetX, offsetY, offsetZ) < gapRadiusSqr) {
                        inGap = true;
                        break;
                    }
                }
                if (inGap) continue;
                
                serverLevel.sendParticles(
                        ModParticles.POTION_EXPLOTION.get(),
                        pos.getX() + 0.5 + offsetX, 
                        pos.getY() + 1.0 + offsetY, 
                        pos.getZ() + 0.5 + offsetZ,
                        0,
                        rgb[0], rgb[1], rgb[2],
                        1.0
                );
            }
            
            spawnProjectiles(serverLevel, sourceEntity, color, projectileAmount);
        }


        spawnMigraineSplashes(level, pos, splashAmount, color, (int) Math.ceil(radius));


        AABB area = new AABB(pos).inflate(radius);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
        
        DamageSource damageSource = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(MIGRAINE_EXPLOSION));

        Vec3 centerPosVec = sourceEntity.position();

        for (LivingEntity target : entities) {
            if (target.equals(sourceEntity)) {
                continue;
            }

            if (target.distanceToSqr(centerPosVec) > radius * radius) {
                continue;
            }

            target.hurt(damageSource, damage);

            target.getPersistentData().putInt("PotionSplashColor", color);
            target.addEffect(new MobEffectInstance(ModMobEffects.POTION_SPLASHED.get(), 20 * 15, 0, false, false));
            
            if (!level.isClientSide()) {
                ModMessages.INSTANCE.send(
                        PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> target),
                        new SyncPotionSplashColorPacket(target.getId(), color)
                );
            }
        }

        if(!sourceEntity.hasEffect(ModMobEffects.DECAPITATED.get())){
            sourceEntity.addEffect(new MobEffectInstance(ModMobEffects.DECAPITATED.get(), 20 * 10));
        }
    }

    private static void spawnProjectiles(ServerLevel level, LivingEntity source, int color, int amount) {
        Vec3 center = source.position().add(0, source.getBbHeight() / 2.0, 0);
        RandomSource random = level.getRandom();

        for (int i = 0; i < amount; i++) {
            PotionSplashProjectile projectile = new PotionSplashProjectile(ModEntities.POTION_SPLASH_PROJECTILE.get(), level);
            projectile.setScale(0.5f + random.nextFloat() * 0.5f);
            projectile.setPotionColor(color);
            projectile.setPos(center);

            double angle = random.nextDouble() * Math.PI * 2.0;
            double horizontalSpeed = 0.15 + random.nextDouble() * 0.25;
            double xMotion = Math.cos(angle) * horizontalSpeed;
            double zMotion = Math.sin(angle) * horizontalSpeed;
            double yMotion = 0.25 + random.nextDouble() * 0.25;

            projectile.setDeltaMovement(xMotion, yMotion, zMotion);
            level.addFreshEntity(projectile);
        }
    }

    private static void spawnMigraineSplashes(Level level, BlockPos centerPos, int maxSplashes, int color, int radius) {
        List<BlockPos> candidates = new ArrayList<>();
        Vec3 origin = Vec3.atCenterOf(centerPos).add(0, 1.2, 0);

        for (BlockPos pos : BlockPos.withinManhattan(centerPos, radius, radius, radius)) {
            if (pos.equals(centerPos)) continue;
            if (!level.getBlockState(pos).isAir()) continue;

            boolean hasSurface = false;
            for (Direction dir : Direction.values()) {
                BlockPos adjacent = pos.relative(dir);
                BlockState adjacentState = level.getBlockState(adjacent);
                if (adjacentState.isFaceSturdy(level, adjacent, dir.getOpposite())) {
                    hasSurface = true;
                    break;
                }
            }

            if (!hasSurface) continue;

            BlockHitResult hit = level.clip(new ClipContext(origin, Vec3.atCenterOf(pos), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
            if (hit.getType() != HitResult.Type.MISS && !hit.getBlockPos().equals(pos)) {
                continue;
            }

            candidates.add(pos.immutable());
        }

        if (candidates.isEmpty()) return;

        candidates.sort(Comparator.comparingDouble(a -> a.distSqr(centerPos)));
        List<BlockPos> selected = new ArrayList<>();
        RandomSource random = level.getRandom();

        for (BlockPos candidate : candidates) {
            double distance = Math.sqrt(candidate.distSqr(centerPos));
            double chance = Math.max(0.1, 1.0 - (distance / (double) radius));
            if (random.nextDouble() < chance) {
                selected.add(candidate);
            }
            if (selected.size() >= maxSplashes) break;
        }

        for (BlockPos splashPos : selected) {
            for (Direction dir : Direction.values()) {
                BlockPos supportPos = splashPos.relative(dir);
                BlockState supportState = level.getBlockState(supportPos);
                if (!supportState.isFaceSturdy(level, supportPos, dir.getOpposite())) continue;
                if (!level.getBlockState(splashPos).isAir()) break;

                BlockState splashState = ModBlocks.POTION_SPLASH.get().defaultBlockState().setValue(MultifaceBlock.getFaceProperty(dir), true);
                level.setBlock(splashPos, splashState, Block.UPDATE_ALL);
                
                if (level.getBlockEntity(splashPos) instanceof PotionSplashBE be) {
                    be.setColor(color);
                }
                break;
            }
        }
    }
}
