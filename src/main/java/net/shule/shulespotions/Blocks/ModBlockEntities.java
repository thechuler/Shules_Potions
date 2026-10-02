package net.shule.shulespotions.Blocks;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Blocks.Entities.AncientRedstoneClockBE;
import net.shule.shulespotions.Blocks.Entities.*;
import net.shule.shulespotions.ShulesPotions;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ShulesPotions.MODID);



    public static final RegistryObject<BlockEntityType<PotionCauldronBE>> POTION_CAULDRON_BE =
            BLOCK_ENTITIES.register("potion_cauldron_be", () ->
                    BlockEntityType.Builder.of(PotionCauldronBE::new,
                            ModBlocks.POTION_CAULDRON.get(),
                                    ModBlocks.BIG_CAULDRON.get(),
                                    ModBlocks.COPPER_CAULDRON.get(),
                                    ModBlocks.METEOR_CAULDRON.get()).
                            build(null));





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

    public static final RegistryObject<BlockEntityType<AncientPedestalBE>>
            ANCIENT_PEDESTAL_BE =
            BLOCK_ENTITIES.register(
                    "ancient_pedestal_be",
                    () -> BlockEntityType.Builder.of(
                            AncientPedestalBE::new,
                            ModBlocks.ANCIENT_PEDESTAL.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<ElectionAncientPedestalBE>>
            ELECTION_ANCIENT_PEDESTAL_BE =
            BLOCK_ENTITIES.register(
                    "election_ancient_pedestal_be",
                    () -> BlockEntityType.Builder.of(
                            ElectionAncientPedestalBE::new,
                            ModBlocks.ELECTION_ANCIENT_PEDESTAL.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<OminousPedestalBE>>
            OMINOUS_PEDESTAL_BE =
            BLOCK_ENTITIES.register(
                    "ominous_pedestal_be",
                    () -> BlockEntityType.Builder.of(
                            OminousPedestalBE::new,
                            ModBlocks.OMINOUS_PEDESTAL.get()
                    ).build(null)
            );


    public static final RegistryObject<BlockEntityType<SpitterTrapBE>>
            SPITTER_TRAP_BE =
            BLOCK_ENTITIES.register(
                    "spitter_trap_be",
                    () -> BlockEntityType.Builder.of(
                            SpitterTrapBE::new,
                            ModBlocks.SPITTER_TRAP.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<AncientRedstoneClockBE>>
            ANCIENT_REDSTONE_CLOCK_BE =
            BLOCK_ENTITIES.register(
                    "ancient_redstone_clock_be",
                    () -> BlockEntityType.Builder.of(
                            AncientRedstoneClockBE::new,
                            ModBlocks.ANCIENT_REDSTONE_CLOCK.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<SpikeBE>>
            SPIKE_BE =
            BLOCK_ENTITIES.register(
                    "spike_be",
                    () -> BlockEntityType.Builder.of(
                            SpikeBE::new,
                            ModBlocks.SPIKE.get()
                    ).build(null)
            );





    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
