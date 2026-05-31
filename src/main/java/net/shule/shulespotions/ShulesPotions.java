package net.shule.shulespotions;


import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.shule.shulespotions.Blocks.ModBlockEntities;
import net.shule.shulespotions.Blocks.ModBlocks;


import net.shule.shulespotions.Fluids.ModFluidTypes;
import net.shule.shulespotions.Fluids.ModFluids;
import net.shule.shulespotions.Fluids.PotionFluidHelper;
import net.shule.shulespotions.Items.ModCreativeTab;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.Items.custom.PotionLiquidBottleItem;
import net.shule.shulespotions.Items.custom.RecipeScroll;
import net.shule.shulespotions.Particles.Custom.BubbleProvider;
import net.shule.shulespotions.Particles.Custom.PotionSplashProvider;
import net.shule.shulespotions.Particles.ModParticles;
import net.shule.shulespotions.util.CauldronActions.AddIngredientAction;
import net.shule.shulespotions.util.CauldronActions.CauldronActionRegistry;
import net.shule.shulespotions.util.CauldronActions.StirAction;




@Mod(net.shule.shulespotions.ShulesPotions.MODID)
public class ShulesPotions {
    public static final String MODID = "shulespotions";


    public ShulesPotions(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModParticles.register(modEventBus);
        ModFluids.register(modEventBus);
        ModFluidTypes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

        event.enqueueWork(() -> {

            CauldronActionRegistry.register("add_ingredient",
                    AddIngredientAction::load
            );

            CauldronActionRegistry.register(
                    "stir",
                    StirAction::load
            );

        });
    }


    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }


    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void  registerParticles(RegisterParticleProvidersEvent event){

            event.registerSpriteSet(ModParticles.BUBBLE.get(), BubbleProvider::new);
            event.registerSpriteSet(ModParticles.POTION_SPLASH.get(), PotionSplashProvider::new);


        }



        @SubscribeEvent
        public static void registerItemColors(RegisterColorHandlersEvent.Item event) {

            event.register((stack, tintIndex) -> {
                //-----------------------SCROLL-------------------//
                        if (stack.getItem() instanceof RecipeScroll) {

                            if (tintIndex == 1) {
                                if (RecipeScroll.hasRecipeData(stack)) {

                                    FluidStack fluid = RecipeScroll.getPotionFluid(stack);

                                    if (!fluid.isEmpty()) {
                                        return PotionFluidHelper
                                                .getPotionLiquid(fluid)
                                                .getColor();
                                    }
                                }

                                return 0xFFFFFF;
                            }
                        }


                        //-----------------------------BOTTLES-------------------------
                        if (tintIndex == 1) {

                            if (stack.getItem() instanceof PotionLiquidBottleItem bottle) {

                                FluidStack fluid = bottle.getFluid(stack);

                                if (!fluid.isEmpty()) {
                                    return PotionFluidHelper
                                            .getPotionLiquid(fluid)
                                            .getColor();
                                }
                            }
                        }
                        return -1;

                    },
                    ModItems.SMALL_POTION_BOTTLE.get(),
                    ModItems.LARGE_POTION_BOTTLE.get(),
                    ModItems.BIG_POTION_BOTTLE.get(),
                    ModItems.RECIPE_SCROLL.get()
            );
        }




        private static final ItemPropertyFunction FILL_LEVEL =
                (stack, level, entity, seed) -> {

                    if (stack.getItem() instanceof PotionLiquidBottleItem bottle) {

                        FluidStack fluid = bottle.getFluid(stack);

                        if (fluid.isEmpty()) {
                            return 0.0F;
                        }

                        return Mth.clamp(
                                (float) fluid.getAmount() / bottle.capacity,
                                0.0F,
                                1.0F
                        );
                    }

                    return 0.0F;
                };



        private static void registerBottle(Item item) {

            ItemProperties.register(
                    item,
                    ResourceLocation.parse("fill_level"),
                    FILL_LEVEL
            );
        }



        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {


            ItemProperties.register(
                    ModItems.RECIPE_SCROLL.get(),
                    ResourceLocation.parse("written"),
                    (stack, level, entity, seed) -> {
                        return RecipeScroll.hasRecipeData(stack) ? 1.0F : 0.0F;
                    }
            );


            registerBottle(ModItems.SMALL_POTION_BOTTLE.get());
            registerBottle(ModItems.LARGE_POTION_BOTTLE.get());
            registerBottle(ModItems.BIG_POTION_BOTTLE.get());


            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_POTION_FLUID.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_POTION_FLUID.get(), RenderType.translucent());

        }
    }
}
