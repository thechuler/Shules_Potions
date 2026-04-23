package net.shule.shulespotions;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
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
import net.shule.shulespotions.Items.custom.PotionLiquidBottleItem;
import net.shule.shulespotions.Items.custom.RecipeSheetItem;
import net.shule.shulespotions.Particles.Custom.BubbleProvider;
import net.shule.shulespotions.Particles.ModParticles;
import net.shule.shulespotions.Recipes.ModRecipes;


/*Este Archivo es el MAIN, o sea el mas importante. En el no vamos a hacer mucho
principalmente conexiones a otros archivos. Este es el que "se levanta primero y
despierta a los demas".
Literalmente este archivo es el primero que Minecraft ejecuta y sirve como conexion para
el resto
 */


@Mod(net.shule.shulespotions.ShulesPotions.MODID)
public class ShulesPotions {

    /*Este es tu MODID O Identificador, (una forma bonita de decir que es el dni de tu mod
    Como en la vida real sirve para que si hay mas mods, puedas identificar al tuyo
    (y para muchas cosas mas que vamos a ver mas adelante)
    */
    public static final String MODID = "shulespotions";


    public ShulesPotions(FMLJavaModLoadingContext context) {

        //Aca es donde vamos a hacer las conexiones con el resto de los archivos.


        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus); //<---Esto agrega items a tu mod
        ModBlocks.register(modEventBus); // <--- Esto agrega bloques
        ModCreativeTab.register(modEventBus); //<--- Esto les da un inventario en creativo
        ModBlockEntities.register(modEventBus);
        ModRecipes.register(modEventBus); // Agrega PotionRecipe type y serializer
        ModMenus.register(modEventBus);
        ModParticles.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }


    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }


    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {


        @SubscribeEvent
        public static void  registerParticles(RegisterParticleProvidersEvent event){
            event.registerSpriteSet(
                    ModParticles.BUBBLE.get(),
                    BubbleProvider::new
            );
        }



        @SubscribeEvent
        public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
            event.register((stack, tintIndex) -> {
                if (tintIndex == 1) {
                    if (stack.getItem() instanceof PotionLiquidBottleItem bottle) {
                        if (bottle.hasPotionLiquid(stack)) {
                            return bottle.getPotionLiquid(stack).getColor();
                        }
                    }
                }
                return -1;
            }, ModItems.SMALL_POTION_BOTTLE.get(),
                    ModItems.LARGE_POTION_BOTTLE.get(),
                    ModItems.BIG_POTION_BOTTLE.get());
        }



        private static final ItemPropertyFunction HAS_LIQUID = (stack, level, entity, seed) -> {
            if (stack.getItem() instanceof PotionLiquidBottleItem bottle) {
                return bottle.getLiquidLevel(stack) > 0 ? 1.0F : 0.0F;
            }
            return 0.0F;
        };

        private static void registerBottle(Item item) {
            ItemProperties.register(item,
                    ResourceLocation.parse("has_liquid"),
                    HAS_LIQUID
            );
        }



        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {



            event.enqueueWork(() -> {
                MenuScreens.register(ModMenus.RECIPE_BOOK_MENU.get(), RecipeBookScreen::new);
            });


            registerBottle(ModItems.SMALL_POTION_BOTTLE.get());
            registerBottle(ModItems.LARGE_POTION_BOTTLE.get());
            registerBottle(ModItems.BIG_POTION_BOTTLE.get());





            event.enqueueWork(() -> {
                Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
                            if (tintIndex == 1) {
                                if (stack.getItem() instanceof RecipeSheetItem rs) {
                                    return rs.getColor();
                                }
                            }
                            return -1;
                        },
                        ModItems.SPEED_RECIPE.get(),
                        ModItems.SLOWNESS_RECIPE.get(),
                        ModItems.FIRE_RESISTANCE_RECIPE.get(),

                        ModItems.WATER_BREATHING_RECIPE.get(),
                        ModItems.LUCK_RECIPE.get(),
                        ModItems.UNLUCK_RECIPE.get(),
                        ModItems.HERO_OF_THE_VILLAGE_RECIPE.get(),
                        ModItems.BAD_OMEN_RECIPE.get(),
                        ModItems.HUNGER_RECIPE.get(),
                        ModItems.SATURATION_RECIPE.get(),

                        ModItems.NAUSEA_RECIPE.get(),
                        ModItems.HEALTH_BOOST_RECIPE.get(),
                        ModItems.REGENERATION_RECIPE.get(),

                        ModItems.INSTANT_DAMAGE_RECIPE.get(),
                        ModItems.INSTANT_HEALTH_RECIPE.get(),

                        ModItems.ABSORPTION_RECIPE.get(),
                        ModItems.WEAKNESS_RECIPE.get(),
                        ModItems.POISON_RECIPE.get(),
                        ModItems.WITHER_RECIPE.get(),

                        ModItems.DARKNESS_RECIPE.get(),
                        ModItems.BLINDNESS_RECIPE.get(),
                        ModItems.INVISIBILITY_RECIPE.get(),
                        ModItems.GLOWING_RECIPE.get(),

                        ModItems.NIGHT_VISION_RECIPE.get(),
                        ModItems.JUMP_RECIPE.get(),
                        ModItems.SLOW_FALLING_RECIPE.get(),
                        ModItems.LEVITATION_RECIPE.get(),

                        ModItems.DIG_SPEED_RECIPE.get(),
                        ModItems.DIG_SLOWDOWN_RECIPE.get(),
                        ModItems.DAMAGE_RESISTANCE_RECIPE.get()
                );
            });

        }
    }
}
