package net.shule.shulespotions.Entities.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class ShadowClonEntity extends PathfinderMob {
    public ShadowClonEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }



    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide)
            return;

        LivingEntity target = getTarget();

        if (target == null || !target.isAlive()) {
            disappear();
            return;
        }


        getLookControl().setLookAt(target, 30.0F, 30.0F);


        getNavigation().moveTo(target, 1.5D);


        spawnIdleParticles();


        if (distanceToSqr(target) < 4.0D) {
            attackTarget(target);
        }
    }

    private void attackTarget(LivingEntity target) {

        doHurtTarget(target);

        disappear();
    }

    private void disappear() {

        spawnDisappearParticles();

        discard();
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();

        if(level() instanceof ServerLevel server){
            server.sendParticles(
                    ParticleTypes.LARGE_SMOKE,
                    getX(),
                    getY() + 1,
                    getZ(),
                    25,
                    0.3,
                    0.5,
                    0.3,
                    0.02
            );
        }
    }

    private void spawnDisappearParticles(){

        if(level() instanceof ServerLevel server){

            server.sendParticles(
                    ParticleTypes.LARGE_SMOKE,
                    getX(),
                    getY()+1,
                    getZ(),
                    30,
                    0.4,
                    0.5,
                    0.4,
                    0.05
            );
        }
    }

    private void spawnIdleParticles(){

        if(tickCount % 2 != 0)
            return;

        if(level() instanceof ServerLevel server){

            server.sendParticles(
                    ParticleTypes.SMOKE,
                    getX(),
                    getY()+1,
                    getZ(),
                    2,
                    0.15,
                    0.25,
                    0.15,
                    0
            );
        }
    }



    public void setDamage(double damage) {
        AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);

        if (attackDamage != null) {
            attackDamage.setBaseValue(damage);
        }
    }

    public void setHealthMax(double maxHealth) {
        AttributeInstance healthAttribute = this.getAttribute(Attributes.MAX_HEALTH);

        if (healthAttribute != null) {
            healthAttribute.setBaseValue(maxHealth);
            this.setHealth((float) maxHealth);
        }
    }



}
