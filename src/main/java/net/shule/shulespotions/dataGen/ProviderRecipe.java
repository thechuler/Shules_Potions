package net.shule.shulespotions.dataGen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.Recipes.MortarRecipeBuilder;
import net.shule.shulespotions.ShulesPotions;

import java.util.function.Consumer;




public class ProviderRecipe extends RecipeProvider {
    public ProviderRecipe(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {





        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModBlocks.POTION_CAULDRON.get().asItem())
                .pattern("I I")
                .pattern("IPI")
                .pattern("III")
                .define('P', ModItems.SMALL_POTION_BOTTLE.get())
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_potion_bottle", has(ModItems.SMALL_POTION_BOTTLE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModBlocks.COPPER_CAULDRON.get().asItem())
                .pattern("C C")
                .pattern("CPC")
                .pattern("CCC")
                .define('P', ModItems.LARGE_POTION_BOTTLE.get())
                .define('C', Items.COPPER_BLOCK)
                .unlockedBy("has_potion_bottle", has(ModItems.SMALL_POTION_BOTTLE.get()))
                .save(consumer);



        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModItems.DIAMOND_SPOON.get())
                .pattern("  S")
                .pattern(" S ")
                .pattern("D  ")
                .define('D', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_iron_spoon", has(ModItems.IRON_SPOON.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModItems.WOODEN_SPOON.get())
                .pattern("  S")
                .pattern(" S ")
                .pattern("P  ")
                .define('P', ItemTags.PLANKS)
                .define('S', Items.STICK)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModItems.STONE_SPOON.get())
                .pattern("  S")
                .pattern(" S ")
                .pattern("C  ")
                .define('C', Items.COBBLESTONE)
                .define('S', Items.STICK)
                .unlockedBy("has_wooden_spoon", has(ModItems.WOODEN_SPOON.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModItems.ONYX.get().asItem())
                .pattern("BBB")
                .pattern("BDB")
                .pattern("BBB")
                .define('B', ModItems.BASTION_FRAGMENT.get())
                .define('D', Items.DIAMOND)
                .unlockedBy("has_bastion_fragment", has(ModItems.BASTION_FRAGMENT.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModItems.IRON_SPOON.get())
                .pattern("  S")
                .pattern(" S ")
                .pattern("I  ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_stone_spoon", has(ModItems.STONE_SPOON.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModItems.BASTION_SPOON.get())
                .pattern("  B")
                .pattern(" G ")
                .pattern("B  ")
                .define('G', Items.GOLD_INGOT)
                .define('B', ModItems.BASTION_FRAGMENT.get())
                .unlockedBy("has_diamond_spoon", has(ModItems.DIAMOND_SPOON.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModItems.SPONGE_SPOON.get())
                .pattern("  S")
                .pattern(" S ")
                .pattern("E  ")
                .define('S', Items.STICK)
                .define('E', Items.SPONGE)
                .unlockedBy("has_diamond_spoon", has(ModItems.DIAMOND_SPOON.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModItems.GOLDEN_SPOON.get())
                .pattern("  G")
                .pattern(" G ")
                .pattern("B  ")
                .define('G', Items.GOLD_INGOT)
                .define('B', Items.GOLD_BLOCK)
                .unlockedBy("has_diamond_spoon", has(ModItems.DIAMOND_SPOON.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModItems.ALCHEMIST_MONOCLE.get())
                .pattern(" G ")
                .pattern("GCG")
                .pattern(" G ")
                .define('G', Items.GOLD_INGOT)
                .define('C', Items.GLASS_PANE)
                .unlockedBy("has_diamond_spoon", has(Items.GOLD_INGOT))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModBlocks.SPOON_RACK.get())
                .pattern("SSS")
                .define('S', Items.STICK)
                .unlockedBy("has_wooden_spoon", has(ModItems.WOODEN_SPOON.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModBlocks.MORTAR.get())
                .pattern("S S")
                .pattern("SSS")
                .define('S', ItemTags.STONE_CRAFTING_MATERIALS)
                .unlockedBy("has_stone", has(ItemTags.STONE_CRAFTING_MATERIALS))
                .save(consumer);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, ModItems.PESTLE.get())
                .requires(ItemTags.STONE_CRAFTING_MATERIALS)
                .requires(ItemTags.STONE_CRAFTING_MATERIALS)
                .unlockedBy("has_stone", has(ItemTags.STONE_CRAFTING_MATERIALS))
                .save(consumer);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, ModItems.ROTTEN_FISH.get())
                .requires(Items.ROTTEN_FLESH)
                .requires(Items.SALMON)
                .unlockedBy("has_rotten_flesh", has(Items.ROTTEN_FLESH))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, ModItems.ROTTEN_CARROT.get())
                .requires(Items.ROTTEN_FLESH)
                .requires(Items.CARROT)
                .unlockedBy("has_rotten_flesh", has(Items.ROTTEN_FLESH))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, ModItems.ROTTEN_MELON_SLICE.get())
                .requires(Items.ROTTEN_FLESH)
                .requires(Items.MELON_SLICE)
                .unlockedBy("has_rotten_flesh", has(Items.ROTTEN_FLESH))
                .save(consumer);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, ModItems.ROTTEN_APPLE.get())
                .requires(Items.ROTTEN_FLESH)
                .requires(Items.APPLE)
                .unlockedBy("has_rotten_flesh", has(Items.ROTTEN_FLESH))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, ModItems.EFFECT_CODEX.get())
                .requires(ModItems.SMALL_POTION_BOTTLE.get())
                .requires(Items.BOOK)
                .unlockedBy("has_potion_bottle", has(ModItems.SMALL_POTION_BOTTLE.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, ModItems.THROWABLE_POTION_BOTTLE.get())
                .requires(ModItems.SMALL_POTION_BOTTLE.get())
                .requires(Items.PAPER)
                .unlockedBy("has_potion_bottle", has(ModItems.SMALL_POTION_BOTTLE.get()))
                .save(consumer);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, ModItems.SMALL_POTION_BOTTLE.get())
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.PAPER)
                .unlockedBy("has_bottle", has(Items.GLASS_BOTTLE))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, ModItems.LARGE_POTION_BOTTLE.get())
                .requires(ModItems.SMALL_POTION_BOTTLE.get())
                .requires(Items.GLASS)
                .requires(Items.GLASS)
                .requires(Items.PAPER)
                .unlockedBy("has_potion_bottle", has(ModItems.SMALL_POTION_BOTTLE.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, ModItems.FRAGMENTED_ENDER_PEARL.get(),2)
                .requires(Items.ENDER_PEARL)
                .unlockedBy("has_potion_bottle", has(ModItems.SMALL_POTION_BOTTLE.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.BUTTER.get())
                .requires(Items.MILK_BUCKET)
                .requires(Items.SUGAR)
                .requires(Items.SUGAR)
                .unlockedBy("has_milk_bucket", has(Items.MILK_BUCKET))
                .save(consumer);




        MortarRecipeBuilder.mortar(ModItems.DIAMOND_DUST.get(),6,3,0.5f,"#4aedd9")
                .addIngredient(Items.DIAMOND)
                .addIngredient(Items.DIAMOND)
                .addIngredient(Items.DIAMOND)
                .save(consumer,ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,"diamond_dust_recipe"));

        MortarRecipeBuilder.mortar(ModItems.IRON_DUST.get(),6,3,0.5f,"#d8af93")
                .addIngredient(Items.IRON_INGOT)
                .addIngredient(Items.IRON_INGOT)
                .addIngredient(Items.IRON_INGOT)
                .save(consumer,ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,"iron_dust_recipe"));

        MortarRecipeBuilder.mortar(ModItems.AMETHYST_DUST.get(),3,2,0.5f,"#b38ef3")
                .addIngredient(Items.AMETHYST_SHARD)
                .addIngredient(Items.AMETHYST_SHARD)
                .addIngredient(Items.AMETHYST_SHARD)
                .save(consumer,ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,"amethyst_dust_recipe"));

        MortarRecipeBuilder.mortar(ModItems.NETHERITE_DUST.get(),8,6,0.5f,"#3b393b")
                .addIngredient(Items.NETHERITE_INGOT)
                .addIngredient(Items.NETHERITE_INGOT)
                .addIngredient(Items.NETHERITE_INGOT)
                .save(consumer,ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,"netherite_dust_recipe"));

        MortarRecipeBuilder.mortar(ModItems.COPPER_DUST.get(),5,3,0.5f,"#c3563c")
                .addIngredient(Items.COPPER_INGOT)
                .addIngredient(Items.COPPER_INGOT)
                .addIngredient(Items.COPPER_INGOT)
                .save(consumer,ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,"copper_dust_recipe"));


        MortarRecipeBuilder.mortar(ModItems.CRUSHED_EYE.get(),3,3,0.5f,"#7d1b04")
                .addIngredient(Items.SPIDER_EYE)
                .addIngredient(Items.SPIDER_EYE)
                .addIngredient(Items.SPIDER_EYE)
                .save(consumer,ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,"crushed_eye_recipe"));

        MortarRecipeBuilder.mortar(ModItems.GOLD_DUST.get(),6,3,0.5f,"#ffde00")
                .addIngredient(Items.GOLD_INGOT)
                .addIngredient(Items.GOLD_INGOT)
                .addIngredient(Items.GOLD_INGOT)
                .save(consumer,ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,"gold_dust_recipe"));

        MortarRecipeBuilder.mortar(ModItems.EMERALD_DUST.get(),3,2,0.5f,"#00fd21")
                .addIngredient(Items.EMERALD)
                .addIngredient(Items.EMERALD)
                .addIngredient(Items.EMERALD)
                .save(consumer,ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,"emerald_dust_recipe"));



    }


}
