package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.shule.shulespotions.Entities.entity.PlayerArmEntity;
import net.shule.shulespotions.Entities.entity.PlayerBodyPartEntity;
import net.shule.shulespotions.Entities.entity.PlayerHeadEntity;
import net.shule.shulespotions.Entities.entity.PlayerLegEntity;
import net.shule.shulespotions.Messages.Custom.SyncBodyPartsPacket;
import net.shule.shulespotions.Messages.ModMessages;
import net.shule.shulespotions.Sounds.ModSounds;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BodyBreakDownEffect extends MobEffect {

    public static final byte HEAD = 1;
    public static final byte RIGHT_ARM = 1 << 1;
    public static final byte LEFT_ARM = 1 << 2;
    public static final byte RIGHT_LEG = 1 << 3;
    public static final byte LEFT_LEG = 1 << 4;
    public static final byte ALL_LIMBS = 31;

    public static final String NBT_MISSING_LIMBS = "shulespotions:missing_limbs";
    public static final String NBT_HEAD_UUID = "shulespotions:head_uuid";
    public static final String NBT_HEAD_SYNCED = "shulespotions:head_camera_synced";

    public static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("b8c146d2-97ec-44f2-bdfa-7a549618b142");
    public static final UUID SPEED_MODIFIER_UUID = UUID.fromString("d3e21544-2451-4fa3-9f12-5883d6928e40");

    public BodyBreakDownEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public static void updateModifiers(LivingEntity entity, byte mask) {
        if (entity.level().isClientSide()) return;

        AttributeInstance maxHpAttr = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHpAttr != null) {
            maxHpAttr.removeModifier(HEALTH_MODIFIER_UUID);
            int missingCount = Integer.bitCount(mask & ALL_LIMBS);
            if (missingCount > 0) {
                double baseMax = maxHpAttr.getBaseValue();
                double reduction = - (missingCount * (baseMax / 6.0));
                if (baseMax + reduction < 1.0D) {
                    reduction = 1.0D - baseMax;
                }
                maxHpAttr.addTransientModifier(new AttributeModifier(
                        HEALTH_MODIFIER_UUID, "Body breakdown health penalty", reduction, AttributeModifier.Operation.ADDITION
                ));
                if (entity.getHealth() > entity.getMaxHealth()) {
                    entity.setHealth(entity.getMaxHealth());
                }
            }
        }

        AttributeInstance speedAttr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttr != null) {
            speedAttr.removeModifier(SPEED_MODIFIER_UUID);
        }
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        if (!(pLivingEntity instanceof Player)) return;
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        if (!pLivingEntity.level().isClientSide()) {
            if (!pLivingEntity.getPersistentData().contains(NBT_MISSING_LIMBS)) {
                pLivingEntity.getPersistentData().putByte(NBT_MISSING_LIMBS, (byte) 0);
            }
            updateModifiers(pLivingEntity, pLivingEntity.getPersistentData().getByte(NBT_MISSING_LIMBS));
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        if (!(pLivingEntity instanceof Player)) return;
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        if (!pLivingEntity.level().isClientSide()) {
            reassembleEntity(pLivingEntity);
        }
    }

    public static void reassembleEntity(LivingEntity entity) {
        if (entity == null || entity.level().isClientSide()) return;

        entity.getPersistentData().remove(NBT_MISSING_LIMBS);
        entity.getPersistentData().remove(NBT_HEAD_UUID);
        entity.getPersistentData().remove(NBT_HEAD_SYNCED);

        AttributeInstance maxHpAttr = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHpAttr != null) {
            maxHpAttr.removeModifier(HEALTH_MODIFIER_UUID);
        }
        AttributeInstance speedAttr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttr != null) {
            speedAttr.removeModifier(SPEED_MODIFIER_UUID);
        }

        if (entity instanceof Player player) {
            if (player.getPose() == net.minecraft.world.entity.Pose.SWIMMING) {
                player.setPose(net.minecraft.world.entity.Pose.STANDING);
            }
            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                serverPlayer.setCamera(serverPlayer);
            }
        }

        if (entity.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            UUID ownerUUID = entity.getUUID();
            List<Entity> toDiscard = new ArrayList<>();
            float totalHeal = 0.0F;
            for (Entity e : serverLevel.getAllEntities()) {
                if (e instanceof PlayerBodyPartEntity limb) {
                    if (ownerUUID.equals(limb.getPlayerUUID())) {
                        if (limb.isAlive()) {
                            totalHeal += limb.getHealth();
                        }
                        toDiscard.add(limb);
                    }
                }
            }
            for (Entity e : toDiscard) {
                e.discard();
            }
            if (totalHeal > 0.0F) {
                entity.heal(totalHeal);
            }
        }

        ModMessages.INSTANCE.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                new SyncBodyPartsPacket(entity.getId(), (byte) 0, -1)
        );

        entity.level().playSound(null, entity.blockPosition(), net.minecraft.sounds.SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.2F);
        entity.level().playSound(null, entity.blockPosition(), ModSounds.BODY_BREAK.get(), SoundSource.PLAYERS, 0.8F, 1.5F);
    }

    public static void dropAllLimbs(LivingEntity pLivingEntity) {
        if (pLivingEntity.level().isClientSide() || pLivingEntity.isDeadOrDying() || pLivingEntity.isRemoved()) return;
        if (!(pLivingEntity instanceof Player player)) {
            return;
        }

        byte currentMask = player.getPersistentData().getByte(NBT_MISSING_LIMBS);
        if ((currentMask & ALL_LIMBS) == ALL_LIMBS) {
            return;
        }

        if (player.isSleeping()) {
            player.stopSleeping();
        }
        player.setPose(net.minecraft.world.entity.Pose.SWIMMING);

        Level level = player.level();
        UUID uuid = player.getUUID();
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        int headEntityId = -1;

        float stolenHealth = Math.max(1.0F, (float) (player.getAttributeBaseValue(Attributes.MAX_HEALTH) / 6.0));
        float baseYaw = player.getYRot();
        var random = player.getRandom();

        if ((currentMask & RIGHT_ARM) == 0) {
            float armAngle = baseYaw + 75.0F + (random.nextFloat() - 0.5F) * 30.0F;
            double rad = Math.toRadians(armAngle);
            double dirX = -Math.sin(rad);
            double dirZ = Math.cos(rad);
            double speed = 0.32D + random.nextDouble() * 0.10D;
            double upSpeed = 0.28D + random.nextDouble() * 0.08D;

            PlayerArmEntity arm = new PlayerArmEntity(level, x + dirX * 0.25, y + 0.9, z + dirZ * 0.25, uuid, false);
            arm.setOwner(player, RIGHT_ARM, stolenHealth);
            float randomYaw = random.nextFloat() * 360.0F;
            arm.setYRot(randomYaw);
            arm.yRotO = randomYaw;
            arm.setYHeadRot(randomYaw);
            arm.setYBodyRot(randomYaw);
            arm.setDeltaMovement(dirX * speed, upSpeed, dirZ * speed);
            arm.hasImpulse = true;
            level.addFreshEntity(arm);
        }

        if ((currentMask & LEFT_ARM) == 0) {
            float armAngle = baseYaw - 75.0F + (random.nextFloat() - 0.5F) * 30.0F;
            double rad = Math.toRadians(armAngle);
            double dirX = -Math.sin(rad);
            double dirZ = Math.cos(rad);
            double speed = 0.32D + random.nextDouble() * 0.10D;
            double upSpeed = 0.28D + random.nextDouble() * 0.08D;

            PlayerArmEntity arm = new PlayerArmEntity(level, x + dirX * 0.25, y + 0.9, z + dirZ * 0.25, uuid, true);
            arm.setOwner(player, LEFT_ARM, stolenHealth);
            float randomYaw = random.nextFloat() * 360.0F;
            arm.setYRot(randomYaw);
            arm.yRotO = randomYaw;
            arm.setYHeadRot(randomYaw);
            arm.setYBodyRot(randomYaw);
            arm.setDeltaMovement(dirX * speed, upSpeed, dirZ * speed);
            arm.hasImpulse = true;
            level.addFreshEntity(arm);
        }

        if ((currentMask & RIGHT_LEG) == 0) {
            float legAngle = baseYaw + 140.0F + (random.nextFloat() - 0.5F) * 30.0F;
            double rad = Math.toRadians(legAngle);
            double dirX = -Math.sin(rad);
            double dirZ = Math.cos(rad);
            double speed = 0.28D + random.nextDouble() * 0.10D;
            double upSpeed = 0.24D + random.nextDouble() * 0.08D;

            PlayerLegEntity leg = new PlayerLegEntity(level, x + dirX * 0.2, y + 0.4, z + dirZ * 0.2, uuid, false);
            leg.setOwner(player, RIGHT_LEG, stolenHealth);
            float randomYaw = random.nextFloat() * 360.0F;
            leg.setYRot(randomYaw);
            leg.yRotO = randomYaw;
            leg.setYHeadRot(randomYaw);
            leg.setYBodyRot(randomYaw);
            leg.setDeltaMovement(dirX * speed, upSpeed, dirZ * speed);
            leg.hasImpulse = true;
            level.addFreshEntity(leg);
        }

        if ((currentMask & LEFT_LEG) == 0) {
            float legAngle = baseYaw - 140.0F + (random.nextFloat() - 0.5F) * 30.0F;
            double rad = Math.toRadians(legAngle);
            double dirX = -Math.sin(rad);
            double dirZ = Math.cos(rad);
            double speed = 0.28D + random.nextDouble() * 0.10D;
            double upSpeed = 0.24D + random.nextDouble() * 0.08D;

            PlayerLegEntity leg = new PlayerLegEntity(level, x + dirX * 0.2, y + 0.4, z + dirZ * 0.2, uuid, true);
            leg.setOwner(player, LEFT_LEG, stolenHealth);
            float randomYaw = random.nextFloat() * 360.0F;
            leg.setYRot(randomYaw);
            leg.yRotO = randomYaw;
            leg.setYHeadRot(randomYaw);
            leg.setYBodyRot(randomYaw);
            leg.setDeltaMovement(dirX * speed, upSpeed, dirZ * speed);
            leg.hasImpulse = true;
            level.addFreshEntity(leg);
        }

        if ((currentMask & HEAD) == 0) {
            float headAngle = baseYaw + (random.nextFloat() - 0.5F) * 20.0F;
            double rad = Math.toRadians(headAngle);
            double dirX = -Math.sin(rad);
            double dirZ = Math.cos(rad);
            double speed = 0.26D + random.nextDouble() * 0.08D;
            double upSpeed = 0.32D + random.nextDouble() * 0.08D;

            PlayerHeadEntity head = new PlayerHeadEntity(level, x + dirX * 0.15, y + player.getEyeHeight() - 0.2, z + dirZ * 0.15, uuid);
            head.setOwner(player, HEAD, stolenHealth);
            float headYaw = player.getYRot();
            head.setYRot(headYaw);
            head.yRotO = headYaw;
            head.setYHeadRot(headYaw);
            head.setYBodyRot(headYaw);
            head.setDeltaMovement(dirX * speed, upSpeed, dirZ * speed);
            head.hasImpulse = true;
            level.addFreshEntity(head);
            headEntityId = head.getId();
            player.getPersistentData().putUUID(NBT_HEAD_UUID, head.getUUID());
            player.getPersistentData().putBoolean(NBT_HEAD_SYNCED, true);
        }

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.POOF, x, y + 0.8, z, 15, 0.3, 0.4, 0.3, 0.05);
        }

        level.playSound(null, player.blockPosition(), ModSounds.BODY_BREAK.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

        player.getPersistentData().putByte(NBT_MISSING_LIMBS, ALL_LIMBS);
        updateModifiers(player, ALL_LIMBS);

        ModMessages.INSTANCE.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                new SyncBodyPartsPacket(player.getId(), ALL_LIMBS, headEntityId)
        );
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!(pLivingEntity instanceof Player)) return;
        if (!pLivingEntity.level().isClientSide() && !pLivingEntity.isDeadOrDying() && !pLivingEntity.isRemoved()) {
            byte mask = pLivingEntity.getPersistentData().getByte(NBT_MISSING_LIMBS);
            if ((mask & ALL_LIMBS) != ALL_LIMBS) {
                dropAllLimbs(pLivingEntity);
            }
        }

        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
