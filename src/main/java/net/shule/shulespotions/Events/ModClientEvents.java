package net.shule.shulespotions.Events;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.shule.shulespotions.Blocks.Entities.PotionSplashBE;
import net.shule.shulespotions.Blocks.Entities.SpoonRackBE;
import net.shule.shulespotions.Blocks.ModBlockEntities;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.Blocks.Renders.MortarRender;
import net.shule.shulespotions.Blocks.Renders.PotionCauldronRenderer;
import net.shule.shulespotions.Blocks.Renders.SpoonRackRenderer;
import net.shule.shulespotions.Entities.ModEntities;
import net.shule.shulespotions.Entities.Models.PotionSplashProjectileModel;
import net.shule.shulespotions.Entities.Renders.PotionSplashProjectileRenderer;
import net.shule.shulespotions.Entities.Renders.ShadowClonRenderer;
import net.shule.shulespotions.Fluids.ModFluids;
import net.shule.shulespotions.Fluids.PotionFluidHelper;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.Items.custom.PotionLiquidBottleItem;
import net.shule.shulespotions.Items.custom.RecipeScroll;
import net.shule.shulespotions.Particles.Custom.BubbleProvider;
import net.shule.shulespotions.Particles.Custom.InstabilityBubbleProvider;
import net.shule.shulespotions.Particles.Custom.PotionExplotionProvider;
import net.shule.shulespotions.Particles.ModParticles;
import net.shule.shulespotions.Renders.CloneOverlayLayer;
import net.shule.shulespotions.Renders.CoveredOnPotionLiquidLayer;
import net.shule.shulespotions.Renders.ButterFingersLayer;
import net.shule.shulespotions.ShulesPotions;


