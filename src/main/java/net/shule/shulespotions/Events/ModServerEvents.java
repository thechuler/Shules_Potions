package net.shule.shulespotions.Events;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.network.PacketDistributor;
import net.shule.shulespotions.Messages.ModMessages;
import net.shule.shulespotions.Messages.Custom.SyncCloneStatusPacket;
import net.shule.shulespotions.Messages.Custom.SyncPotionSplashColorPacket;
import net.shule.shulespotions.Messages.Custom.SyncMigraineActivePacket;
import net.shule.shulespotions.Messages.Custom.SyncDecapitatedActivePacket;
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

        if (data.contains("shulespotions:missing_limbs")) {
            byte missingLimbs = data.getByte("shulespotions:missing_limbs");
            if (missingLimbs > 0) {
                int headEntityId = -1;
                if ((missingLimbs & 1) != 0 && playerToReceivePacket.equals(targetEntity)) {
                    if (data.hasUUID("shulespotions:head_uuid")) {
                        java.util.UUID headUUID = data.getUUID("shulespotions:head_uuid");
                        if (playerToReceivePacket.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                            Entity head = serverLevel.getEntity(headUUID);
                            if (head != null) {
                                headEntityId = head.getId();
                            }
                        }
                    }
                }
                ModMessages.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> playerToReceivePacket),
                    new net.shule.shulespotions.Messages.Custom.SyncBodyPartsPacket(targetEntity.getId(), missingLimbs, headEntityId)
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
        ServerPlayer player = (ServerPlayer) event.getEntity();
        player.getPersistentData().putBoolean("shulespotions:head_camera_synced", false);

        if (!player.hasEffect(ModMobEffects.BODY_BREAK_DOWN.get())) {
            player.getPersistentData().remove(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.NBT_MISSING_LIMBS);
            player.getPersistentData().remove(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.NBT_HEAD_UUID);
            player.getPersistentData().remove(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.NBT_HEAD_SYNCED);
            var maxHp = player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH);
            if (maxHp != null) {
                maxHp.removeModifier(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.HEALTH_MODIFIER_UUID);
            }
            var speed = player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED);
            if (speed != null) {
                speed.removeModifier(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.SPEED_MODIFIER_UUID);
            }
        }

        syncEntityData(player, player);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent event) {
        var player = event.getEntity();
        player.getPersistentData().putBoolean("shulespotions:head_camera_synced", false);
        player.getPersistentData().remove(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.NBT_MISSING_LIMBS);
        player.getPersistentData().remove(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.NBT_HEAD_UUID);
        player.getPersistentData().remove(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.NBT_HEAD_SYNCED);

        var maxHp = player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH);
        if (maxHp != null) {
            maxHp.removeModifier(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.HEALTH_MODIFIER_UUID);
        }
        var speed = player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED);
        if (speed != null) {
            speed.removeModifier(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.SPEED_MODIFIER_UUID);
        }

        syncEntityData((ServerPlayer) player, player);
    }

    @SubscribeEvent
    public static void onLivingDeath(net.minecraftforge.event.entity.living.LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!entity.level().isClientSide() && entity.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            if (entity.getPersistentData().contains(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.NBT_MISSING_LIMBS)) {
                java.util.UUID ownerUUID = entity.getUUID();
                java.util.List<Entity> toDiscard = new java.util.ArrayList<>();
                for (Entity e : serverLevel.getAllEntities()) {
                    if (e instanceof net.shule.shulespotions.Entities.entity.PlayerBodyPartEntity limb) {
                        if (ownerUUID.equals(limb.getPlayerUUID())) {
                            toDiscard.add(limb);
                        }
                    }
                }
                for (Entity limb : toDiscard) {
                    limb.discard();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        player.getPersistentData().putBoolean("shulespotions:head_camera_synced", false);

        if (player.getPersistentData().contains(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.NBT_MISSING_LIMBS)) {
            byte mask = player.getPersistentData().getByte(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.NBT_MISSING_LIMBS);
            if ((mask & net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.HEAD) != 0) {
                byte newMask = (byte) (mask & ~net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.HEAD);
                player.getPersistentData().remove(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.NBT_HEAD_UUID);
                player.getPersistentData().remove(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.NBT_HEAD_SYNCED);
                if (newMask == 0) {
                    player.getPersistentData().remove(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.NBT_MISSING_LIMBS);
                } else {
                    player.getPersistentData().putByte(net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.NBT_MISSING_LIMBS, newMask);
                }
                net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect.updateModifiers(player, newMask);
            }
        }

        syncEntityData(player, player);
    }

    @SubscribeEvent
    public static void onLivingTick(net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (!entity.level().isClientSide()) {
            if (entity.getPersistentData().getBoolean("MigraineForceRemove")) {
                entity.removeEffect(ModMobEffects.MIGRAINE.get());
                entity.getPersistentData().remove("MigraineForceRemove");
            }

            if (entity instanceof ServerPlayer player) {
                CompoundTag data = player.getPersistentData();
                if (data.contains("shulespotions:missing_limbs")) {
                    byte missingLimbs = data.getByte("shulespotions:missing_limbs");
                    if ((missingLimbs & 1) != 0 && data.hasUUID("shulespotions:head_uuid") && !data.getBoolean("shulespotions:head_camera_synced")) {
                        java.util.UUID headUUID = data.getUUID("shulespotions:head_uuid");
                        if (player.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                            Entity head = serverLevel.getEntity(headUUID);
                            if (head != null) {
                                ModMessages.INSTANCE.send(
                                    PacketDistributor.PLAYER.with(() -> player),
                                    new net.shule.shulespotions.Messages.Custom.SyncBodyPartsPacket(player.getId(), missingLimbs, head.getId())
                                );
                                data.putBoolean("shulespotions:head_camera_synced", true);
                                data.remove("shulespotions:head_find_attempts");
                            } else {
                                int attempts = data.getInt("shulespotions:head_find_attempts") + 1;
                                data.putInt("shulespotions:head_find_attempts", attempts);
                                if (attempts > 80) {
                                    data.remove("shulespotions:head_find_attempts");
                                    player.removeEffect(ModMobEffects.BODY_BREAK_DOWN.get());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
