package net.shule.shulespotions.ModEvents;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(modid = ShulesPotions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModServerEvents {
 /*  @SubscribeEvent
    public static void OnEntityHurt(LivingHurtEvent event){
        if (event.getEntity().level().isClientSide) return;

        Entity attacker = event.getSource().getEntity();
        float amount = event.getAmount();

       if (attacker instanceof LivingEntity entity) {

           if (entity.hasEffect(ModMobEffects.LEECH.get())) {

               if (entity instanceof Player player) {
                   if(player.getHealth() < player.getMaxHealth()){
                       //seguir aca chule del futuro
                   }

                   float current = player.getAbsorptionAmount();
                   player.setAbsorptionAmount(current +amount * 10);
               }


           }
    }


}
*/
}
