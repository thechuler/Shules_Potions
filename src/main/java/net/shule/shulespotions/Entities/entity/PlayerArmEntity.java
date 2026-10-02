package net.shule.shulespotions.Entities.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.shule.shulespotions.Entities.ModEntities;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerArmEntity extends PlayerBodyPartEntity {

    private static final EntityDataAccessor<Boolean> IS_LEFT_ARM =
            SynchedEntityData.defineId(PlayerArmEntity.class, EntityDataSerializers.BOOLEAN);

    public PlayerArmEntity(EntityType<? extends PlayerArmEntity> entityType, Level level) {
        super(entityType, level);
    }

    public PlayerArmEntity(Level level, double x, double y, double z, @Nullable UUID playerUUID, boolean isLeftArm) {
        this(ModEntities.PLAYER_ARM.get(), level);
        this.setPos(x, y, z);
        this.setPlayerUUID(playerUUID);
        this.setLeftArm(isLeftArm);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_LEFT_ARM, false);
    }

    public void setLeftArm(boolean isLeft) {
        this.entityData.set(IS_LEFT_ARM, isLeft);
    }

    public boolean isLeftArm() {
        return this.entityData.get(IS_LEFT_ARM);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("IsLeftArm")) {
            this.setLeftArm(tag.getBoolean("IsLeftArm"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsLeftArm", this.isLeftArm());
    }
}
