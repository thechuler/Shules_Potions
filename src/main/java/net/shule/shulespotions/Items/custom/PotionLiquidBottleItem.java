package net.shule.shulespotions.Items.custom;


import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;
import net.shule.shulespotions.Potions.PotionLiquid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.minecraft.world.InteractionResult.FAIL;
import static net.minecraft.world.InteractionResult.SUCCESS;

public class PotionLiquidBottleItem extends PotionLiquidContainerItem {
    private int tolerancy;
    private int useDuration;
    private int capacity;


    public PotionLiquidBottleItem(Properties pProperties, int pTolerancy, int pUseDuration, int pCapacity) {
        super(pProperties);
        tolerancy = pTolerancy;
        useDuration = pUseDuration;
        capacity = pCapacity;
    }


    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
        Player player = pLivingEntity instanceof Player ? (Player) pLivingEntity : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, pStack);
        }

        if (!pLevel.isClientSide) {
            setLiquidLevel(pStack,getLiquidLevel(pStack) - 1 );

            List<MobEffectInstance> effects = resolvePotionEffects(pStack);

            for (MobEffectInstance effect : effects) {
                assert player != null;
                player.addEffect(effect);
            }

            if(getLiquidLevel(pStack) <= 0){
                removePotionLiquid(pStack);
            }
        }

        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }


    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return this.useDuration;
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack =player.getItemInHand(hand);
      if(hasLiquidLevel(stack) && getLiquidLevel(stack) > 0) {
          return ItemUtils.startUsingInstantly(level, player, hand);
      }else{
          return InteractionResultHolder.pass(stack);
      }
    }


    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getLevel().isClientSide) return InteractionResult.PASS;

        BlockEntity be = pContext.getLevel().getBlockEntity(pContext.getClickedPos());
        if (be instanceof PotionCauldronBE cauldron && cauldron.hasPotionLiquid()) {
            ItemStack stack = pContext.getItemInHand();
            PotionLiquid cauldronPL = cauldron.getPotionLiquid();
            PotionLiquid bottlePL = getPotionLiquid(stack);
            int bottleLL = getLiquidLevel(stack);
            int cauldronLL = cauldron.getLiquidLevel();

            if (bottlePL == null && bottleLL == 0 && cauldronLL > 0) {
                int transfer = Math.min(capacity, cauldronLL);

                setPotionLiquid(stack, cauldronPL);
                setLiquidLevel(stack, transfer);

                cauldron.setLiquidLevel(cauldronLL - transfer);
                return SUCCESS;
            }

            if (!cauldronPL.equals(bottlePL)) return FAIL;

            if (bottleLL >= capacity) return FAIL;

            int transfer = Math.min(capacity - bottleLL, cauldronLL);

            setLiquidLevel(stack, bottleLL + transfer);
            cauldron.setLiquidLevel(cauldronLL - transfer);

            return SUCCESS;

        }


        return super.useOn(pContext);
    }


    private List<MobEffectInstance> resolvePotionEffects(ItemStack stack) {
        PotionLiquid pl = getPotionLiquid(stack);

        if (pl != null) {
            List<MobEffectInstance> effects = new ArrayList<>();

            for (MobEffect effect : pl.getEffect()) {
                effects.add(new MobEffectInstance(effect, pl.getDuration(), (int) pl.getPower()));
            }

            return effects;
        }

        return Collections.emptyList();
    }


    @Override
    public @NotNull String getDescriptionId() {
        return "potion";
    }


    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        if(getLiquidLevel(pStack )< 1) return;
        pTooltipComponents.add(Component.literal("LiquidLevel: " + (this.hasLiquidLevel(pStack) ? this.getLiquidLevel(pStack) : "-")).withStyle(ChatFormatting.WHITE));


        pTooltipComponents.add(Component.literal("Power: " + (this.hasPotionLiquid(pStack) ? this.getPotionLiquid(pStack).getPower() : "-")).withStyle(ChatFormatting.RED));

        pTooltipComponents.add(Component.literal("Purity: " + (this.hasPotionLiquid(pStack) ? this.getPotionLiquid(pStack).getPurity() : "-") + "%").withStyle(ChatFormatting.BLUE));

        pTooltipComponents.add(Component.literal("Duration: " + (this.hasPotionLiquid(pStack) ? this.getPotionLiquid(pStack).getDuration() : "-")).withStyle(ChatFormatting.GREEN));


        pTooltipComponents.add(Component.literal("Complexity: " + (this.hasPotionLiquid(pStack) ? this.getPotionLiquid(pStack).getComplexity() : "-")).withStyle(ChatFormatting.YELLOW));


        pTooltipComponents.add(Component.empty());

        if (this.hasPotionLiquid(pStack)) {
            MutableComponent line = Component.literal("Effects: ");
            boolean first = true;
            for (MobEffect effect : this.getPotionLiquid(pStack).getEffect()) {
                if (!first) {
                    line.append(Component.literal(" | ").withStyle(ChatFormatting.DARK_GRAY));
                }
                line.append(Component.literal(effect.getDisplayName().getString()).withStyle(style -> style.withColor(effect.getColor())));
                first = false;
            }
            pTooltipComponents.add(line);


        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }





    public void setLiquidLevel(ItemStack stack, int liquid) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("LiquidLevel", liquid);
    }

    public int getLiquidLevel(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("LiquidLevel")) {
            return tag.getInt("LiquidLevel");
        }
        return 0;
    }


    public boolean hasLiquidLevel(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("LiquidLevel");
    }


}
