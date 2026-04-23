package net.shule.shulespotions.Events;

import net.minecraftforge.fml.common.Mod;
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
