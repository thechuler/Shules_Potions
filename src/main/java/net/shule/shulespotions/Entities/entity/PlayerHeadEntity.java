package net.shule.shulespotions.Entities.entity;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.shule.shulespotions.Entities.ModEntities;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerHeadEntity extends PlayerBodyPartEntity {

    private boolean locallyControlled = false;

    public PlayerHeadEntity(EntityType<? extends PlayerHeadEntity> entityType, Level level) {
        super(entityType, level);
        this.setMaxUpStep(0.6F);
    }

    public PlayerHeadEntity(Level level, double x, double y, double z, @Nullable UUID playerUUID) {
        this(ModEntities.PLAYER_HEAD.get(), level);
        this.setPos(x, y, z);
        this.setPlayerUUID(playerUUID);
    }

    public void setLocallyControlled(boolean locallyControlled) {
        this.locallyControlled = locallyControlled;
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return this.locallyControlled || super.isControlledByLocalInstance();
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps, boolean teleport) {
        if (this.locallyControlled) {
            return;
        }
        super.lerpTo(x, y, z, yRot, xRot, steps, teleport);
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            LivingEntity owner = this.getOwnerEntity();
            if (owner != null && owner.isAlive() && !owner.isRemoved()) {
                if (this.level() != owner.level()) {
                    owner.removeEffect(ModMobEffects.BODY_BREAK_DOWN.get());
                    return;
                }

                double distance = this.distanceTo(owner);
                if (distance > 30.0D) {
                    Vec3 pullDir = owner.position().subtract(this.position()).normalize();
                    this.setDeltaMovement(this.getDeltaMovement().add(pullDir.scale(0.12D)));
                }

                if (distance > 45.0D) {
                    this.teleportTo(owner.getX(), owner.getY() + 0.5D, owner.getZ());
                    this.setDeltaMovement(Vec3.ZERO);
                }
            }
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.25F;
    }
}
