package net.shule.shulespotions.dataGen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Blocks.Custom.SpitterTrap;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.ShulesPotions;


public class ProviderBlockState extends BlockStateProvider {
    public ProviderBlockState(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ShulesPotions.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
       // blockWithItem(ModBlocks.ALCHEMIST_WOOD);

        blockWithItem(ModBlocks.METEORITE_STONE);
        blockWithItem(ModBlocks.METEORITE_STONE_BURNED);
        blockWithItem(ModBlocks.METEORITE_STONE_PETRIFIED);


        blockWithItem(ModBlocks.INSTABLE_ANCITE_BRICKS);
        blockWithItem(ModBlocks.ANCITE_BRICKS);
        blockWithItem(ModBlocks.SMOOTH_ANCITE);
        blockWithItem(ModBlocks.CRACKED_ANCITE_BRICKS);
        blockWithItem(ModBlocks.CHISELED_ANCITE_BRICKS);
        blockWithItem(ModBlocks.MOSSY_ANCITE_BRICKS);
        blockWithItem(ModBlocks.GROWN_MOSSY_ANCITE_BRICKS);
        blockWithItem(ModBlocks.OVERGROWN_MOSSY_ANCITE_BRICKS);
        blockWithItem(ModBlocks.ANCITE_ORNAMENTED_BRICKS);
        blockWithItem(ModBlocks.CHISELED_ANCITE_ORNAMENTED_BRICKS);
        blockWithItem(ModBlocks.ANCITE_LOCK);
        blockWithItem(ModBlocks.ANCITE_BLOCKING_WALL);


        logBlock((RotatedPillarBlock) ModBlocks.ANCITE_PILLAR.get());
        simpleBlockItem(ModBlocks.ANCITE_PILLAR.get(), models().getExistingFile(modLoc("block/ancite_pillar")));

        stairsBlock(((StairBlock) ModBlocks.ANCITE_BRICKS_STAIRS.get()), blockTexture(ModBlocks.ANCITE_BRICKS.get()));
        simpleBlockItem(ModBlocks.ANCITE_BRICKS_STAIRS.get(), models().getExistingFile(modLoc("block/ancite_bricks_stairs")));


        blockWithItem(ModBlocks.ANCIENT_REDSTONE_CLOCK);


        blockWithItem(ModBlocks.ONYX_ORE);
        createRandomShelf(ModBlocks.POTIONSHELF.get());

        ModelFile spitterTrapModel = models().cube(
                "spitter_trap",
                modLoc("block/smooth_ancite"),
                modLoc("block/smooth_ancite"),
                modLoc("block/spitter_trap_back"),
                modLoc("block/spitter_trap_front"),
                modLoc("block/smooth_ancite"),
                modLoc("block/smooth_ancite")
        ).texture("particle", modLoc("block/smooth_ancite"));


        getVariantBuilder(ModBlocks.SPITTER_TRAP.get())
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(spitterTrapModel)
                        .rotationY((int) state.getValue(SpitterTrap.FACING).toYRot())
                        .build()
                );
        simpleBlockItem(ModBlocks.SPITTER_TRAP.get(), spitterTrapModel);
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
