package net.shule.shulespotions.Events;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.Items.custom.PotionLiquidBottleItem;
import net.shule.shulespotions.Potions.IngredientStat;
import net.shule.shulespotions.Potions.ItemStatRegistry;
import net.shule.shulespotions.ShulesPotions;

import java.util.Map;

@Mod.EventBusSubscriber(
        modid = ShulesPotions.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT)

public class ModEventBusEvents {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        AlchemistMonocleTooltip(event);
        ComingSoonTooltip(event);

    }




    @SubscribeEvent
    public static void OnToolTipRender(RenderTooltipEvent.Color event){
        ItemStack stack = event.getItemStack();

        if(stack.getItem() instanceof PotionLiquidBottleItem){
            event.setBackground(0xEE130000);
            event.setBorderEnd(0xFF000000);
            event.setBorderStart(0xFFd00e0e);
        }
    }



    private static void ComingSoonTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        boolean flg =
                stack.is(ModItems.MANDRAKE_SEED.get()) ||
                stack.is(ModItems.POTION_HOMUNCULUS.get()) ||
                stack.is(ModItems.RECIPE_BOOK.get()) ||
                stack.is(ModItems.ONYX.get()) ||
                stack.is(ModItems.EMERALD_DUST.get()) ||
                stack.is(ModItems.IRON_DUST.get()) ||
                stack.is(ModItems.POTION_BARREL.get()) ||
                stack.is(ModItems.ROTTEN_FISH.get());;

        if (flg) {
            event.getToolTip().add(
                    Component.translatable("tooltip.shulespotions.comingsoon")
                            .withStyle(ChatFormatting.YELLOW)
            );
        }

    }

    private static void AlchemistMonocleTooltip(ItemTooltipEvent event){
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (player == null) return;

        if (stack.is(ModItems.ALCHEMIST_MONOCLE.get())) {

            event.getToolTip().add(
                    Component.translatable("tooltip.shulespotions.alchemist_monocle")
                            .withStyle(ChatFormatting.DARK_GRAY)
            );
        }

        boolean hasMonocle = player.getInventory().contains(
                new ItemStack(ModItems.ALCHEMIST_MONOCLE.get())
        );

        if (!hasMonocle) return;



        if (!ItemStatRegistry.hasStats(stack.getItem())) {
            return;
        }

        IngredientStat stats = ItemStatRegistry.get(stack);

        event.getToolTip().add(Component.empty());

        event.getToolTip().add(
                Component.translatable("tooltip.shulespotions.alchemist_monocle_tittle")
                        .withStyle(ChatFormatting.GOLD)
        );


        if(stats.getPurity() != 0) {
            event.getToolTip().add(
                    Component.translatable(
                                    "tooltip.shulespotions.alchemist_monocle_purity",
                                    stats.getPurity()
                            )
                            .withStyle(ChatFormatting.WHITE)
            );
        }

        if(stats.getVitality() != 0) {
            event.getToolTip().add(
                    Component.translatable(
                                    "tooltip.shulespotions.alchemist_monocle_vitality",
                                    stats.getVitality()
                            )
                            .withStyle(ChatFormatting.RED)
            );
        }

        if(stats.getFlavor() != 0) {
            event.getToolTip().add(
                    Component.translatable(
                                    "tooltip.shulespotions.alchemist_monocle_flavor",
                                    stats.getFlavor()
                            )
                            .withStyle(ChatFormatting.DARK_RED)
            );
        }


        if(stats.getStability() != 0) {
            event.getToolTip().add(
                    Component.translatable(
                                    "tooltip.shulespotions.alchemist_monocle_stability",
                                    stats.getStability()
                            )
                            .withStyle(ChatFormatting.AQUA)
            );
        }

        if (stats.getDuration() != 0) {
            int totalSeconds = stats.getDuration() / 20;
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
            event.getToolTip().add(
                    Component.translatable(
                            "tooltip.shulespotions.alchemist_monocle_duration",
                            String.format("%d:%02d", minutes, seconds)
                    ).withStyle(ChatFormatting.GREEN)
            );
        }


        if (!stats.getEffectWeights().isEmpty()) {

            event.getToolTip().add(Component.empty());

            event.getToolTip().add(
                    Component.literal("Effects")
                            .withStyle(ChatFormatting.LIGHT_PURPLE)
            );

            for (Map.Entry<ResourceLocation, Integer> entry :
                    stats.getEffectWeights().entrySet()) {

                MobEffect effect =
                        BuiltInRegistries.MOB_EFFECT.get(entry.getKey());

                if (effect == null) continue;

                int chance = entry.getValue();

                MutableComponent line = Component.literal(
                                chance + "% "
                        ).withStyle(ChatFormatting.GRAY)

                        .append(
                                Component.translatable(effect.getDescriptionId())
                                        .withStyle(style -> style.withColor(effect.getColor()))
                        );

                event.getToolTip().add(line);
            }
        }

    }

}
