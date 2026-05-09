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

import java.util.function.Consumer;


//Aca es donde agregamos las recetas de nuestro mod

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








    }


}
