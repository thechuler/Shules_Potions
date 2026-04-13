package net.shule.shulespotions.util.CauldronActions;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.ShulesPotions;

public class StirAction implements CauldronAction{
    private final ItemStack EXPECTED_ITEM;
    private final ResourceLocation FRAME = ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,"item/frame/stir_action_frame");


    public StirAction(ItemStack expectedItem) {
        this.EXPECTED_ITEM = expectedItem;
    }

    public static CauldronAction fromNetwork(FriendlyByteBuf buffer) {
        return new StirAction(buffer.readItem());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeItemStack(EXPECTED_ITEM, true);
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
                InteractionHand.MAIN_HAND).is(ModItems.WOODEN_SPOON.get());
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
        return EXPECTED_ITEM;
    }

    @Override
    public ResourceLocation getFrameResource() {
        return FRAME;
    }


}
