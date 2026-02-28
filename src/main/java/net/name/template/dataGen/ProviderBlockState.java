package net.name.template.dataGen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.name.template.Blocks.ModBlocks;
import net.name.template.Template;

public class ProviderBlockState extends BlockStateProvider {
    public ProviderBlockState(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Template.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
       // blockWithItem(ModBlocks.THERIUM_ORE);

    }




    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
