package net.shule.shulespotions.Events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.MobEffects.Custom.BodyBreakDownEffect;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(modid = ShulesPotions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BodyBreakDownEvents {

    private static byte getMissingLimbs(LivingEntity entity) {
        if (entity != null && entity.getPersistentData().contains(BodyBreakDownEffect.NBT_MISSING_LIMBS)) {
            return entity.getPersistentData().getByte(BodyBreakDownEffect.NBT_MISSING_LIMBS);
        }
        return 0;
    }

    private static boolean isEffectActive(LivingEntity entity) {
        if (entity == null) return false;
        return entity.hasEffect(ModMobEffects.BODY_BREAK_DOWN.get()) || getMissingLimbs(entity) != 0;
    }

    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (event.getEffectInstance().getEffect() == ModMobEffects.BODY_BREAK_DOWN.get()) {
            if (!(event.getEntity() instanceof Player)) {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (isEffectActive(player)) {
            if (player.getPose() != Pose.SLEEPING && player.getPose() != Pose.DYING) {
                player.setPose(Pose.SWIMMING);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (isEffectActive(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (isEffectActive(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (isEffectActive(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        if (isEffectActive(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (isEffectActive(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        if (isEffectActive(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (isEffectActive(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onContainerOpen(PlayerContainerEvent.Open event) {
        Player player = event.getEntity();
        if (isEffectActive(player)) {
            player.closeContainer();
        }
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        Player player = event.getPlayer();
        if (isEffectActive(player)) {
            event.setCanceled(true);
            if (!event.getEntity().getItem().isEmpty()) {
                player.getInventory().add(event.getEntity().getItem());
            }
        }
    }
}
