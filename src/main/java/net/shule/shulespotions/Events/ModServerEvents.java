package net.shule.shulespotions.Events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.Player.PlayerRecipesProvider;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(modid = ShulesPotions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModServerEvents {
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            event.addCapability(
                    ResourceLocation.fromNamespaceAndPath("shulespotions", "player_recipes"),
                    new PlayerRecipesProvider()
            );
        }
    }



    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        event.getOriginal().getCapability(PlayerRecipesProvider.PLAYER_RECIPES).ifPresent(oldCap -> {
            event.getEntity().getCapability(PlayerRecipesProvider.PLAYER_RECIPES).ifPresent(newCap -> {
                newCap.getRecipes().addAll(oldCap.getRecipes());
            });
        });
    }
}
