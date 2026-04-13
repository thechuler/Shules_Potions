package net.shule.shulespotions.util.CauldronActions;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.ShulesPotions;

public class AddItemAction implements CauldronAction {
    private final ItemStack EXPECTED_ITEM;
    private final ResourceLocation FRAME = ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID,"item/frame/additem_action_frame");

    public AddItemAction(ItemStack pExpectedItem){
        EXPECTED_ITEM = pExpectedItem;
    }

    public static CauldronAction fromNetwork(FriendlyByteBuf pBuffer) {
        return new AddItemAction(pBuffer.readItem());
    }


    //estos son necesarios para compartir la accion, ya que es un objeto como la receta
    public void toNetwork(FriendlyByteBuf buffer){
        //leer el caso del metodo a ver si corresponde ese valor de limitedTag
        buffer.writeItemStack(EXPECTED_ITEM, true);
    }

    @Override
    public String getType(){
        return "add_item";
    }

    @Override
    public void execute(CauldronActionContext ctx) {
        ItemStack stack = ctx.itemEntity.getItem();
        stack.shrink(1);

        if (stack.isEmpty()) {
            ctx.itemEntity.discard();
        }
    }

    @Override
    public boolean canTrigger(CauldronActionContext ctx) {
        if (ctx.itemEntity == null) return false;
        return ItemStack.matches(EXPECTED_ITEM, ctx.itemEntity.getItem());
    }


    @Override
    public CauldronActionTrigger getTrigger() {
        return CauldronActionTrigger.ITEM_INSIDE;
    }

    @Override
    public String getActionDescription() {
        return "Add: " + EXPECTED_ITEM.getHoverName().getString();
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
