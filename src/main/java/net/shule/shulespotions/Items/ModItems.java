package net.shule.shulespotions.Items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Fluids.ModFluids;
import net.shule.shulespotions.Items.custom.*;
import net.shule.shulespotions.ShulesPotions;


/*Esta es una de las clases mas importantes. Aca es donde le vamos a "avisar" a minecraft
que van a existir items nuevos.
Aca podemos configurar 2 cosas importantes.

1-La id del item (osea el "nombre" que lo diferencia del resto)

2-Las propiedades del item (ya sea si es o no comestible, cuanto puede stackear, su calidad, etc)
 */


public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ShulesPotions.MODID);



    public static final RegistryObject<Item> POTION_BARREL = ITEMS.register(
            "potion_barrel", () -> new BucketItem(ModFluids.SOURCE_SOAP_WATER
                    ,new Item.Properties()));

    public static final RegistryObject<Item> SMALL_POTION_BOTTLE = ITEMS.register("small_potion_bottle", () -> new PotionLiquidBottleItem(new Item.Properties(),5,10,1));
    public static final RegistryObject<Item> LARGE_POTION_BOTTLE = ITEMS.register("large_potion_bottle", () -> new PotionLiquidBottleItem(new Item.Properties(),15,30,3));
    public static final RegistryObject<Item> BIG_POTION_BOTTLE = ITEMS.register("big_potion_bottle", () -> new PotionLiquidBottleItem(new Item.Properties(),20,30,5));



    public static final RegistryObject<Item> RECIPE_BOOK = ITEMS.register("recipe_book", () -> new RecipeBook(new Item.Properties()));

    public static final RegistryObject<Item> WOODEN_SPOON = ITEMS.register("wooden_spoon", () -> new SwordItem(Tiers.WOOD, 2, 2, new Item.Properties()));

    public static final RegistryObject<Item> ROTTEN_FISH = ITEMS.register("rotten_fish", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EMERALD_DUST = ITEMS.register("emerald_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> IRON_DUST = ITEMS.register("iron_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ONYX = ITEMS.register("onyx", () -> new Item(new Item.Properties()));





    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
