package net.shule.shulespotions.Blocks;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;
import net.shule.shulespotions.ShulesPotions;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ShulesPotions.MODID);



    public static final RegistryObject<BlockEntityType<PotionCauldronBE>> POTION_CAULDRON_BE =
            BLOCK_ENTITIES.register("potion_cauldron_be", () ->
                    BlockEntityType.Builder.of(PotionCauldronBE::new,
                            ModBlocks.caldero.get()).build(null));



    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
