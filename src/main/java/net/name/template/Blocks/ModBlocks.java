package net.name.template.Blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.name.template.Template;
import net.name.template.Items.ModItems;

import java.util.function.Supplier;


/*
Aca es donde vamos a agregar nuestros bloques, si te fijas es muy similar a la clase de items.
Tambien podemos hacer las dos cosas iniciales. Por un lado, ponerle un id, y por el  otro
propiedades
 */

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Template.MODID);


/*
    public static final RegistryObject<Block> THERIUM_ORE = registerBlock("therium_ore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE))
    );
*/





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
