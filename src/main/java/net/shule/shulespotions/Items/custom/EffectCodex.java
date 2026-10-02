package net.shule.shulespotions.Items.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.shule.shulespotions.Screens.ClientScreenHooks;

public class EffectCodex extends Item {
    public EffectCodex(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (pLevel.isClientSide) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientScreenHooks::openCodexScreen);
        }

        pLevel.playSound(
                pPlayer,
                pPlayer.getOnPos(),
                SoundEvents.BOOK_PAGE_TURN,
                SoundSource.PLAYERS
        );

        return InteractionResultHolder.success(stack);
    }
}
