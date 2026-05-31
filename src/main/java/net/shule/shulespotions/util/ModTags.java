package net.shule.shulespotions.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import net.shule.shulespotions.ShulesPotions;

public class ModTags {


    public static class Blocks{


        private static TagKey<Block> tag(String name){
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,name));
        }
    }





    public static class Items{
        private static TagKey<Item> tag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,name));
        }
    }


}
