package net.shule.shulespotions.dataGen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.ShulesPotions;


// Aca vamos a agregar los modelos de nuestros items (un modelo es como la formita del item)
public class ProviderItemModel extends ItemModelProvider {
    public ProviderItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ShulesPotions.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        simpleItem(ModItems.RECIPE_BOOK);
        simpleItem(ModItems.ROTTEN_FISH);
        simpleItem(ModItems.ONYX);
        simpleItem(ModItems.IRON_DUST);
        simpleItem(ModItems.EMERALD_DUST);



        recipeSheetItem(ModItems.SPEED_RECIPE);
        recipeSheetItem(ModItems.SLOWNESS_RECIPE);
        recipeSheetItem(ModItems.FIRE_RESISTANCE_RECIPE);


        recipeSheetItem(ModItems.WATER_BREATHING_RECIPE);
        recipeSheetItem(ModItems.LUCK_RECIPE);
        recipeSheetItem(ModItems.UNLUCK_RECIPE);
        recipeSheetItem(ModItems.HERO_OF_THE_VILLAGE_RECIPE);
        recipeSheetItem(ModItems.BAD_OMEN_RECIPE);
        recipeSheetItem(ModItems.HUNGER_RECIPE);
        recipeSheetItem(ModItems.SATURATION_RECIPE);

        recipeSheetItem(ModItems.NAUSEA_RECIPE);
        recipeSheetItem(ModItems.HEALTH_BOOST_RECIPE);
        recipeSheetItem(ModItems.REGENERATION_RECIPE);

        recipeSheetItem(ModItems.INSTANT_DAMAGE_RECIPE);
        recipeSheetItem(ModItems.INSTANT_HEALTH_RECIPE);

        recipeSheetItem(ModItems.ABSORPTION_RECIPE);
        recipeSheetItem(ModItems.WEAKNESS_RECIPE);
        recipeSheetItem(ModItems.POISON_RECIPE);
        recipeSheetItem(ModItems.WITHER_RECIPE);

        recipeSheetItem(ModItems.DARKNESS_RECIPE);
        recipeSheetItem(ModItems.BLINDNESS_RECIPE);
        recipeSheetItem(ModItems.INVISIBILITY_RECIPE);
        recipeSheetItem(ModItems.GLOWING_RECIPE);

        recipeSheetItem(ModItems.NIGHT_VISION_RECIPE);
        recipeSheetItem(ModItems.JUMP_RECIPE);
        recipeSheetItem(ModItems.SLOW_FALLING_RECIPE);
        recipeSheetItem(ModItems.LEVITATION_RECIPE);

        recipeSheetItem(ModItems.DIG_SPEED_RECIPE);
        recipeSheetItem(ModItems.DIG_SLOWDOWN_RECIPE);
        recipeSheetItem(ModItems.DAMAGE_RESISTANCE_RECIPE);
    }



    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                 ResourceLocation.fromNamespaceAndPath("minecraft", "item/generated"))
                .texture("layer0",
                         ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder recipeSheetItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                mcLoc("item/generated"))
                .texture("layer0", modLoc("item/recipe_sheet_base"))
                .texture("layer1", modLoc("item/recipe_sheet_overlay"));
    }



    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                 ResourceLocation.parse("item/handheld")).texture("layer0",
                 ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,"item/" + item.getId().getPath()));
    }


}
