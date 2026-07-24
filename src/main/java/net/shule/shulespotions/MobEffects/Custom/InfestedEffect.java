package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(modid = ShulesPotions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InfestedEffect extends MobEffect {
    public InfestedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }



    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event){

        LivingEntity entity = event.getEntity();

        if (!entity.hasEffect(ModMobEffects.INFESTED.get()))
            return;


        Level level = entity.level();

        if(level.isClientSide)
            return;


        int amplifier = entity
                .getEffect(ModMobEffects.INFESTED.get())
                .getAmplifier();


        int amount = entity.getRandom().nextInt(1, 3) + amplifier;


        for(int i = 0; i < amount; i++) {

            Silverfish silverfish =
                    new Silverfish(EntityType.SILVERFISH, level);


            silverfish.moveTo(
                    entity.getX(),
                    entity.getY(),
                    entity.getZ(),
                    entity.getRandom().nextFloat() * 360F,
                    0
            );


            level.addFreshEntity(silverfish);
        }
    }
}
