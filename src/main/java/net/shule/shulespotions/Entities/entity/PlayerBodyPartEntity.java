package net.shule.shulespotions.Entities.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.shule.shulespotions.Messages.Custom.SyncBodyPartsPacket;
import net.shule.shulespotions.Messages.ModMessages;
import net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public abstract class PlayerBodyPartEntity extends LivingEntity {

    private static final EntityDataAccessor<Optional<UUID>> PLAYER_UUID =
            SynchedEntityData.defineId(PlayerBodyPartEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<String> OWNER_TYPE =
            SynchedEntityData.defineId(PlayerBodyPartEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Byte> LIMB_TYPE =
            SynchedEntityData.defineId(PlayerBodyPartEntity.class, EntityDataSerializers.BYTE);

    private float stolenMaxHealth = 3.33F;
    private boolean handledReattachOrDeath = false;

    public PlayerBodyPartEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PLAYER_UUID, Optional.empty());
        this.entityData.define(OWNER_TYPE, "");
        this.entityData.define(LIMB_TYPE, (byte) 0);
    }

    public void setPlayerUUID(@Nullable UUID uuid) {
        this.entityData.set(PLAYER_UUID, Optional.ofNullable(uuid));
    }

    @Nullable
    public UUID getPlayerUUID() {
        return this.entityData.get(PLAYER_UUID).orElse(null);
    }

    public void setOwnerType(String type) {
        this.entityData.set(OWNER_TYPE, type);
    }

    public String getOwnerType() {
        return this.entityData.get(OWNER_TYPE);
    }

    public void setLimbType(byte limbType) {
        this.entityData.set(LIMB_TYPE, limbType);
    }

    public byte getLimbType() {
        return this.entityData.get(LIMB_TYPE);
    }

    public void setStolenMaxHealth(float health) {
        this.stolenMaxHealth = health;
    }

    public float getStolenMaxHealth() {
        return this.stolenMaxHealth;
    }

    public void setOwner(LivingEntity owner, byte limbType, float stolenHealth) {
        if (owner instanceof PlayerBodyPartEntity) return;
        this.setPlayerUUID(owner.getUUID());
        this.setOwnerType(EntityType.getKey(owner.getType()).toString());
        this.setLimbType(limbType);
        this.setStolenMaxHealth(stolenHealth);

        var maxHp = this.getAttribute(Attributes.MAX_HEALTH);
        if (maxHp != null) {
            maxHp.setBaseValue(stolenHealth);
        }
        this.setHealth(stolenHealth);
    }

    @Nullable
    public LivingEntity getOwnerEntity() {
        UUID uuid = this.getPlayerUUID();
        if (uuid == null) return null;
        if (this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(uuid);
            if (entity instanceof LivingEntity living) {
                if (living instanceof PlayerBodyPartEntity) return null;
                return living;
            }
        }
        return this.level().getPlayerByUUID(uuid);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            LivingEntity owner = this.getOwnerEntity();

            if (owner != null) {
                if (owner.isDeadOrDying() || (!owner.isRemoved() && !owner.hasEffect(ModMobEffects.BODY_BREAK_DOWN.get()))) {
                    this.discard();
                    return;
                }

                byte mask = owner.getPersistentData().getByte(BodyBreakDownEffect.NBT_MISSING_LIMBS);
                if ((mask & this.getLimbType()) == 0) {
                    this.discard();
                    return;
                }
            }

            if (this.level().getBlockState(this.blockPosition()).is(Blocks.NETHER_PORTAL)
                    || this.level().getBlockState(this.blockPosition()).is(Blocks.END_PORTAL)) {
                if (owner != null) {
                    owner.removeEffect(ModMobEffects.BODY_BREAK_DOWN.get());
                } else {
                    this.discard();
                }
            }
        }
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);

        if (!this.level().isClientSide()) {
            if (this.handledReattachOrDeath) return;
            this.handledReattachOrDeath = true;

            LivingEntity owner = this.getOwnerEntity();
            if (owner != null && owner.isAlive()) {
                byte limbBit = this.getLimbType();
                if (limbBit == BodyBreakDownEffect.HEAD) {
                    owner.removeEffect(ModMobEffects.BODY_BREAK_DOWN.get());
                }
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, 0.0D, 0.7D));
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeAffected(net.minecraft.world.effect.MobEffectInstance effect) {
        return false;
    }

    @Override
    public boolean startRiding(Entity vehicle, boolean force) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("PlayerUUID")) {
            this.setPlayerUUID(tag.getUUID("PlayerUUID"));
        }
        if (tag.contains("OwnerType")) {
            this.setOwnerType(tag.getString("OwnerType"));
        }
        if (tag.contains("LimbType")) {
            this.setLimbType(tag.getByte("LimbType"));
        }
        if (tag.contains("StolenMaxHealth")) {
            this.stolenMaxHealth = tag.getFloat("StolenMaxHealth");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        UUID uuid = this.getPlayerUUID();
        if (uuid != null) {
            tag.putUUID("PlayerUUID", uuid);
        }
        tag.putString("OwnerType", this.getOwnerType());
        tag.putByte("LimbType", this.getLimbType());
        tag.putFloat("StolenMaxHealth", this.stolenMaxHealth);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
