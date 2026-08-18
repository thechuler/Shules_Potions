package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(modid = ShulesPotions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FlyEffect extends MobEffect {
    public FlyEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }


    @SubscribeEvent
    public static void OnEffectAdded(MobEffectEvent.Added event){
        if(event.getEntity() instanceof ServerPlayer player){
            if(event.getEffectInstance().getEffect() == ModMobEffects.FLY.get()){
                player.getAbilities().mayfly = true;
                player.onUpdateAbilities();
            }
        }
    }

    @SubscribeEvent
    public static void onEffectRemove(MobEffectEvent.Remove event) {
        if (event.getEntity() instanceof ServerPlayer player
                && event.getEffect() == ModMobEffects.FLY.get()) {

            if (!player.isCreative() && !player.isSpectator()) {
                player.getAbilities().flying = false;
                player.getAbilities().mayfly = false;
                player.onUpdateAbilities();
            }
        }
    }




}
