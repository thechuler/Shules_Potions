package net.shule.shulespotions.util.CauldronActions;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.shule.shulespotions.ShulesPotions;

public class AddLiquidAction implements CauldronAction {
    private final ItemStack EXPECTED_ITEM;
    private final ResourceLocation FRAME = ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID, "item/frame/addliquid_action_frame");

    public AddLiquidAction(ItemStack expectedItem) {
        this.EXPECTED_ITEM = expectedItem;
    }

    public static CauldronAction fromNetwork(FriendlyByteBuf pBuffer) {
        return new AddLiquidAction(pBuffer.readItem());
    }


    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeItemStack(EXPECTED_ITEM, true);
    }

    @Override
    public String getType() {
        return "add_liquid";
    }

    @Override
    public void execute(CauldronActionContext ctx) {
        assert ctx.level != null;
        ctx.level.playSound(null, ctx.pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
        if (!ctx.player.isCreative()) ctx.player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
        ctx.cauldron.setLiquidLevel(3);
    }

    @Override
    public boolean canTrigger(CauldronActionContext ctx) {
        return ctx.player != null && ctx.player
                .getItemInHand(InteractionHand.MAIN_HAND).is(EXPECTED_ITEM.getItem());
    }

    @Override
    public CauldronActionTrigger getTrigger() {
        return CauldronActionTrigger.RIGHT_CLICK;
    }

    @Override
    public String getActionDescription() {
        return "AddLiquid" + EXPECTED_ITEM.getHoverName().getString();
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
