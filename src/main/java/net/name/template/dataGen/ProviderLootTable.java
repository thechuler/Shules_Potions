package net.name.template.dataGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.name.template.dataGen.Loot.BlockLootTable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ProviderLootTable  {

    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(BlockLootTable::new, LootContextParamSets.BLOCK)
             //   new LootTableProvider.SubProviderEntry(EntityLootTable::new, LootContextParamSets.ENTITY)
        ));
    }


}
