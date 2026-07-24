package net.shule.shulespotions.MobEffects.Custom;


import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(modid = ShulesPotions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LeechEffect extends MobEffect   {
    public LeechEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }


    @SubscribeEvent
    public static void effect(LivingDamageEvent event){
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker))
            return;

        if(!attacker.hasEffect(ModMobEffects.LEECH.get())) return;
        float damage = event.getAmount();

        int amplifier = attacker.getEffect(ModMobEffects.LEECH.get()).getAmplifier();

        float heal = damage * (0.39f + amplifier * 0.10f);

        attacker.heal(heal);
    }
}
