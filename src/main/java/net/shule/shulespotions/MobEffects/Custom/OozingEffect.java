package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(
        modid = ShulesPotions.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class OozingEffect extends MobEffect {


    public OozingEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }


    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {

        LivingEntity entity = event.getEntity();


        if (!entity.hasEffect(ModMobEffects.OOZING.get()))
            return;


        int amplifier = entity
                .getEffect(ModMobEffects.OOZING.get())
                .getAmplifier();



        int amount = entity.getRandom().nextInt(2, 4) + amplifier;


        Level level = entity.level();


        if(level.isClientSide)
            return;


        for(int i = 0; i < amount; i++) {

            Slime slime = new Slime(net.minecraft.world.entity.EntityType.SLIME, level);


            slime.moveTo(
                    entity.getX(),
                    entity.getY(),
                    entity.getZ(),
                    entity.getRandom().nextFloat() * 360F,
                    0
            );



            slime.setSize(
                    1 + entity.getRandom().nextInt(2),
                    true
            );


            level.addFreshEntity(slime);
        }
    }
}