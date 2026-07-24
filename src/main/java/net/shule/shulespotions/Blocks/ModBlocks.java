package net.shule.shulespotions.Blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.shule.shulespotions.Blocks.Custom.*;
import net.shule.shulespotions.Fluids.ModFluids;
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
            () -> new PotionCauldron(BlockBehaviour.Properties.copy(Blocks.CAULDRON),5,1000));

    public static final RegistryObject<Block> COPPER_CAULDRON = registerBlock("copper_cauldron",
            () -> new PotionCauldron(BlockBehaviour.Properties.copy(Blocks.CAULDRON),7,1000));

    /*
    public static final RegistryObject<Block> BIG_CAULDRON = registerBlock("big_cauldron",
            () -> new PotionCauldron(BlockBehaviour.Properties.copy(Blocks.CAULDRON),10,1000));


*/


    public static final RegistryObject<Block> POTIONSHELF = registerBlock("potionshelf",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BOOKSHELF)));

    public static final RegistryObject<Block> MORTAR = registerBlock("mortar",
            () -> new Mortar(BlockBehaviour.Properties.copy(Blocks.STONE)));

/*
    public static final RegistryObject<Block> ALCHEMIST_WOOD = registerBlock("alchemist_wood",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.ACACIA_WOOD)));
*/
    public static final RegistryObject<Block> POTION_SPLASH = registerBlock("potion_splash",
            () -> new PotionSplashBlock(BlockBehaviour.Properties.copy(Blocks.SLIME_BLOCK)));

    public static final RegistryObject<Block> SPOON_RACK = registerBlock("spoon_rack",
            () -> new SpoonRack(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL)));




    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        RegisterBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> RegisterBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    public static final RegistryObject<LiquidBlock> POTION_FLUID_BLOCK = BLOCKS.register("potion_fluid_block",
            ()-> new LiquidBlock(ModFluids.SOURCE_POTION_FLUID,BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }

}
