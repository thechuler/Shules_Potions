package net.shule.shulespotions.util.CauldronActions;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;

public class AddItemAction implements CauldronAction {
    private final ItemStack expectedItem;

    public AddItemAction(ItemStack pExpectedItem){
        expectedItem = pExpectedItem;
    }


    @Override
    public boolean PerformAction(PotionCauldronBE Cauldron, Player player){
        ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
        return ItemStack.matches(expectedItem,item);
    }


    public ItemStack getExpectedItem() {
        return expectedItem;
    }
}
