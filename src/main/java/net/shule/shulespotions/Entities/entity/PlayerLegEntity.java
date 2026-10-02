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

public class PlayerLegEntity extends PlayerBodyPartEntity {

    private static final EntityDataAccessor<Boolean> IS_LEFT_LEG =
            SynchedEntityData.defineId(PlayerLegEntity.class, EntityDataSerializers.BOOLEAN);

    public PlayerLegEntity(EntityType<? extends PlayerLegEntity> entityType, Level level) {
        super(entityType, level);
    }

    public PlayerLegEntity(Level level, double x, double y, double z, @Nullable UUID playerUUID, boolean isLeftLeg) {
        this(ModEntities.PLAYER_LEG.get(), level);
        this.setPos(x, y, z);
        this.setPlayerUUID(playerUUID);
        this.setLeftLeg(isLeftLeg);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_LEFT_LEG, false);
    }

    public void setLeftLeg(boolean isLeft) {
        this.entityData.set(IS_LEFT_LEG, isLeft);
    }

    public boolean isLeftLeg() {
        return this.entityData.get(IS_LEFT_LEG);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("IsLeftLeg")) {
            this.setLeftLeg(tag.getBoolean("IsLeftLeg"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsLeftLeg", this.isLeftLeg());
    }
}
