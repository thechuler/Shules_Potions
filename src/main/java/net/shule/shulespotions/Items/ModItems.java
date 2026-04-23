package net.shule.shulespotions.Items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
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



    public static final RegistryObject<Item> POTION_BARREL = ITEMS.register("potion_barrel", () -> new PotionBarrel(new Item.Properties()));

    public static final RegistryObject<Item> SMALL_POTION_BOTTLE = ITEMS.register("small_potion_bottle", () -> new PotionLiquidBottleItem(new Item.Properties(),5,10,1));
    public static final RegistryObject<Item> LARGE_POTION_BOTTLE = ITEMS.register("large_potion_bottle", () -> new PotionLiquidBottleItem(new Item.Properties(),15,30,3));
    public static final RegistryObject<Item> BIG_POTION_BOTTLE = ITEMS.register("big_potion_bottle", () -> new PotionLiquidBottleItem(new Item.Properties(),20,30,5));



    public static final RegistryObject<Item> RECIPE_BOOK = ITEMS.register("recipe_book", () -> new RecipeBook(new Item.Properties()));

    public static final RegistryObject<Item> WOODEN_SPOON = ITEMS.register("wooden_spoon", () -> new SwordItem(Tiers.WOOD, 2, 2, new Item.Properties()));

    public static final RegistryObject<Item> ROTTEN_FISH = ITEMS.register("rotten_fish", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EMERALD_DUST = ITEMS.register("emerald_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> IRON_DUST = ITEMS.register("iron_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ONYX = ITEMS.register("onyx", () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> SPEED_RECIPE = registerRecipe("speed", MobEffects.MOVEMENT_SPEED);
    public static final RegistryObject<Item> SLOWNESS_RECIPE = registerRecipe("slowness", MobEffects.MOVEMENT_SLOWDOWN);
    public static final RegistryObject<Item> FIRE_RESISTANCE_RECIPE = registerRecipe("fire_resistance", MobEffects.FIRE_RESISTANCE);

    public static final RegistryObject<Item> WATER_BREATHING_RECIPE = registerRecipe("water_breathing", MobEffects.WATER_BREATHING);
    public static final RegistryObject<Item> LUCK_RECIPE = registerRecipe("luck", MobEffects.LUCK);
    public static final RegistryObject<Item> UNLUCK_RECIPE = registerRecipe("unluck", MobEffects.UNLUCK);
    public static final RegistryObject<Item> HERO_OF_THE_VILLAGE_RECIPE = registerRecipe("hero_of_the_village", MobEffects.HERO_OF_THE_VILLAGE);
    public static final RegistryObject<Item> BAD_OMEN_RECIPE = registerRecipe("bad_omen", MobEffects.BAD_OMEN);
    public static final RegistryObject<Item> HUNGER_RECIPE = registerRecipe("hunger", MobEffects.HUNGER);
    public static final RegistryObject<Item> SATURATION_RECIPE = registerRecipe("saturation", MobEffects.SATURATION);

    public static final RegistryObject<Item> NAUSEA_RECIPE = registerRecipe("nausea", MobEffects.CONFUSION);
    public static final RegistryObject<Item> HEALTH_BOOST_RECIPE = registerRecipe("health_boost", MobEffects.HEALTH_BOOST);
    public static final RegistryObject<Item> REGENERATION_RECIPE = registerRecipe("regeneration", MobEffects.REGENERATION);

    public static final RegistryObject<Item> INSTANT_DAMAGE_RECIPE = registerRecipe("instant_damage", MobEffects.HARM);
    public static final RegistryObject<Item> INSTANT_HEALTH_RECIPE = registerRecipe("instant_health", MobEffects.HEAL);

    public static final RegistryObject<Item> ABSORPTION_RECIPE = registerRecipe("absorption", MobEffects.ABSORPTION);
    public static final RegistryObject<Item> WEAKNESS_RECIPE = registerRecipe("weakness", MobEffects.WEAKNESS);
    public static final RegistryObject<Item> POISON_RECIPE = registerRecipe("poison", MobEffects.POISON);
    public static final RegistryObject<Item> WITHER_RECIPE = registerRecipe("wither", MobEffects.WITHER);

    public static final RegistryObject<Item> DARKNESS_RECIPE = registerRecipe("darkness", MobEffects.DARKNESS);
    public static final RegistryObject<Item> BLINDNESS_RECIPE = registerRecipe("blindness", MobEffects.BLINDNESS);
    public static final RegistryObject<Item> INVISIBILITY_RECIPE = registerRecipe("invisibility", MobEffects.INVISIBILITY);
    public static final RegistryObject<Item> GLOWING_RECIPE = registerRecipe("glowing", MobEffects.GLOWING);

    public static final RegistryObject<Item> NIGHT_VISION_RECIPE = registerRecipe("night_vision", MobEffects.NIGHT_VISION);
    public static final RegistryObject<Item> JUMP_RECIPE = registerRecipe("jump", MobEffects.JUMP);
    public static final RegistryObject<Item> SLOW_FALLING_RECIPE = registerRecipe("slow_falling", MobEffects.SLOW_FALLING);
    public static final RegistryObject<Item> LEVITATION_RECIPE = registerRecipe("levitation", MobEffects.LEVITATION);

    public static final RegistryObject<Item> DIG_SPEED_RECIPE = registerRecipe("dig_speed", MobEffects.DIG_SPEED);
    public static final RegistryObject<Item> DIG_SLOWDOWN_RECIPE = registerRecipe("dig_slowdown", MobEffects.DIG_SLOWDOWN);
    public static final RegistryObject<Item> DAMAGE_RESISTANCE_RECIPE = registerRecipe("damage_resistance", MobEffects.DAMAGE_RESISTANCE);


    private static RegistryObject<Item> registerRecipe(String name, MobEffect effect) {
        return ITEMS.register(name + "_recipe", () -> new RecipeSheetItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID, name + "_recipe"), effect.getColor()));
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
