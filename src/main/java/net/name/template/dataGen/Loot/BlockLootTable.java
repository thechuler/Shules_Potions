package net.name.template.dataGen.Loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.name.template.Blocks.ModBlocks;
import net.name.template.Items.ModItems;


import java.util.Set;

public class BlockLootTable extends BlockLootSubProvider {
    public BlockLootTable() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }



    @Override
    protected void generate() {
     /*   this.add(ModBlocks.GRONITE_ORE.get(),
                block -> createOreDrop(ModBlocks.GRONITE_ORE.get(), ModItems.GRONITE.get())
        );

*/
    }



    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
