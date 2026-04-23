package net.shule.shulespotions.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;


import java.util.ArrayList;
import java.util.List;

public class CauldronUtils {


    public static List<BlockPos> findBlocksInArea(Level level, BlockPos center, int distance, Block target) {
        List<BlockPos> found = new ArrayList<>();

        BlockPos min = center.offset(-distance, -distance, -distance);
        BlockPos max = center.offset(distance, distance, distance);

        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            if (level.getBlockState(pos).is(target)) {
                found.add(pos);
            }
        }

        return found;
    }







}
