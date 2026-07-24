package net.shule.shulespotions.Items.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.shule.shulespotions.Entities.Projectile.ThrowablePotionBottleProjectile;
import org.jetbrains.annotations.NotNull;

public class ThrowablePotionBottleItem extends PotionLiquidBottleItem{
    public ThrowablePotionBottleItem(Properties properties, int useDuration, int capacity) {
        super(properties, useDuration, capacity);
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level level,
            @NotNull Player player,
            @NotNull InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (hasFluid(stack)) {

            if (!level.isClientSide) {

                ThrowablePotionBottleProjectile splash =
                        new ThrowablePotionBottleProjectile(level, player);

                splash.setItem(stack.copy());

                splash.shootFromRotation(
                        player,
                        player.getXRot(),
                        player.getYRot(),
                        -20.0F,
                        0.5F,
                        1.0F
                );

                level.addFreshEntity(splash);

                stack.shrink(1);
            }

            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

}
