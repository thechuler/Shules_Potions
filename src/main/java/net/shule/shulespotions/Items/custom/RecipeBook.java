package net.shule.shulespotions.Items.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.shule.shulespotions.Guis.Menus.RecipeBookMenu;

public class RecipeBook extends Item {
    public RecipeBook(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide) {
            NetworkHooks.openScreen((ServerPlayer) player,
                    new SimpleMenuProvider(
                            (id, inventory, p) -> new RecipeBookMenu(id, inventory),
                            Component.literal("Recipe Book")
                    )
            );
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}
