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


import net.shule.shulespotions.Entities.ModEntities;
import net.shule.shulespotions.Fluids.ModFluidTypes;
import net.shule.shulespotions.Fluids.ModFluids;

import net.shule.shulespotions.Items.ModCreativeTab;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.Messages.ModMessages;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.Particles.ModParticles;
import net.shule.shulespotions.Sounds.ModSounds;
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
        ModEntities.ENTITIES.register(modEventBus);
        ModParticles.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModFluids.register(modEventBus);
        ModFluidTypes.register(modEventBus);
        ModMobEffects.register(modEventBus);
        ModMessages.register();
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



}
