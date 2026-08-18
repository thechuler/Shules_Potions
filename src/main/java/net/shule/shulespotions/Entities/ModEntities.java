package net.shule.shulespotions.Entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Entities.Projectile.PotionSplashProjectile;
import net.shule.shulespotions.Entities.Projectile.ThrowablePotionBottleProjectile;
import net.shule.shulespotions.Entities.entity.PlayerCloneEntity;
import net.shule.shulespotions.Entities.entity.ShadowClonEntity;
import net.shule.shulespotions.ShulesPotions;

import java.util.function.Supplier;



@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ShulesPotions.MODID);


    public static final RegistryObject<EntityType<PotionSplashProjectile>> POTION_SPLASH_PROJECTILE =
            ENTITIES.register("potion_splash_projectile",
                    () -> EntityType.Builder.<PotionSplashProjectile>of(
                            PotionSplashProjectile::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("potion_splash_projectile"));

    public static final RegistryObject<EntityType<ThrowablePotionBottleProjectile>> THROWABLE_POTION_BOTTLE_PROJECTILE =
            ENTITIES.register("throwable_potion_bottle_projectile",
                    () -> EntityType.Builder.<ThrowablePotionBottleProjectile>of(
                                    ThrowablePotionBottleProjectile::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("throwable_potion_bottle_projectile"));

    public static final RegistryObject<EntityType<ShadowClonEntity>> SHADOW_CLONE =
            ENTITIES.register("shadow_clone",
                    () -> EntityType.Builder
                            .of(ShadowClonEntity::new, MobCategory.MISC)
                            .sized(0.6F, 1.95F)
                            .clientTrackingRange(8)
                            .build("shadow_clone"));


    public static final RegistryObject<EntityType<PlayerCloneEntity>> PLAYER_CLONE =
            ENTITIES.register("player_clone",
                    () -> EntityType.Builder
                            .of(PlayerCloneEntity::new, MobCategory.CREATURE)
                            .sized(0.6F, 1.8F)
                            .clientTrackingRange(10)
                            .build("player_clone"));

    public static final RegistryObject<EntityType<net.shule.shulespotions.Entities.entity.SpinningBlockEntity>> SPINNING_BLOCK =
            ENTITIES.register("spinning_block",
                    () -> EntityType.Builder
                            .<net.shule.shulespotions.Entities.entity.SpinningBlockEntity>of(net.shule.shulespotions.Entities.entity.SpinningBlockEntity::new, MobCategory.MISC)
                            .sized(0.98F, 0.98F)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build("spinning_block"));


    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
         //   FrogManEntity.init();
           // NitroMoscaEntity.init();

        });
    }


    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
       event.put(SHADOW_CLONE.get(), ShadowClonEntity.createAttributes().build());
       event.put(PLAYER_CLONE.get(), PlayerCloneEntity.createAttributes().build());
    //    event.put(NITRO_FLY.get(), NitroMoscaEntity.createAttributes().build());
    }

    @SubscribeEvent
    public  static void  RegistrarLugardeSpawn(SpawnPlacementRegisterEvent event){

    //    event.register(InicializarEntidades.FROGMAN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.WORLD_SURFACE,FrogManEntity::PuedeSpawnear,RegisterSpawnPlacementsEvent.Operation.OR);
      //  event.register(InicializarEntidades.NITRO_FLY.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NitroMoscaEntity::PuedeSpawnear,RegisterSpawnPlacementsEvent.Operation.REPLACE);


    }

}
