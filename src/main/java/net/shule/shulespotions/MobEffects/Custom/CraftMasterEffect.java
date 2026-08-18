package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.ShulesPotions;
import net.shule.shulespotions.MobEffects.ModMobEffects;

@Mod.EventBusSubscriber(modid = ShulesPotions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CraftMasterEffect extends MobEffect {
    public CraftMasterEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @SubscribeEvent
    public static void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getEntity();

        MobEffect craftMaster = ModMobEffects.CRAFT_MASTER.get();
        if (player.hasEffect(craftMaster)) {
            MobEffectInstance effectInstance = player.getEffect(craftMaster);

            int amplifier = effectInstance != null ? effectInstance.getAmplifier() : 0;

            double chance = 0.05 + (amplifier * 0.05);

            Container inventory = event.getInventory();
            boolean savedAny = false;

            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (!stack.isEmpty()) {
                    if (player.getRandom().nextDouble() < chance) {
                        ItemStack returnedItem = stack.copy();
                        returnedItem.setCount(1);
                        
                        if (!player.level().isClientSide()) {
                            player.drop(returnedItem, false);
                            savedAny = true;
                        }
                    }
                }
            }


            if (savedAny && !player.level().isClientSide()) {
                player.level().playSound(null, player.blockPosition(), SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 0.5f, 1.2f);

                ServerLevel serverLevel = (ServerLevel) player.level();
                serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, 
                        player.getX(), player.getY() + 1.0, player.getZ(), 
                        10, 0.4, 0.4, 0.4, 0.0);
            }
        }
    }
}
