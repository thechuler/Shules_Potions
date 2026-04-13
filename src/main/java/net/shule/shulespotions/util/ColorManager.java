package net.shule.shulespotions.util;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.Items.custom.PotionLiquidContainerItem;
import net.shule.shulespotions.Potions.PotionLiquid;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ColorManager {


    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {

        event.register((stack, tintIndex) -> {
                    if (tintIndex == 1) {


                        int[] colors = {
                                0xFF0000,
                                0x00FF00,
                                0x0000FF
                        };

                        float time = (System.currentTimeMillis() % 3000) / 3000f;
                        return ColorManager.lerpColorsLoop(colors, time);
                    }
                    return 0xFFFFFFFF;
                }, ModItems.SMALL_POTION.get());


        event.register((stack, tintIndex) -> {
            if (tintIndex == 1) {

                if (!(stack.getItem() instanceof PotionLiquidContainerItem potionContainer)) {
                    return 0x00000000;
                }

                return !potionContainer.hasPotionLiquid(stack)
                        ? 0x00000000
                        : 0xFF000000 | potionContainer.getPotionLiquid(stack).getColor();
            }

            return 0x00FFFFFF;
        }, ModItems.POTION_BARREL.get());


    }



    public static int lerpColorsLoop(int[] colors, float t) {
        if (colors == null || colors.length == 0) {
            return 0xFFFFFF;
        }

        float scaled = t * colors.length;
        int index = (int) scaled;
        float localT = scaled - index;

        int colorA = colors[index % colors.length];
        int colorB = colors[(index + 1) % colors.length];

        return lerpColor(colorA, colorB, localT);
    }


    public static int lerpColor(int colorA, int colorB, float t) {
        int rA = (colorA >> 16) & 0xFF;
        int gA = (colorA >> 8) & 0xFF;
        int bA = colorA & 0xFF;

        int rB = (colorB >> 16) & 0xFF;
        int gB = (colorB >> 8) & 0xFF;
        int bB = colorB & 0xFF;

        int r = (int)(rA + (rB - rA) * t);
        int g = (int)(gA + (gB - gA) * t);
        int b = (int)(bA + (bB - bA) * t);

        return (r << 16) | (g << 8) | b;
    }





}
