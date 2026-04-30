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
import net.shule.shulespotions.Player.PlayerRecipesProvider;

public class RecipeBook extends Item {
    public RecipeBook(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide) {

            player.getCapability(PlayerRecipesProvider.PLAYER_RECIPES).ifPresent(data -> {

                NetworkHooks.openScreen((ServerPlayer) player,
                        new SimpleMenuProvider(
                                (id, inventory, p) -> new RecipeBookMenu(id, inventory, data.getRecipes()),
                                Component.literal("Recipe Book")
                        ),
                        buf -> {
                            buf.writeInt(data.getRecipes().size());
                            data.getRecipes().forEach(buf::writeResourceLocation);
                        }
                );

            });
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}
