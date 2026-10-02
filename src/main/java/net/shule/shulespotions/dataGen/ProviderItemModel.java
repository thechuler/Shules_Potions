package net.shule.shulespotions.dataGen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.ShulesPotions;


// Aca vamos a agregar los modelos de nuestros items (un modelo es como la formita del item)
public class ProviderItemModel extends ItemModelProvider {
    public ProviderItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ShulesPotions.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        simpleItem(ModItems.EFFECT_CODEX);
        simpleItem(ModItems.ROTTEN_FISH);
        simpleItem(ModItems.ROTTEN_MELON_SLICE);
        simpleItem(ModItems.ROTTEN_CARROT);
        simpleItem(ModItems.ONYX);
        simpleItem(ModItems.IRON_DUST);
        simpleItem(ModItems.EMERALD_DUST);
        simpleItem(ModItems.DIAMOND_DUST);
        simpleItem(ModItems.COPPER_DUST);
        simpleItem(ModItems.AMETHYST_DUST);
        simpleItem(ModItems.CRUSHED_EYE);
        simpleItem(ModItems.GOLD_DUST);
        simpleItem(ModItems.NETHERITE_DUST);
        simpleItem(ModItems.PESTLE);
        simpleItem(ModItems.GHAST_HEART);
        simpleItem(ModItems.ROTTEN_APPLE);
      //  simpleItem(ModItems.ALCHEMIST_MONOCLE);
        simpleItem(ModItems.MANDRAKE_SEED);
        simpleItem(ModItems.BASTION_FRAGMENT);
        simpleItem(ModItems.BAT_EAR);
        simpleItem(ModItems.ALLAY_ESSENCE);
        simpleItem(ModItems.FRAGMENTED_ENDER_PEARL);
        simpleItem(ModItems.BUTTER);
        simpleItem(ModItems.NITRO_SPORES);
        simpleItem(ModItems.CRUSHED_TOTEM);
        simpleItem(ModItems.DOLPHIN_FIN);
        simpleItem(ModItems.FERMENTED_CARROT);

        simpleItem(ModItems.COMMON_LOOT_BUNLDE);
        simpleItem(ModItems.UNCOMMON_LOOT_BUNLDE);
        simpleItem(ModItems.INUSUAL_LOOT_BUNLDE);
        simpleItem(ModItems.RARE_LOOT_BUNLDE);
        simpleItem(ModItems.EXOTIC_LOOT_BUNLDE);
        simpleItem(ModItems.ANCIENT_LOOT_BUNLDE);
        simpleItem(ModItems.ANCIENT_KEY);

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
