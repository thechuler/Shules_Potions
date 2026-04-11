package net.shule.shulespotions.util.CauldronActions;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class StirAction implements CauldronAction{
    private final ItemStack expectedItem;

    public StirAction(ItemStack expectedItem) {
        this.expectedItem = expectedItem;
    }

    public static CauldronAction fromNetwork(FriendlyByteBuf buffer) {
        return new StirAction(buffer.readItem());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeItemStack(expectedItem, true);
    }

    @Override
    public String getType() {
        return "stir";
    }

    @Override
    public void execute(CauldronActionContext ctx) {

    }

    @Override
    public boolean canTrigger(CauldronActionContext ctx) {
        return ctx.player != null && ctx.player.getItemInHand(
                InteractionHand.MAIN_HAND).is(Items.WOODEN_SHOVEL);
    }

    @Override
    public CauldronActionTrigger getTrigger() {
        return CauldronActionTrigger.RIGHT_CLICK;
    }

    @Override
    public String getActionDescription() {
        return "Stir";
    }

    @Override
    public ItemStack getAsociatedItem() {
        return expectedItem;
    }


}
