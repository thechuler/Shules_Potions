package net.shule.shulespotions.util.CauldronActions;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AddLiquidAction implements CauldronAction{
    private final ItemStack expectedItem;

    public AddLiquidAction(ItemStack expectedItem) {
        this.expectedItem = expectedItem;
    }

    public static CauldronAction fromNetwork(FriendlyByteBuf pBuffer) {
        return new AddLiquidAction(pBuffer.readItem());
    }


    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeItemStack(expectedItem, true);
    }

    @Override
    public String getType() {
        return "add_liquid";
    }

    @Override
    public void execute(CauldronActionContext ctx) {
        ctx.player.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(Items.BUCKET));
        ctx.cauldron.setLiquidLevel(1);

     // ctx.cauldron.getPotionLiquid().setColor();
    }

    @Override
    public boolean canTrigger(CauldronActionContext ctx) {
        return ctx.player != null && ctx.player
                .getItemInHand(InteractionHand.MAIN_HAND).is(expectedItem.getItem());
    }

    @Override
    public CauldronActionTrigger getTrigger() {
        return CauldronActionTrigger.RIGHT_CLICK;
    }

    @Override
    public String getActionDescription() {
        return "AddLiquid" + expectedItem.getHoverName().getString();
    }

    @Override
    public ItemStack getAsociatedItem() {
        return expectedItem;
    }
}
