package net.name.template.dataGen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.name.template.Items.ModItems;

import java.util.function.Consumer;



//Aca es donde agregamos las recetas de nuestro mod

public class ProviderRecipe extends RecipeProvider {
    public ProviderRecipe(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {


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
