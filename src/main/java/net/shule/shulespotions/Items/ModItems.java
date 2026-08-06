package net.shule.shulespotions.Items;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Fluids.ModFluids;
import net.shule.shulespotions.Items.custom.*;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.ShulesPotions;
import net.shule.shulespotions.util.CauldronActions.StirToolType;

import java.util.HashMap;
import java.util.Map;




public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ShulesPotions.MODID);



    public static final RegistryObject<Item> POTION_BARREL = ITEMS.register(
            "potion_barrel", () -> new BucketItem(ModFluids.SOURCE_POTION_FLUID
                    ,new Item.Properties()));

    public static final RegistryObject<Item> SMALL_POTION_BOTTLE = ITEMS.register("small_potion_bottle", () -> new PotionLiquidBottleItem(new Item.Properties().stacksTo(1),5,250));

   public static final RegistryObject<Item> LARGE_POTION_BOTTLE = ITEMS.register("large_potion_bottle", () -> new PotionLiquidBottleItem(new Item.Properties().stacksTo(1),10,250*2));

    public static final RegistryObject<Item> BIG_POTION_BOTTLE = ITEMS.register("big_potion_bottle", () -> new PotionLiquidBottleItem(new Item.Properties().stacksTo(1),15,250*3));

 public static final RegistryObject<Item> THROWABLE_POTION_BOTTLE = ITEMS.register("throwable_potion_bottle",
         () -> new ThrowablePotionBottleItem(new Item.Properties().stacksTo(1),0,250));


 public static final RegistryObject<Item> EFFECT_CODEX = ITEMS.register("effect_codex", () -> new EffectCodex(new Item.Properties()));

    public static final RegistryObject<Item> WOODEN_SPOON = ITEMS.register("wooden_spoon", () -> new SpoonItem(StirToolType.WOOD,new Item.Properties().stacksTo(1),"wooden"));

    public static final RegistryObject<Item> GOLDEN_SPOON = ITEMS.register("golden_spoon", () -> new SpoonItem(StirToolType.GOLD,new Item.Properties().stacksTo(1),"golden"));

    public static final RegistryObject<Item> DIAMOND_SPOON = ITEMS.register("diamond_spoon", () -> new SpoonItem(StirToolType.DIAMOND,new Item.Properties().stacksTo(1),"diamond"));

    public static final RegistryObject<Item> STONE_SPOON = ITEMS.register("stone_spoon", () -> new SpoonItem(StirToolType.STONE,new Item.Properties().stacksTo(1),"stone"));

    public static final RegistryObject<Item> IRON_SPOON = ITEMS.register("iron_spoon", () -> new SpoonItem(StirToolType.IRON,new Item.Properties().stacksTo(1),"iron"));

    public static final RegistryObject<Item> BASTION_SPOON = ITEMS.register("bastion_spoon", () -> new SpoonItem(StirToolType.BASTION,new Item.Properties().stacksTo(1),"bastion"));

    public static final RegistryObject<Item> SPONGE_SPOON = ITEMS.register("sponge_spoon", () -> new SpoonItem(StirToolType.SPONGE,new Item.Properties().stacksTo(1),"sponge"));

    public static final RegistryObject<Item> ENDER_SPOON = ITEMS.register("ender_spoon", () -> new SpoonItem(StirToolType.ENDER,new Item.Properties().stacksTo(1),"ender"));


    public static final RegistryObject<Item> PESTLE = ITEMS.register("pestle", () -> new Pestle(new Item.Properties()));


    public static final RegistryObject<Item> RECIPE_SCROLL = ITEMS.register("recipe_scroll", () -> new RecipeScroll(new Item.Properties()));

    public static final RegistryObject<Item> ALCHEMIST_MONOCLE = ITEMS.register("alchemist_monocle", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> MANDRAKE_SEED = ITEMS.register("mandrake_seed", () -> new Item(new Item.Properties().rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> ALLAY_ESSENCE = ITEMS.register("allay_essence", () -> new Item(new Item.Properties().rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> BAT_EAR = ITEMS.register("bat_ear", () -> new Item(new Item.Properties().rarity(Rarity.COMMON)));


    public static final RegistryObject<Item> ROTTEN_APPLE = ITEMS.register("rotten_apple", () ->
            new Item(new Item.Properties().rarity(Rarity.COMMON)
                    .food(new FoodProperties.Builder()
                            .saturationMod(3)
                            .nutrition(4)
                            .effect(new MobEffectInstance(
                                    ModMobEffects.INFESTED.get(), 200),
                                    0.2f)
                            .build())));

   public static final RegistryObject<Item> ROTTEN_CARROT = ITEMS.register("rotten_carrot", () ->
           new Item(new Item.Properties().rarity(Rarity.COMMON)
                   .food(new FoodProperties.Builder()
                           .saturationMod(2)
                           .nutrition(2)
                           .effect(new MobEffectInstance(
                                   MobEffects.BAD_OMEN,200),
                                   0.2f)
                           .build())));

   public static final RegistryObject<Item> ROTTEN_MELON_SLICE = ITEMS.register("rotten_melon_slice", () ->
           new Item(new Item.Properties().rarity(Rarity.COMMON)
                   .food(new FoodProperties.Builder()
                           .saturationMod(3)
                           .nutrition(4)
                           .effect(new MobEffectInstance(
                                   ModMobEffects.OOZING.get(),200),
                           0.2f)
                           .build())));

   public static final RegistryObject<Item> ROTTEN_FISH = ITEMS.register("rotten_fish", () ->
           new Item(new Item.Properties()
                   .food(new FoodProperties.Builder()
                           .saturationMod(4)
                           .nutrition(4)
                           .effect(new MobEffectInstance(MobEffects.POISON,200), 0.2f)
                           .effect(new MobEffectInstance(MobEffects.CONFUSION,200), 0.2f)
                           .build())));


   public static final RegistryObject<Item> GHAST_HEART = ITEMS.register("ghast_heart", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

   public static final RegistryObject<Item> POTION_HOMUNCULUS = ITEMS.register("potion_homunculus", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));


    public static final RegistryObject<Item> EMERALD_DUST = ITEMS.register("emerald_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> IRON_DUST = ITEMS.register("iron_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> AMETHYST_DUST = ITEMS.register("amethyst_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GOLD_DUST = ITEMS.register("gold_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COPPER_DUST = ITEMS.register("copper_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DIAMOND_DUST = ITEMS.register("diamond_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CRUSHED_EYE = ITEMS.register("crushed_eye", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> NETHERITE_DUST = ITEMS.register("netherite_dust", () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> ONYX = ITEMS.register("onyx", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BASTION_FRAGMENT = ITEMS.register("bastion_fragment", () -> new Item(new Item.Properties()));










    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
