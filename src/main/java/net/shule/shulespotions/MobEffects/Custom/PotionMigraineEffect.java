package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.PacketDistributor;
import net.shule.shulespotions.Messages.ModMessages;
import net.shule.shulespotions.Messages.SyncMigraineActivePacket;
import net.shule.shulespotions.util.MigraineExplosionUtils;

public class PotionMigraineEffect extends MobEffect {

    public static final String DAMAGE_TAG = "MigraineStackedDamage";

    public PotionMigraineEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int pAmplifier) {
        MobEffectInstance instance = entity.getEffect(this);
        if (instance == null) return;
        
        int duration = instance.getDuration();

        if (duration % 20 == 0) {
            float currentDamage = entity.getPersistentData().getFloat(DAMAGE_TAG);

            float addedDamage = 5.0f * (pAmplifier + 1);
            entity.getPersistentData().putFloat(DAMAGE_TAG, currentDamage + addedDamage);
        }

        int activeTicks = entity.getPersistentData().getInt("MigraineActiveTicks");
        activeTicks++;
        entity.getPersistentData().putInt("MigraineActiveTicks", activeTicks);

        float growthRate = 0.005f * (pAmplifier + 1);
        float currentScale = 1.0f + (activeTicks * growthRate);

        if (currentScale >= 5.0f || duration == 1) {
            float totalDamage = entity.getPersistentData().getFloat(DAMAGE_TAG);
            
            if (totalDamage > 0) {

                if (!entity.level().isClientSide()) {
                    MigraineExplosionUtils.explode(entity.level(), entity, totalDamage, this.getColor());
                }

                entity.getPersistentData().remove(DAMAGE_TAG);
            }

            if (currentScale >= 3.0f && duration > 1) {
                entity.getPersistentData().putBoolean("MigraineForceRemove", true);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            pLivingEntity.getPersistentData().putInt("MigraineActiveTicks", 0);
            ModMessages.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> pLivingEntity), 
                new SyncMigraineActivePacket(pLivingEntity.getId(), pAmplifier, true));
        }
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            pLivingEntity.getPersistentData().remove("MigraineActiveTicks");
            ModMessages.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> pLivingEntity), 
                new SyncMigraineActivePacket(pLivingEntity.getId(), pAmplifier, false));
        }
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }
}
