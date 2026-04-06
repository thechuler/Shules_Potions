package net.shule.shulespotions.Items;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Items.custom.PotionRecipeItem;
import net.shule.shulespotions.ShulesPotions;
import net.shule.shulespotions.util.CauldronActions.AddItemAction;
import net.shule.shulespotions.util.CauldronActions.CauldronAction;

import java.util.ArrayList;
import java.util.List;


/*Esta es una de las clases mas importantes. Aca es donde le vamos a "avisar" a minecraft
que van a existir items nuevos.
Aca podemos configurar 2 cosas importantes.

1-La id del item (osea el "nombre" que lo diferencia del resto)

2-Las propiedades del item (ya sea si es o no comestible, cuanto puede stackear, su calidad, etc)
 */


public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ShulesPotions.MODID);


    public static final RegistryObject<Item> HASTE_RECIPE = ITEMS.register("haste_recipe",
            () -> new PotionRecipeItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE),
                    List.of(
                            new AddItemAction(new ItemStack(Items.WATER_BUCKET)),
                            new AddItemAction(new ItemStack(Items.STONE)),
                            new AddItemAction(new ItemStack(Items.LAVA_BUCKET)),
                            new AddItemAction(new ItemStack(Items.STONE)),
                            new AddItemAction(new ItemStack(Items.AMETHYST_SHARD))
                    ),
                    MobEffects.DIG_SPEED));


    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
