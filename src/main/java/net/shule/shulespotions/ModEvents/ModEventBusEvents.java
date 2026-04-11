package net.shule.shulespotions.ModEvents;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.Items.custom.SmallPotion;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(
        modid = ShulesPotions.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT)

public class ModEventBusEvents {

    @SubscribeEvent
    public static void OnToolTipRender(RenderTooltipEvent.Color event){
        ItemStack stack = event.getItemStack();

        if(stack.getItem() instanceof SmallPotion){
            event.setBackground(0xEE130000);
            event.setBorderEnd(0xFF000000);
            event.setBorderStart(0xFFd00e0e);
        }
    }

}
