package net.shule.shulespotions.Guis.Menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Guis.ModMenus;


public class RecipeBookMenu extends AbstractContainerMenu {

    public RecipeBookMenu(int id, Inventory inventory) {
        super(ModMenus.RECIPE_BOOK_MENU.get(), id);
    }
    public RecipeBookMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}
