package net.shule.shulespotions.util.CauldronActions;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;

public class AddItemAction implements CauldronAction {
    private final ItemStack expectedItem;

    public AddItemAction(ItemStack pExpectedItem){
        expectedItem = pExpectedItem;
    }

    public static CauldronAction fromNetwork(FriendlyByteBuf pBuffer) {
        return new AddItemAction(pBuffer.readItem());
    }


    @Override
    public boolean PerformAction(PotionCauldronBE Cauldron, Player player){
        ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
        return ItemStack.matches(expectedItem,item);
    }

    //estos son necesarios para compartir la accion, ya que es un objeto como la receta
    public void toNetwork(FriendlyByteBuf buffer){
        //leer el caso del metodo a ver si corresponde ese valor de limitedTag
        buffer.writeItemStack(expectedItem, true);
    }

    @Override
    public String getType(){
        return "add_item";
    }

    public ItemStack getExpectedItem() {
        return expectedItem;
    }
}
