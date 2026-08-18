package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class OrbitalGravityEffect extends MobEffect {
    public OrbitalGravityEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        double range = 10.0 + (pAmplifier * 2.0);
        double targetRadius = 6.0 + pAmplifier;
        
        AABB boundingBox = pLivingEntity.getBoundingBox().inflate(range);
        List<Entity> entities = pLivingEntity.level().getEntities(pLivingEntity, boundingBox);

        for (Entity entity : entities) {

            if (!(entity instanceof LivingEntity) && !(entity instanceof ItemEntity)) continue;

            if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) continue;


            boolean isBoss = entity instanceof EnderDragon
                    || entity instanceof WitherBoss
                    || entity instanceof Warden
                    || entity instanceof ElderGuardian;
            
            if (isBoss) continue;

            double dx = entity.getX() - pLivingEntity.getX();
            double dy = entity.getY() - (pLivingEntity.getY() + pLivingEntity.getBbHeight() / 2.0);
            double dz = entity.getZ() - pLivingEntity.getZ();
            
            double horizontalDistanceSq = dx * dx + dz * dz;
            double distanceSq = horizontalDistanceSq + dy * dy;
            
            if (distanceSq > range * range) continue;
            
            double horizontalDistance = Math.sqrt(horizontalDistanceSq);
            if (horizontalDistance < 0.001) horizontalDistance = 0.001;
            

            double nx = dx / horizontalDistance;
            double nz = dz / horizontalDistance;
            

            double tx = -nz;
            double tz = nx;
            

            double pullStrength = (horizontalDistance - targetRadius) * 0.75;
            double pullY = -dy * 0.15;
            
            double speed = 0.3 + (pAmplifier * 0.1);
            

            double targetVx = tx * speed - nx * pullStrength;
            double targetVy = pullY;
            double targetVz = tz * speed - nz * pullStrength;
            

            if (entity.horizontalCollision) {
                targetVy += 0.3;
            }

            entity.setDeltaMovement(
                entity.getDeltaMovement().x * 0.6 + targetVx * 0.4,
                entity.getDeltaMovement().y * 0.6 + targetVy * 0.4,
                entity.getDeltaMovement().z * 0.6 + targetVz * 0.4
            );
            
            entity.fallDistance = 0;
            

            if (entity.tickCount % 10 == 0) {
                entity.hurtMarked = true;
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
