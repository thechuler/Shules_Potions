package net.shule.shulespotions.Items;


import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Fluids.ModFluids;
import net.shule.shulespotions.Items.custom.*;
import net.shule.shulespotions.ShulesPotions;
import net.shule.shulespotions.util.CauldronActions.StirToolType;




public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ShulesPotions.MODID);



    public static final RegistryObject<Item> POTION_BARREL = ITEMS.register(
            "potion_barrel", () -> new BucketItem(ModFluids.SOURCE_POTION_FLUID
                    ,new Item.Properties()));

    public static final RegistryObject<Item> SMALL_POTION_BOTTLE = ITEMS.register("small_potion_bottle", () -> new PotionLiquidBottleItem(new Item.Properties().stacksTo(1),5,250));

   public static final RegistryObject<Item> LARGE_POTION_BOTTLE = ITEMS.register("large_potion_bottle", () -> new PotionLiquidBottleItem(new Item.Properties().stacksTo(1),10,250*2));

    public static final RegistryObject<Item> BIG_POTION_BOTTLE = ITEMS.register("big_potion_bottle", () -> new PotionLiquidBottleItem(new Item.Properties().stacksTo(1),15,250*3));

    public static final RegistryObject<Item> RECIPE_BOOK = ITEMS.register("recipe_book", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOODEN_SPOON = ITEMS.register("wooden_spoon", () -> new SpoonItem(StirToolType.WOOD,new Item.Properties().stacksTo(1),"wooden"));

    public static final RegistryObject<Item> GOLDEN_SPOON = ITEMS.register("golden_spoon", () -> new SpoonItem(StirToolType.GOLD,new Item.Properties().stacksTo(1),"golden"));

    public static final RegistryObject<Item> DIAMOND_SPOON = ITEMS.register("diamond_spoon", () -> new SpoonItem(StirToolType.DIAMOND,new Item.Properties().stacksTo(1),"diamond"));

    public static final RegistryObject<Item> STONE_SPOON = ITEMS.register("stone_spoon", () -> new SpoonItem(StirToolType.IRON,new Item.Properties().stacksTo(1),"iron"));

    public static final RegistryObject<Item> IRON_SPOON = ITEMS.register("iron_spoon", () -> new SpoonItem(StirToolType.STONE,new Item.Properties().stacksTo(1),"stone"));


    public static final RegistryObject<Item> RECIPE_SCROLL = ITEMS.register("recipe_scroll", () -> new RecipeScroll(new Item.Properties()));

    public static final RegistryObject<Item> ALCHEMIST_MONOCLE = ITEMS.register("alchemist_monocle", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> MANDRAKE_SEED = ITEMS.register("mandrake_seed", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> ROTTEN_APPLE = ITEMS.register("rotten_apple", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> GHAST_HEART = ITEMS.register("ghast_heart", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> POTION_HOMUNCULUS = ITEMS.register("potion_homunculus", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> ROTTEN_FISH = ITEMS.register("rotten_fish", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EMERALD_DUST = ITEMS.register("emerald_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> IRON_DUST = ITEMS.register("iron_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ONYX = ITEMS.register("onyx", () -> new Item(new Item.Properties()));


    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
