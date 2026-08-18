package net.shule.shulespotions.Events;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.network.PacketDistributor;
import net.shule.shulespotions.Messages.ModMessages;
import net.shule.shulespotions.Messages.SyncCloneStatusPacket;
import net.shule.shulespotions.Messages.SyncPotionSplashColorPacket;
import net.shule.shulespotions.Messages.SyncMigraineActivePacket;
import net.shule.shulespotions.Messages.SyncDecapitatedActivePacket;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.shule.shulespotions.MobEffects.Custom.LeechEffect;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(modid = ShulesPotions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModServerEvents {



    private static void syncEntityData(ServerPlayer playerToReceivePacket, Entity targetEntity) {
        if (targetEntity.level().isClientSide) return;
        
        CompoundTag data = targetEntity.getPersistentData();

        if (data.contains("PotionSplashColor")) {
            int color = data.getInt("PotionSplashColor");
            ModMessages.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> playerToReceivePacket),
                new SyncPotionSplashColorPacket(targetEntity.getId(), color)
            );
        }

        if (data.contains("shulespotions:is_clone")) {
            boolean isClone = data.getBoolean("shulespotions:is_clone");
            if (isClone) {
                ModMessages.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> playerToReceivePacket),
                    new SyncCloneStatusPacket(targetEntity.getId(), true)
                );
            }
        }
    }

    @SubscribeEvent
    public static void onStartTracking(net.minecraftforge.event.entity.player.PlayerEvent.StartTracking event) {

        syncEntityData((ServerPlayer) event.getEntity(), event.getTarget());
        
        if (event.getTarget() instanceof LivingEntity living) {
            MobEffectInstance migraineEffect = living.getEffect(ModMobEffects.MIGRAINE.get());
            if (migraineEffect != null) {
                ModMessages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), 
                    new SyncMigraineActivePacket(living.getId(), migraineEffect.getAmplifier(), true));
            }
            
            MobEffectInstance decapitatedEffect = living.getEffect(ModMobEffects.DECAPITATED.get());
            if (decapitatedEffect != null) {
                ModMessages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), 
                    new SyncDecapitatedActivePacket(living.getId(), true));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
        syncEntityData((ServerPlayer) event.getEntity(), event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent event) {
        syncEntityData((ServerPlayer) event.getEntity(), event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent event) {
        syncEntityData((ServerPlayer) event.getEntity(), event.getEntity());
    }

    @SubscribeEvent
    public static void onLivingTick(net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (!entity.level().isClientSide()) {
            if (entity.getPersistentData().getBoolean("MigraineForceRemove")) {
                entity.removeEffect(ModMobEffects.MIGRAINE.get());
                entity.getPersistentData().remove("MigraineForceRemove");
            }
        }
    }
}
