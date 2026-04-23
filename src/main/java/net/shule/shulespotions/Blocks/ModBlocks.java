package net.shule.shulespotions.Blocks;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.shule.shulespotions.Blocks.Custom.PotionCauldron;
import net.shule.shulespotions.Blocks.Custom.RecipeLectern;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.ShulesPotions;

import java.util.function.Supplier;


/*
Aca es donde vamos a agregar nuestros bloques, si te fijas es muy similar a la clase de items.
Tambien podemos hacer las dos cosas iniciales. Por un lado, ponerle un id, y por el  otro
propiedades
 */

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ShulesPotions.MODID);



    public static final RegistryObject<Block> POTION_CAULDRON = registerBlock("potion_cauldron",
            () -> new PotionCauldron(BlockBehaviour.Properties.copy(Blocks.CAULDRON)));



    public static final RegistryObject<Block> RECIPE_LECTERN = registerBlock("recipe_lectern",
            () -> new RecipeLectern(BlockBehaviour.Properties.copy(Blocks.LECTERN)));





    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        RegisterBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> RegisterBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }

}
