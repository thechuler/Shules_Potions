package net.shule.shulespotions.util.CauldronActions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;

public class CauldronActionContext {
    public final PotionCauldronBE cauldron;
    public final Level level;
    public final BlockPos pos;
    public final Player player;
    public final ItemEntity itemEntity;

    public CauldronActionContext(PotionCauldronBE cauldron, Player player, ItemEntity itemEntity) {
        this.cauldron = cauldron;
        this.level = cauldron.getLevel();
        this.pos = cauldron.getBlockPos();
        this.player = player;
        this.itemEntity = itemEntity;
    }
}
