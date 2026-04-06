package net.shule.shulespotions.util.CauldronActions;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;

public interface CauldronAction {
    boolean PerformAction(PotionCauldronBE cauldron, Player player);
    ItemStack getExpectedItem();
}
