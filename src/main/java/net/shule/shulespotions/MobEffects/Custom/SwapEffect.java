package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class SwapEffect extends MobEffect {
    public SwapEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, LivingEntity pLivingEntity, int pAmplifier, double pHealth) {

        Entity thrower = pIndirectSource != null ? pIndirectSource : pSource;
        
        if (thrower != null && pLivingEntity != null && thrower != pLivingEntity) {
            double throwerX = thrower.getX();
            double throwerY = thrower.getY();
            double throwerZ = thrower.getZ();
            
            double targetX = pLivingEntity.getX();
            double targetY = pLivingEntity.getY();
            double targetZ = pLivingEntity.getZ();
            

            if (thrower.level() instanceof ServerLevel serverLevel) {
                

                double throwerMidY = throwerY + thrower.getBbHeight() / 2.0;
                double targetMidY = targetY + pLivingEntity.getBbHeight() / 2.0;
                
                double dx = targetX - throwerX;
                double dy = targetMidY - throwerMidY;
                double dz = targetZ - throwerZ;
                
                for (int i = 0; i < 30; i++) {
                    double randX = (serverLevel.random.nextDouble() - 0.5) * 1.5;
                    double randY = (serverLevel.random.nextDouble() - 0.5) * 1.5;
                    double randZ = (serverLevel.random.nextDouble() - 0.5) * 1.5;
                    

                    serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.PORTAL, 
                            throwerX, throwerMidY, throwerZ, 0, dx + randX, dy + randY, dz + randZ, 1.0);
                            

                    serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.PORTAL, 
                            targetX, targetMidY, targetZ, 0, -dx + randX, -dy + randY, -dz + randZ, 1.0);
                }

                if (thrower instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.teleport(targetX, targetY, targetZ, serverPlayer.getYRot(), serverPlayer.getXRot());
                } else {
                    thrower.teleportTo(targetX, targetY, targetZ);
                }
                
                if (pLivingEntity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.teleport(throwerX, throwerY, throwerZ, serverPlayer.getYRot(), serverPlayer.getXRot());
                } else {
                    pLivingEntity.teleportTo(throwerX, throwerY, throwerZ);
                }
            }
        }

        super.applyInstantenousEffect(pSource, pIndirectSource, pLivingEntity, pAmplifier, pHealth);
    }
}
