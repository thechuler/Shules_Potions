package net.shule.shulespotions.Events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(modid = ShulesPotions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CameraShakeHandler {

    private static int shakeTicks = 0;
    private static float shakeIntensity = 0f;

    public static void triggerShake(double x, double y, double z, int ticks, float intensity) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            double distanceSq = player.distanceToSqr(x, y, z);
            double maxDistance = 16.0; // 16 blocks radius
            if (distanceSq <= maxDistance * maxDistance) {
                // Closer to the explosion = more intensity
                float distanceFactor = (float) (1.0 - (Math.sqrt(distanceSq) / maxDistance));
                shakeTicks = ticks;
                shakeIntensity = intensity * distanceFactor;
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (shakeTicks > 0) {
                shakeTicks--;
            }
        }
    }

    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        if (shakeTicks > 0 && Minecraft.getInstance().level != null) {
            RandomSource random = Minecraft.getInstance().level.getRandom();
            float shakeX = (random.nextFloat() - 0.5f) * shakeIntensity * 2.0f; // Multiplier to make it more noticeable
            float shakeY = (random.nextFloat() - 0.5f) * shakeIntensity * 2.0f;
            float shakeRoll = (random.nextFloat() - 0.5f) * shakeIntensity * 2.0f;

            event.setPitch(event.getPitch() + shakeX);
            event.setYaw(event.getYaw() + shakeY);
            event.setRoll(event.getRoll() + shakeRoll);
        }
    }
}
