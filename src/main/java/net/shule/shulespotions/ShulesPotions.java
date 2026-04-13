package net.shule.shulespotions;


import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.shule.shulespotions.Blocks.ModBlockEntities;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.Guis.ModMenus;
import net.shule.shulespotions.Guis.Screens.RecipeBookScreen;
import net.shule.shulespotions.Items.ModCreativeTab;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.Items.custom.PotionLiquidContainerItem;
import net.shule.shulespotions.Recipes.ModRecipes;


/*Este Archivo es el MAIN, o sea el mas importante. En el no vamos a hacer mucho
principalmente conexiones a otros archivos. Este es el que "se levanta primero y
despierta a los demas".
Literalmente este archivo es el primero que Minecraft ejecuta y sirve como conexion para
el resto
 */


@Mod(net.shule.shulespotions.ShulesPotions.MODID)
public class ShulesPotions
{

    /*Este es tu MODID O Identificador, (una forma bonita de decir que es el dni de tu mod
    Como en la vida real sirve para que si hay mas mods, puedas identificar al tuyo
    (y para muchas cosas mas que vamos a ver mas adelante)
    */
    public static final String MODID = "shulespotions";




    public ShulesPotions(FMLJavaModLoadingContext context)
    {

    //Aca es donde vamos a hacer las conexiones con el resto de los archivos.



        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus); //<---Esto agrega items a tu mod
        ModBlocks.register(modEventBus); // <--- Esto agrega bloques
        ModCreativeTab.register(modEventBus); //<--- Esto les da un inventario en creativo
        ModBlockEntities.register(modEventBus);
        ModRecipes.register(modEventBus); // Agrega PotionRecipe type y serializer
        ModMenus.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }


    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }




    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            event.enqueueWork(() -> {
                MenuScreens.register(ModMenus.RECIPE_BOOK_MENU.get(), RecipeBookScreen::new);
            });

            event.enqueueWork(() -> {

                ItemProperties.register(
                        ModItems.POTION_BARREL.get(),
                        ResourceLocation.fromNamespaceAndPath("shulestpotions","has_liquid"),
                        (stack, level, entity, seed) -> {

                            if (stack.getItem() instanceof PotionLiquidContainerItem container) {
                                return container.hasPotionLiquid(stack) ? 1.0F : 0.0F;
                            }

                            return 0.0F;
                        }
                );

            });

        }
    }
}
