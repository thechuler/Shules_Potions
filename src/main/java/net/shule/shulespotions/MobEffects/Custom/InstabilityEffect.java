package net.shule.shulespotions.MobEffects.Custom;



import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class InstabilityEffect extends MobEffect {

    public InstabilityEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {

        int interval = Math.max(4, 20 - amplifier * 3);
        return duration % interval == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {

        RandomSource random = entity.getRandom();


        Vec3 direction = new Vec3(
                random.nextDouble() * 2.0 - 1.0,
                (random.nextDouble() - 0.5) * 0.4,
                random.nextDouble() * 2.0 - 1.0
        ).normalize();


        double strength = 0.35 + amplifier * 0.30;

        entity.push(
                direction.x * strength,
                direction.y * strength,
                direction.z * strength
        );

        entity.hurtMarked = true;
    }
}