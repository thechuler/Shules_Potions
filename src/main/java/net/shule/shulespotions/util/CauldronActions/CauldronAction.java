package net.shule.shulespotions.util.CauldronActions;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;

public interface CauldronAction {

    void toNetwork(FriendlyByteBuf buffer);
    String getType();
    void execute(CauldronActionContext ctx);
    boolean canTrigger(CauldronActionContext ctx);
    CauldronActionTrigger getTrigger();
    String getActionDescription();
    ItemStack getAsociatedItem();

}
