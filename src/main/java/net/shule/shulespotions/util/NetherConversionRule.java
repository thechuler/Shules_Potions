package net.shule.shulespotions.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;



public class NetherConversionRule {

    private final Block block;
    private final TagKey<Block> tag;
    private final Block replacement;

    public NetherConversionRule(Block block, Block replacement) {
        this.block = block;
        this.tag = null;
        this.replacement = replacement;
    }


    public NetherConversionRule(TagKey<Block> tag, Block replacement) {
        this.block = null;
        this.tag = tag;
        this.replacement = replacement;
    }

    public boolean matches(BlockState state) {
        if (block != null) {
            return state.is(block);
        }

        return state.is(tag);
    }

    public Block getReplacement() {
        return replacement;
    }
}