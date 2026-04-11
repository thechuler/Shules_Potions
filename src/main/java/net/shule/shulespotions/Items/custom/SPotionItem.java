package net.shule.shulespotions.Items.custom;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

import net.minecraft.world.level.Level;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.Potions.PotionModifiers.PotionModifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class SPotionItem extends PotionLiquidContainerItem {


    public SPotionItem(Properties pProperties) {
        super(pProperties);

    }


    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return this.hasPotionLiquid(pStack) ? this.getPotionLiquid(pStack).getDuration() : 0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {

            RandomSource random = level.getRandom();

            int duration = 100 + random.nextInt(400); // 100-500 ticks
            int complexity = random.nextInt(10); // 0-9
            float power = 0.5f + random.nextFloat() * 2f; // 0.5 - 2.5
            float purity = random.nextFloat(); // 0 - 1
            int color = random.nextInt(0xFFFFFF); // color random

            List<MobEffect> possibleEffects = List.of(
                    MobEffects.MOVEMENT_SPEED,
                    MobEffects.DAMAGE_BOOST,
                    MobEffects.REGENERATION,
                    MobEffects.POISON,
                    MobEffects.NIGHT_VISION
            );

            List<MobEffect> effects = new ArrayList<>();

            int effectCount = 1 + random.nextInt(3);

            for (int i = 0; i < effectCount; i++) {
                MobEffect effect = possibleEffects.get(random.nextInt(possibleEffects.size()));
                if (!effects.contains(effect)) {
                    effects.add(effect);
                }
            }

            PotionLiquid liquid = new PotionLiquid(
                    duration,
                    complexity,
                    power,
                    purity,
                    effects,
                    color
            );


            setPotionLiquid(stack, liquid);


            player.sendSystemMessage(
                    Component.literal("Nuevo PotionLiquid generado!")
            );
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }


    @Override
    public String getDescriptionId() {
        return "caca";
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {


        pTooltipComponents.add(Component.literal("Power: " +
                        (this.hasPotionLiquid(pStack) ? this.getPotionLiquid(pStack).getPower() : "-"))
                .withStyle(ChatFormatting.RED));

        pTooltipComponents.add(Component.literal("Purity: " +
                        (this.hasPotionLiquid(pStack) ? this.getPotionLiquid(pStack).getPurity() : "-") + "%")
                .withStyle(ChatFormatting.BLUE));

        pTooltipComponents.add(Component.literal("Duration: " +
                        (this.hasPotionLiquid(pStack) ? this.getPotionLiquid(pStack).getDuration() : "-"))
                .withStyle(ChatFormatting.GREEN));


        pTooltipComponents.add(Component.literal("Complexity: " +
                        (this.hasPotionLiquid(pStack) ? this.getPotionLiquid(pStack).getComplexity() : "-"))
                .withStyle(ChatFormatting.YELLOW));


        pTooltipComponents.add(Component.empty());

        if (this.hasPotionLiquid(pStack)) {
            MutableComponent line = Component.literal("Effects: ");
            boolean first = true;
            for (MobEffect effect : this.getPotionLiquid(pStack).getEffect()) {
                if (!first) {
                    line.append(Component.literal(" | ").withStyle(ChatFormatting.DARK_GRAY));
                }
                line.append(
                        Component.literal(effect.getDisplayName().getString())
                                .withStyle(style -> style.withColor(effect.getColor())));
                first = false;
            }
            pTooltipComponents.add(line);


        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
