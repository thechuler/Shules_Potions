package net.shule.shulespotions.Blocks;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Blocks.Custom.Mortar;
import net.shule.shulespotions.Blocks.Entities.*;
import net.shule.shulespotions.ShulesPotions;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ShulesPotions.MODID);



    public static final RegistryObject<BlockEntityType<PotionCauldronBE>> POTION_CAULDRON_BE =
            BLOCK_ENTITIES.register("potion_cauldron_be", () ->
                    BlockEntityType.Builder.of(PotionCauldronBE::new,
                            ModBlocks.POTION_CAULDRON.get(),
                                    ModBlocks.COPPER_CAULDRON.get()).
                            build(null));



    public static final RegistryObject<BlockEntityType<SmallPotionBlockBE>>
            SMALL_POTION_BE =
            BLOCK_ENTITIES.register(
                    "small_potion_be",
                    () -> BlockEntityType.Builder.of(
                            SmallPotionBlockBE::new,
                            ModBlocks.SMALL_POTION_BLOCK.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<MortarBE>>
            MORTAR_BE =
            BLOCK_ENTITIES.register(
                    "mortar_be",
                    () -> BlockEntityType.Builder.of(
                            MortarBE::new,
                            ModBlocks.MORTAR.get()
                    ).build(null)
            );


    public static final RegistryObject<BlockEntityType<PotionSplashBE>>
            POTION_SPLASH_BE =
            BLOCK_ENTITIES.register(
                    "potion_splash_be",
                    () -> BlockEntityType.Builder.of(
                            PotionSplashBE::new,
                            ModBlocks.POTION_SPLASH.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<SpoonRackBE>>
            SPOON_RACK_BE =
            BLOCK_ENTITIES.register(
                    "spoon_rack_be",
                    () -> BlockEntityType.Builder.of(
                            SpoonRackBE::new,
                            ModBlocks.SPOON_RACK.get()
                    ).build(null)
            );




    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
