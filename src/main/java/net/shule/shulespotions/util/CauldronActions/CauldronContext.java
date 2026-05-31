package net.shule.shulespotions.util.CauldronActions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;
import net.shule.shulespotions.Potions.PotionLiquid;

import javax.annotation.Nullable;

public class CauldronContext {

    private final PotionCauldronBE cauldron;
    private final Level level;
    private final BlockPos pos;
    private final @Nullable Player player;

    public CauldronContext(PotionCauldronBE cauldron, @Nullable Player player
    ) {
        this.cauldron = cauldron;
        this.level = cauldron.getLevel();
        this.pos = cauldron.getBlockPos();
        this.player = player;
    }

    public PotionLiquid getPotion() {
        return cauldron.getPotionLiquid();
    }

    public PotionCauldronBE getCauldron() {
        return cauldron;
    }

    public Level getLevel() {
        return level;
    }

    public BlockPos getPos() {
        return pos;
    }

    public @Nullable Player getPlayer() {
        return player;
    }
}