package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.network.PacketDistributor;
import net.shule.shulespotions.Messages.ModMessages;
import net.shule.shulespotions.Messages.SyncButterFingersPacket;

public class ButterFingersEffect extends MobEffect {
    public ButterFingersEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        if (!pLivingEntity.level().isClientSide()) {
            pLivingEntity.getPersistentData().putBoolean("shulespotions:has_butter_fingers", true);
            ModMessages.INSTANCE.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> pLivingEntity),
                new SyncButterFingersPacket(pLivingEntity.getId(), true)
            );
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        if (!pLivingEntity.level().isClientSide()) {
            pLivingEntity.getPersistentData().remove("shulespotions:has_butter_fingers");
            ModMessages.INSTANCE.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> pLivingEntity),
                new SyncButterFingersPacket(pLivingEntity.getId(), false)
            );
        }
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

        if (!pLivingEntity.level().isClientSide() && pLivingEntity instanceof Player player) {
            ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (!item.isEmpty()) {
                player.drop(item.copy(), false, true);
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                player.level().playSound(null, player.blockPosition(), SoundEvents.HONEY_BLOCK_SLIDE, SoundSource.PLAYERS, 1f, 2.2f);
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int rate = Math.max(10, 60 - (pAmplifier * 10)); 
        return pDuration % rate == 0;
    }
}
