package net.shule.shulespotions.MobEffects.Custom;


import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class LowGravityEffect extends MobEffect {
    public LowGravityEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.isNoGravity() && !pLivingEntity.onGround()) {

            double extraGravity = Math.min(0.04D + (0.01D * pAmplifier), 0.075D);
            Vec3 vec3 = pLivingEntity.getDeltaMovement();
            pLivingEntity.setDeltaMovement(vec3.x, vec3.y + extraGravity, vec3.z);
        }


        pLivingEntity.resetFallDistance();
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
