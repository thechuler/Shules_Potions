package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.ShulesPotions;


@Mod.EventBusSubscriber(
        modid = ShulesPotions.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class EnderDisruptionEffect extends MobEffect {


    public EnderDisruptionEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }


    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {


        if (!(event.getRayTraceResult() instanceof EntityHitResult hit))
            return;


        Entity target = hit.getEntity();


        if (!(target instanceof LivingEntity entity))
            return;



        if (!entity.hasEffect(ModMobEffects.ENDER_DISRUPTION.get()))
            return;



        if (!event.getProjectile().getType()
                .getDescriptionId()
                .contains("arrow"))
            return;


        for (int i = 0; i < 32; i++) {


            double x = entity.getX()
                    + (entity.getRandom().nextDouble() - 0.5D) * 5.0;

            double y = entity.getY();

            double z = entity.getZ()
                    + (entity.getRandom().nextDouble() - 0.5D) * 5.0;



            if (entity.randomTeleport(x, y, z, true)) {



                event.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);


                return;
            }
        }
    }
}