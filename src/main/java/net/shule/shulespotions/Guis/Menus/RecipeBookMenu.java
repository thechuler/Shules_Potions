package net.shule.shulespotions.Guis.Menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Guis.ModMenus;


import java.util.HashSet;
import java.util.Set;

public class RecipeBookMenu extends AbstractContainerMenu {

    private final Set<ResourceLocation> recipes;

    public RecipeBookMenu(int id, Inventory inventory, Set<ResourceLocation> recipes) {
        super(ModMenus.RECIPE_BOOK_MENU.get(), id);
        this.recipes = recipes;
    }

    public RecipeBookMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        super(ModMenus.RECIPE_BOOK_MENU.get(), id);

        int size = buf.readInt();
        this.recipes = new HashSet<>();

        for (int i = 0; i < size; i++) {
            this.recipes.add(buf.readResourceLocation());
        }
    }

    public Set<ResourceLocation> getRecipes() {
        return recipes;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}
