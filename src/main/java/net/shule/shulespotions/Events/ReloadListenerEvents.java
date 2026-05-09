package net.shule.shulespotions.Events;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.Potions.IngredientStatLoader;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(
        modid = ShulesPotions.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ReloadListenerEvents {

    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent event) {
        event.addListener(new IngredientStatLoader());
    }
}