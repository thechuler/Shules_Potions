package net.shule.shulespotions.dataGen.Loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.shule.shulespotions.Blocks.ModBlocks;
import net.shule.shulespotions.Items.ModItems;


import java.util.Set;

public class BlockLootTable extends BlockLootSubProvider {
    public BlockLootTable() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }



    @Override
    protected void generate() {


        dropSelf(ModBlocks.MORTAR.get());
        dropSelf(ModBlocks.COPPER_CAULDRON.get());
        dropSelf(ModBlocks.POTION_CAULDRON.get());
        dropSelf(ModBlocks.POTIONSHELF.get());
        dropSelf(ModBlocks.SPOON_RACK.get());

        dropSelf(ModBlocks.METEORITE_STONE.get());
        dropSelf(ModBlocks.METEORITE_STONE_BURNED.get());
        dropSelf(ModBlocks.METEORITE_STONE_PETRIFIED.get());
        dropSelf(ModBlocks.METEOR_CAULDRON.get());
        dropSelf(ModBlocks.BIG_CAULDRON.get());




        dropSelf(ModBlocks.SPIKE.get());
        this.add(ModBlocks.ONYX_ORE.get(),
                block -> createOreDrop(ModBlocks.ONYX_ORE.get(), ModItems.ONYX.get())
        );


        this.add(ModBlocks.POTION_SPLASH.get(),noDrop());
        this.add(ModBlocks.POTION_FLUID_BLOCK.get(), noDrop());
        this.add(ModBlocks.UNFINISHED_POTION_FLUID_BLOCK.get(), noDrop());
    }



    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
