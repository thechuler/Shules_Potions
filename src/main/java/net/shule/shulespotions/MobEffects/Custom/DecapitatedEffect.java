package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.network.PacketDistributor;
import net.shule.shulespotions.Messages.ModMessages;
import net.shule.shulespotions.Messages.SyncDecapitatedActivePacket;

public class DecapitatedEffect extends MobEffect {
    public DecapitatedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            ModMessages.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> pLivingEntity),
                new SyncDecapitatedActivePacket(pLivingEntity.getId(), true));
        }
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            ModMessages.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> pLivingEntity),
                new SyncDecapitatedActivePacket(pLivingEntity.getId(), false));
        }
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }
}
