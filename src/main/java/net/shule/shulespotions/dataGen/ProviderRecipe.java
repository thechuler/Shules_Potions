package net.shule.shulespotions.dataGen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.shule.shulespotions.Items.ModItems;

import java.util.function.Consumer;


//Aca es donde agregamos las recetas de nuestro mod

public class ProviderRecipe extends RecipeProvider {
    public ProviderRecipe(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {


        //SPEED
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.SUGAR)
                .stir(ModItems.WOODEN_SPOON.get())
                .result(MobEffects.MOVEMENT_SPEED)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "speed_recipe"));

        //SLOWNESS
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.SUGAR)
                .freeze(-100)
                .result(MobEffects.MOVEMENT_SLOWDOWN)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "slowness_recipe"));


        //FIRE RESISTANCE
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.SUGAR)
                .addItem(ModItems.IRON_DUST.get())
                .heat(100)
                .addItem(Items.BLAZE_POWDER)
                .result(MobEffects.FIRE_RESISTANCE)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "fire_resistance_recipe"));


        //WATHER BREATHING
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.SUGAR)
                .addItem(ModItems.IRON_DUST.get())
                .heat(100)
                .addItem(Items.KELP)
                .result(MobEffects.WATER_BREATHING)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "water_breathing_recipe"));


        //BAD OMEN
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(ModItems.EMERALD_DUST.get())
                .stir(ModItems.WOODEN_SPOON.get())
                .result(MobEffects.BAD_OMEN)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "bad_omen_recipe"));


        //LUCK
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(ModItems.EMERALD_DUST.get())
                .stir(ModItems.WOODEN_SPOON.get())
                .addItem(Items.EMERALD)
                .freeze(-100)
                .result(MobEffects.LUCK)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "luck_recipe"));


        //UNLUCK
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(ModItems.EMERALD_DUST.get())
                .stir(ModItems.WOODEN_SPOON.get())
                .addItem(Items.EMERALD)
                .heat(100)
                .result(MobEffects.UNLUCK)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "unluck_recipe"));


        //HERO OF THE VILLAGE
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(ModItems.EMERALD_DUST.get())
                .stir(ModItems.WOODEN_SPOON.get())
                .addItem(Items.EMERALD)
                .addItem(Items.EMERALD_BLOCK)
                .stir(ModItems.WOODEN_SPOON.get())
                .addItem(Items.TOTEM_OF_UNDYING)
                .result(MobEffects.HERO_OF_THE_VILLAGE)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "hero_of_the_village_recipe"));


        //JUMP_BOOST
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.FEATHER)
                .result(MobEffects.JUMP)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "jump_recipe"));


        //SLOW_FALLING
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.FEATHER)
                .freeze(-100)
                .result(MobEffects.SLOW_FALLING)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "slow_falling_recipe"));


        //LEVITATION
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.FEATHER)
                .freeze(-100)
                .freeze(-100)
                .addItem(Items.PHANTOM_MEMBRANE)
                .stir(ModItems.WOODEN_SPOON.get())
                .result(MobEffects.LEVITATION)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "levitation_recipe"));


        //DIG_SLOWDOWN
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.STONE)
                .freeze(-100)
                .result(MobEffects.DIG_SLOWDOWN)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "dig_slowdown_recipe"));


        //DIG_SPEED
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.STONE)
                .heat(100)
                .addItem(Items.AMETHYST_SHARD)
                .stir(ModItems.WOODEN_SPOON.get())
                .heat(100)
                .addItem(ModItems.ONYX.get())
                .result(MobEffects.DIG_SPEED)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "dig_speed_recipe"));


        //HUNGER
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.ROTTEN_FLESH)
                .result(MobEffects.HUNGER)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "hunger_recipe"));


        //RESISTANCE
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.ROTTEN_FLESH)
                .addItem(Items.GOLDEN_CARROT)
                .addItem(ModItems.IRON_DUST.get())
                .stir(ModItems.WOODEN_SPOON.get())
                .addItem(Items.BONE_MEAL)
                .result(MobEffects.DAMAGE_RESISTANCE)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "damage_resistance_recipe"));


        //SATURATION
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.ROTTEN_FLESH)
                .addItem(Items.GOLDEN_CARROT)
                .heat(100)
                .stir(ModItems.WOODEN_SPOON.get())
                .result(MobEffects.SATURATION)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "saturation_recipe"));


        //NAUSEA
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(ModItems.ROTTEN_FISH.get())
                .result(MobEffects.CONFUSION)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "nausea_recipe"));


        //HEALTH_BOOST
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(ModItems.ROTTEN_FISH.get())
                .addItem(Items.GLISTERING_MELON_SLICE)
                .result(MobEffects.HEALTH_BOOST)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "health_boost_recipe"));





        //REGENERATION
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(ModItems.ROTTEN_FISH.get())
                .addItem(Items.GLISTERING_MELON_SLICE)
                .heat(100)
                .stir(ModItems.WOODEN_SPOON.get())
                .result(MobEffects.REGENERATION)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "regeneration_recipe"));


        //INSTANT_HEALTH
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(ModItems.ROTTEN_FISH.get())
                .addItem(Items.GLISTERING_MELON_SLICE)
                .freeze(-100)
                .addItem(Items.HONEY_BOTTLE)
                .stir(ModItems.WOODEN_SPOON.get())
                .heat(100)
                .result(MobEffects.HEAL)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "instant_health_recipe"));

        //INSTANT_DAMAGE
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(ModItems.ROTTEN_FISH.get())
                .addItem(Items.GLISTERING_MELON_SLICE)
                .freeze(-100)
                .addItem(Items.HONEY_BOTTLE)
                .stir(ModItems.WOODEN_SPOON.get())
                .addItem(Items.FERMENTED_SPIDER_EYE)
                .result(MobEffects.HARM)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "instant_damage_recipe"));





        //ABSORPTION
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(ModItems.ROTTEN_FISH.get())
                .addItem(Items.GLISTERING_MELON_SLICE)
                .freeze(-100)
                .addItem(Items.HONEY_BOTTLE)
                .stir(ModItems.WOODEN_SPOON.get())
                .heat(100)
                .addItem(Items.GOLDEN_CARROT)
                .addItem(Items.GOLDEN_APPLE)
                .heat(100)
                .stir(ModItems.WOODEN_SPOON.get())
                .result(MobEffects.ABSORPTION)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "absorption_recipe"));




        //POISON
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(ModItems.ROTTEN_FISH.get())
                .addItem(Items.POISONOUS_POTATO)
                .addItem(Items.PUFFERFISH)
                .stir(ModItems.WOODEN_SPOON.get())
                .result(MobEffects.POISON)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "poison_recipe"));


        //WITHER
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(ModItems.ROTTEN_FISH.get())
                .addItem(Items.POISONOUS_POTATO)
                .addItem(Items.PUFFERFISH)
                .stir(ModItems.WOODEN_SPOON.get())
                .addItem(ModItems.IRON_DUST.get())
                .heat(100)
                .result(MobEffects.WITHER)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "wither_recipe"));


        //WEAKNESS
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(ModItems.ROTTEN_FISH.get())
                .addItem(Items.POISONOUS_POTATO)
                .addItem(Items.PUFFERFISH)
                .freeze(-100)
                .result(MobEffects.WEAKNESS)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "weakness_recipe"));


        //BLINDNESS
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.SPIDER_EYE)
                .result(MobEffects.BLINDNESS)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "blindness_recipe"));


        //INVISIBILITY
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.SPIDER_EYE)
                .heat(100)
                .result(MobEffects.INVISIBILITY)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "invisibility_recipe"));

        //DARKNESS
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.SPIDER_EYE)
                .freeze(-100)
                .result(MobEffects.DARKNESS)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "darkness_recipe"));


        //GLOWING
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.SPIDER_EYE)
                .addItem(Items.GOLDEN_CARROT)
                .stir(ModItems.WOODEN_SPOON.get())
                .result(MobEffects.GLOWING)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "glowing_recipe"));


        //NIGHT_VISION
        PotionRecipeBuilder.create()
                .addLiquid(Items.WATER_BUCKET)
                .addItem(Items.SPIDER_EYE)
                .addItem(Items.GOLDEN_CARROT)
                .stir(ModItems.WOODEN_SPOON.get())
                .addItem(Items.GOLDEN_CARROT)
                .heat(100)
                .result(MobEffects.NIGHT_VISION)
                .save(consumer, ResourceLocation.fromNamespaceAndPath(
                        "shulespotions",
                        "night_vision_recipe"));


        /*
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.GRONITE_MACE.get())
                .pattern("GGG")
                .pattern("GSG")
                .pattern(" S ")
                .define('G', ModItems.GRONITE.get())
                .define('S', Items.STICK)
                .unlockedBy("has_gronite", has(ModItems.GRONITE.get()))
                .save(consumer);

        */

    }


}
