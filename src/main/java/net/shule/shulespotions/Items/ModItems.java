package net.shule.shulespotions.Items;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Items.custom.PotionBarrel;
import net.shule.shulespotions.Items.custom.RecipeBook;
import net.shule.shulespotions.Items.custom.SmallPotion;
import net.shule.shulespotions.Items.custom.PotionRecipeItem;
import net.shule.shulespotions.ShulesPotions;
import net.shule.shulespotions.util.CauldronActions.AddItemAction;

import java.util.List;


/*Esta es una de las clases mas importantes. Aca es donde le vamos a "avisar" a minecraft
que van a existir items nuevos.
Aca podemos configurar 2 cosas importantes.

1-La id del item (osea el "nombre" que lo diferencia del resto)

2-Las propiedades del item (ya sea si es o no comestible, cuanto puede stackear, su calidad, etc)
 */


public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ShulesPotions.MODID);


    public static final RegistryObject<Item> SPEED_RECIPE = ITEMS.register("speed_recipe",
            () -> new PotionRecipeItem(
                    new Item.Properties().stacksTo(1).rarity(Rarity.RARE),
                    ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID, "sp_potion_speed")));



    public static final RegistryObject<Item> SMALL_POTION = ITEMS.register("small_potion",
            ()-> new SmallPotion(new Item.Properties()));


    public static final RegistryObject<Item> POTION_BARREL = ITEMS.register("potion_barrel",
        ()-> new PotionBarrel(new Item.Properties()));

    public static final RegistryObject<Item> RECIPE_BOOK = ITEMS.register("recipe_book",
            ()-> new RecipeBook(new Item.Properties()));

    public static final RegistryObject<Item> WOODEN_SPOON = ITEMS.register("wooden_spoon",
            ()-> new SwordItem(Tiers.WOOD,2,2,new Item.Properties()));



    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
