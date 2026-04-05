package net.shule.shulespotions.dataGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.shule.shulespotions.ShulesPotions;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ProviderBlockTag extends BlockTagsProvider {
    public ProviderBlockTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ShulesPotions.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

       // this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.GRONITE_ORE.get());

    }
}
