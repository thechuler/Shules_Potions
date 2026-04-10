package net.shule.shulespotions.dataGen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
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
        simpleItem(ModItems.SPEED_RECIPE);
    }



    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                 ResourceLocation.fromNamespaceAndPath("minecraft", "item/generated"))
                .texture("layer0",
                         ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID, "item/" + item.getId().getPath()));
    }


    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                 ResourceLocation.parse("item/handheld")).texture("layer0",
                 ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,"item/" + item.getId().getPath()));
    }


}
