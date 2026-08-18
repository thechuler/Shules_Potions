package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.shule.shulespotions.Sounds.ModSounds;

public class ExplosiveSneezeEffect extends MobEffect {
    public ExplosiveSneezeEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }


    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Level level = pLivingEntity.level();
        level.playSound(null, pLivingEntity.getOnPos(), ModSounds.SNEEZE.get(), SoundSource.PLAYERS, 2, 1);
       
        Vec3 look = pLivingEntity.getLookAngle();
        double pushStrength = 1.0 + (pAmplifier * 0.5);
        pLivingEntity.setDeltaMovement(pLivingEntity.getDeltaMovement().add(
                -look.x * pushStrength,
                -look.y * pushStrength,
                -look.z * pushStrength
        ));
        pLivingEntity.hurtMarked = true;

        boolean wasInvulnerable = pLivingEntity.isInvulnerable();
        pLivingEntity.setInvulnerable(true);

        level.explode(pLivingEntity,
                pLivingEntity.getX() + look.x * 1.5,
                pLivingEntity.getY() + pLivingEntity.getEyeHeight() * 0.5,
                pLivingEntity.getZ() + look.z * 1.5,
                1 + pAmplifier, Level.ExplosionInteraction.NONE);

        pLivingEntity.setInvulnerable(wasInvulnerable);

        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 60 == 0 && Math.random() < 0.40;
    }
}
