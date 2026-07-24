package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.Entities.ModEntities;
import net.shule.shulespotions.Entities.entity.ShadowClonEntity;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(
        modid = ShulesPotions.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ShadowManipulationEffect extends MobEffect {
    public ShadowManipulationEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        Entity entity = event.getSource().getEntity();
        if(event.getEntity().level().isClientSide()) return;
        if(!(entity instanceof LivingEntity attacker)) return;
        if(!attacker.hasEffect(ModMobEffects.SHADOW_MANIPULATION.get())) return;
        if(attacker instanceof ShadowClonEntity) return;

        MobEffectInstance effect = attacker.getEffect(ModMobEffects.SHADOW_MANIPULATION.get());
        spawnClones(attacker, event.getEntity(), effect.getAmplifier());


    }
    public static void spawnClones(LivingEntity attacker, LivingEntity target, int amplifier) {

        ServerLevel level = (ServerLevel) attacker.level();

        int amount = 1 + attacker.getRandom().nextInt(2) + amplifier;

        for(int i = 0; i < amount; i++) {

            ShadowClonEntity clone = ModEntities.SHADOW_CLONE.get().create(level);

            if(clone == null)
                continue;

           clone.setTarget(target);
           clone.setDamage(5 + amplifier);
           clone.setHealthMax(5 + amplifier);

            double angle = attacker.getRandom().nextDouble() * Math.PI * 2;
            double radius = 1.5 + amplifier;

            double x = attacker.getX() + Math.cos(angle) * radius;
            double z = attacker.getZ() + Math.sin(angle) * radius;
            double y = attacker.getY();

            clone.moveTo(
                    x,
                    y,
                    z,
                    attacker.getYRot(),
                    attacker.getXRot()
            );

            level.addFreshEntity(clone);
        }
    }
}
