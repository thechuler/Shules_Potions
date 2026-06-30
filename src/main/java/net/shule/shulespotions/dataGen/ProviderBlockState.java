package net.shule.shulespotions.dataGen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.ShulesPotions;


public class ProviderBlockState extends BlockStateProvider {
    public ProviderBlockState(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ShulesPotions.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
       // blockWithItem(ModBlocks.ALCHEMIST_WOOD);
      //  blockWithItem(ModBlocks.POTIONSHELF);
        createRandomShelf(ModBlocks.POTIONSHELF.get());

    }

    private void createRandomShelf(Block block) {

        ModelFile model0 = models().cubeColumn(
                "potionshelf_0",
                modLoc("block/potionshelf"),
                modLoc("block/potionshelf_top")
        );

        ModelFile model1 = models().cubeColumn(
                "potionshelf_1",
                modLoc("block/potionshelf1"),
                modLoc("block/potionshelf_top")
        );

        ModelFile model2 = models().cubeColumn(
                "potionshelf_2",
                modLoc("block/potionshelf2"),
                modLoc("block/potionshelf_top")
        );

        getVariantBuilder(block)
                .partialState()
                .setModels(
                        new ConfiguredModel(model0),
                        new ConfiguredModel(model1),
                        new ConfiguredModel(model2)
                );

        simpleBlockItem(block, model0);
    }


    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
