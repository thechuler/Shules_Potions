package net.shule.shulespotions.dataGen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
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

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BREWING, ModItems.BIG_POTION_BOTTLE.get())
                .requires(ModItems.LARGE_POTION_BOTTLE.get())
                .requires(Items.GLASS)
                .requires(Items.GLASS)
                .requires(Items.GLASS)
                .requires(Items.PAPER)
                .unlockedBy("has_potion_bottle", has(ModItems.SMALL_POTION_BOTTLE.get()))
                .save(consumer);


        MortarRecipeBuilder.mortar(ModItems.DIAMOND_DUST.get(),6,3,0.5f,"#4aedd9")
                .addIngredient(Items.DIAMOND)
                .addIngredient(Items.DIAMOND)
                .addIngredient(Items.DIAMOND)
                .save(consumer,ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,"diamond_dust_recipe"));

        MortarRecipeBuilder.mortar(ModItems.IRON_DUST.get(),6,3,0.5f,"#d8af93")
                .addIngredient(Items.RAW_IRON)
                .addIngredient(Items.RAW_IRON)
                .addIngredient(Items.RAW_IRON)
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




    }


}
