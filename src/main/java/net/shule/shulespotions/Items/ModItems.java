package net.shule.shulespotions.Items;

import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.ShulesPotions;


/*Esta es una de las clases mas importantes. Aca es donde le vamos a "avisar" a minecraft
que van a existir items nuevos.
Aca podemos configurar 2 cosas importantes.

1-La id del item (osea el "nombre" que lo diferencia del resto)

2-Las propiedades del item (ya sea si es o no comestible, cuanto puede stackear, su calidad, etc)
 */


public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ShulesPotions.MODID);





    public static final RegistryObject<Item> Test = ITEMS.register("test",
            () -> new PotionItem(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON)));




    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