@Mod.EventBusSubscriber(modid = ShulesPotions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                ModBlockEntities.POTION_CAULDRON_BE.get(),
                PotionCauldronRenderer::new
        );
        event.registerBlockEntityRenderer(
                ModBlockEntities.SPOON_RACK_BE.get(),
                SpoonRackRenderer::new
        );

        event.registerBlockEntityRenderer(
                ModBlockEntities.MORTAR_BE.get(),
                MortarRender::new
        );
        event.registerEntityRenderer(
                ModEntities.POTION_SPLASH_PROJECTILE.get(),
                PotionSplashProjectileRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.THROWABLE_POTION_BOTTLE_PROJECTILE.get(),
                ThrownItemRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.SHADOW_CLONE.get(),
                ShadowClonRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.PLAYER_CLONE.get(),
                net.shule.shulespotions.Entities.Renders.PlayerCloneRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.SPINNING_BLOCK.get(),
                net.shule.shulespotions.Entities.Renders.SpinningBlockRenderer::new
        );
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(
            EntityRenderersEvent.RegisterLayerDefinitions event) {

        event.registerLayerDefinition(
                PotionSplashProjectileModel.LAYER_LOCATION,
                PotionSplashProjectileModel::createBodyLayer
        );
    }

    @SubscribeEvent
    public static void  registerParticles(RegisterParticleProvidersEvent event){

        event.registerSpriteSet(ModParticles.BUBBLE.get(), BubbleProvider::new);
        event.registerSpriteSet(ModParticles.POTION_EXPLOTION.get(), PotionExplotionProvider::new);
        event.registerSpriteSet(ModParticles.INSTABILITY_BUBBLE.get(), InstabilityBubbleProvider::new);
        event.registerSpriteSet(ModParticles.CORRUPTION_FIRE.get(), net.shule.shulespotions.Particles.Custom.CorruptionFireProvider::new);

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
                                            .getPotionLiquid(fluid).getStats()
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
                                        .getPotionLiquid(fluid).getStats()
                                        .getColor();
                            }
                        }
                    }
                    return -1;

                },
                ModItems.SMALL_POTION_BOTTLE.get(),
                ModItems.LARGE_POTION_BOTTLE.get(),
                ModItems.BIG_POTION_BOTTLE.get(),
                ModItems.THROWABLE_POTION_BOTTLE.get(),
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
        registerBottle(ModItems.THROWABLE_POTION_BOTTLE.get());


        ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_POTION_FLUID.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_POTION_FLUID.get(), RenderType.translucent());

    }


    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {

        event.register(
                (state, level, pos, tintIndex) -> {

                    if (level == null || pos == null)
                        return 0xFFFFFF;

                    BlockEntity be = level.getBlockEntity(pos);

                    if (be instanceof PotionSplashBE splash) {
                        return splash.getColor();
                    }

                    return 0xFFFFFF;
                },
                ModBlocks.POTION_SPLASH.get()
        );
    }


    @SubscribeEvent
    public static void addLayer(EntityRenderersEvent.AddLayers event) {

        BuiltInRegistries.ENTITY_TYPE.forEach(type -> {

            EntityRenderer<?> renderer = event.getEntityRenderer(type);

            if (renderer instanceof LivingEntityRenderer<?, ?> livingRenderer) {
                livingRenderer.addLayer(
                        new CoveredOnPotionLiquidLayer(livingRenderer)
                );

                livingRenderer.addLayer(
                        new CloneOverlayLayer(livingRenderer)
                );

                livingRenderer.addLayer(
                        new ButterFingersLayer(livingRenderer)
                );
            }
        });

        for (String skin : event.getSkins()) {
            PlayerRenderer playerRenderer = event.getSkin(skin);

            playerRenderer.addLayer(
                    new CoveredOnPotionLiquidLayer<>(playerRenderer)
            );

            playerRenderer.addLayer(
                    new CloneOverlayLayer<>(playerRenderer)
            );

            playerRenderer.addLayer(
                    new ButterFingersLayer<>(playerRenderer)
            );
        }
    }

    @Mod.EventBusSubscriber(modid = net.shule.shulespotions.ShulesPotions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeEvents {
        private static final Map<UUID, Integer> migraineStartTicks = new HashMap<>();

        private static boolean scaleHead(EntityModel<?> model, float scale) {
            boolean headFound = false;

            if (model instanceof HumanoidModel<?> humanoid) {
                humanoid.head.xScale = scale;
                humanoid.head.yScale = scale;
                humanoid.head.zScale = scale;
                
                humanoid.hat.xScale = scale;
                humanoid.hat.yScale = scale;
                humanoid.hat.zScale = scale;
                headFound = true;
            }
            

            Class<?> clazz = model.getClass();
            while (clazz != null && clazz != Object.class) {
                for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                    if (field.getType() == net.minecraft.client.model.geom.ModelPart.class) {
                        try {
                            field.setAccessible(true);
                            net.minecraft.client.model.geom.ModelPart part = (net.minecraft.client.model.geom.ModelPart) field.get(model);
                            if (part != null) {
                                if (part.hasChild("head")) {
                                    net.minecraft.client.model.geom.ModelPart head = part.getChild("head");
                                    head.xScale = scale;
                                    head.yScale = scale;
                                    head.zScale = scale;
                                    headFound = true;
                                }
                                if (part.hasChild("hat")) {
                                    net.minecraft.client.model.geom.ModelPart hat = part.getChild("hat");
                                    hat.xScale = scale;
                                    hat.yScale = scale;
                                    hat.zScale = scale;
                                    headFound = true;
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }
            
            return headFound;
        }

        @SubscribeEvent
        public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
            LivingEntity entity = event.getEntity();
            EntityModel<?> model = event.getRenderer().getModel();
            float targetScale = 1.0f;
            boolean hasEffect = false;
            
            boolean hasMigraineLocally = entity.hasEffect(ModMobEffects.MIGRAINE.get());
            boolean hasMigraineSynced = entity.getPersistentData().getBoolean("HasMigraine");
            
            if (hasMigraineLocally || hasMigraineSynced) {
                int amplifier;
                int startTick;
                
                if (hasMigraineLocally) {
                    amplifier = entity.getEffect(ModMobEffects.MIGRAINE.get()).getAmplifier();
                    startTick = migraineStartTicks.computeIfAbsent(entity.getUUID(), k -> entity.tickCount);
                } else {
                    amplifier = entity.getPersistentData().getInt("MigraineAmplifier");
                    startTick = entity.getPersistentData().getInt("MigraineStartTick");
                }
                
                int activeTicks = entity.tickCount - startTick;
                float growthRate = 0.005f * (amplifier + 1);
                targetScale = 1.0f + (activeTicks * growthRate);
                hasEffect = true;
            } else {
                migraineStartTicks.remove(entity.getUUID());
            }

            boolean hasDecapitatedLocally = entity.hasEffect(ModMobEffects.DECAPITATED.get());
            boolean hasDecapitatedSynced = entity.getPersistentData().getBoolean("HasDecapitated");

            if (hasDecapitatedLocally || hasDecapitatedSynced) {
                targetScale = 0.0f;
                hasEffect = true;
            }

            if (hasEffect) {
                scaleHead(model, targetScale);
            }
        }

        @SubscribeEvent
        public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
            EntityModel<?> model = event.getRenderer().getModel();
            scaleHead(model, 1.0f);
        }
    }
}
