package net.shule.shulespotions.Messages;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.shule.shulespotions.Events.BodyBreakDownClientEvents;
import net.shule.shulespotions.Messages.Custom.SyncBodyPartsPacket;
import net.shule.shulespotions.Messages.Custom.SyncButterFingersPacket;
import net.shule.shulespotions.Messages.Custom.SyncCloneStatusPacket;
import net.shule.shulespotions.Messages.Custom.SyncDecapitatedActivePacket;
import net.shule.shulespotions.Messages.Custom.SyncMigraineActivePacket;
import net.shule.shulespotions.Messages.Custom.SyncPotionSplashColorPacket;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandler {

    public static void handlePotionSplashColor(SyncPotionSplashColorPacket msg) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        Entity entity = mc.level.getEntity(msg.entityId);
        if (entity == null) return;

        if (msg.color == -1) {
            entity.getPersistentData().remove("PotionSplashColor");
        } else {
            entity.getPersistentData().putInt("PotionSplashColor", msg.color);
        }
    }

    public static void handleCloneStatus(SyncCloneStatusPacket msg) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        Entity entity = mc.level.getEntity(msg.entityId);
        if (entity == null) return;

        entity.getPersistentData().putBoolean("shulespotions:is_clone", msg.isClone);
    }

    public static void handleButterFingers(SyncButterFingersPacket msg) {
        if (Minecraft.getInstance().level != null) {
            Entity entity = Minecraft.getInstance().level.getEntity(msg.entityId);
            if (entity != null) {
                if (msg.hasButterFingers) {
                    entity.getPersistentData().putBoolean("shulespotions:has_butter_fingers", true);
                } else {
                    entity.getPersistentData().remove("shulespotions:has_butter_fingers");
                }
            }
        }
    }

    public static void handleMigraineActive(SyncMigraineActivePacket msg) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            Entity entity = level.getEntity(msg.entityId);
            if (entity != null) {
                if (msg.isActive) {
                    entity.getPersistentData().putBoolean("HasMigraine", true);
                    entity.getPersistentData().putInt("MigraineAmplifier", msg.amplifier);
                    entity.getPersistentData().putInt("MigraineStartTick", entity.tickCount);
                } else {
                    entity.getPersistentData().putBoolean("HasMigraine", false);
                }
            }
        }
    }

    public static void handleDecapitatedActive(SyncDecapitatedActivePacket msg) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            Entity entity = level.getEntity(msg.entityId);
            if (entity != null) {
                entity.getPersistentData().putBoolean("HasDecapitated", msg.isActive);
            }
        }
    }

    public static void handleBodyParts(SyncBodyPartsPacket msg) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            Entity entity = level.getEntity(msg.entityId);
            if (entity != null) {
                if (msg.missingLimbsMask == 0) {
                    entity.getPersistentData().remove("shulespotions:missing_limbs");
                    entity.getPersistentData().remove("shulespotions:head_uuid");
                    entity.getPersistentData().remove("shulespotions:head_camera_synced");
                } else {
                    entity.getPersistentData().putByte("shulespotions:missing_limbs", msg.missingLimbsMask);
                }
            }

            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.player.getId() == msg.entityId) {
                if (msg.headEntityId != -1) {
                    Entity headEntity = level.getEntity(msg.headEntityId);
                    if (headEntity != null) {
                        mc.setCameraEntity(headEntity);
                    } else {
                        BodyBreakDownClientEvents.pendingHeadEntityId = msg.headEntityId;
                    }
                } else if ((msg.missingLimbsMask & 1) == 0) {
                    BodyBreakDownClientEvents.pendingHeadEntityId = -1;
                    mc.setCameraEntity(mc.player);
                }
            }
        }
    }
}
