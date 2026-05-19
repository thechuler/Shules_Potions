package net.shule.shulespotions.Items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;
import net.shule.shulespotions.Fluids.ModFluids;
import net.shule.shulespotions.Fluids.PotionFluidHelper;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.Potions.PotionLiquidUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotionLiquidBottleItem extends Item {

    private static final String FLUID_TAG = "StoredFluid";


    private final int useDuration;
    public final int capacity;

    public PotionLiquidBottleItem(Properties properties,
                                  int useDuration,
                                  int capacity) {

        super(properties);

        this.useDuration = useDuration;
        this.capacity = capacity;
    }

    /*
     *
     * =========================================
     * FLUID STORAGE
     * =========================================
     *
     */

    public void setFluid(ItemStack stack, FluidStack fluid) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put(FLUID_TAG, fluid.writeToNBT(new CompoundTag()));
    }


    public FluidStack getFluid(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(FLUID_TAG)) {
            return FluidStack.EMPTY;
        }
        return FluidStack.loadFluidStackFromNBT(
                tag.getCompound(FLUID_TAG)
        );
    }

    public boolean hasFluid(ItemStack stack) {
        return !getFluid(stack).isEmpty();
    }

    public void removeFluid(ItemStack stack) {

        CompoundTag tag = stack.getTag();

        if (tag != null) {
            tag.remove(FLUID_TAG);
        }
    }





    /*
     *
     * =========================================
     * DRINKING
     * =========================================
     *
     */

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity) {

        Player player = livingEntity instanceof Player p ? p : null;

        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
        }

        if (!level.isClientSide) {

            FluidStack fluid = getFluid(stack);

            if (!fluid.isEmpty()) {

                PotionLiquid pl = PotionFluidHelper.getPotionLiquid(fluid);

                List<MobEffect> effects = PotionLiquidUtils.resolve(pl);

                for (MobEffect effect : effects) {

                    if (player != null) {

                        player.addEffect(
                                new MobEffectInstance(
                                        effect,
                                        300
                                )
                        );
                    }
                }

                fluid.shrink(250);

                if (fluid.getAmount() <= 0) {
                    removeFluid(stack);

                } else {
                    setFluid(stack, fluid);
                }
            }
        }

        return super.finishUsingItem(stack, level, livingEntity);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return this.useDuration;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        FluidStack fluid = getFluid(stack);

        if (!fluid.isEmpty()) {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }

        return InteractionResultHolder.pass(stack);
    }

    /*
     *
     * =========================================
     * CAULDRON INTERACTION
     * =========================================
     *
     */

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {

        if (context.getLevel().isClientSide) {
            return InteractionResult.PASS;
        }

        BlockEntity be = context.getLevel().getBlockEntity(context.getClickedPos());

        if (!(be instanceof PotionCauldronBE cauldron)) {
            return super.useOn(context);
        }

        ItemStack stack = context.getItemInHand();

        FluidStack bottleFluid = getFluid(stack);

        FluidStack cauldronFluid = cauldron.getTank().getFluid();

        /*
         *
         * =========================================
         * FILL EMPTY BOTTLE
         * =========================================
         *
         */

        if (bottleFluid.isEmpty()) {

            if (cauldronFluid.isEmpty()) {
                return InteractionResult.FAIL;
            }

            FluidStack copied = cauldronFluid.copy();

            copied.setAmount(Math.min(capacity,cauldronFluid.getAmount()));
            setFluid(stack, copied);
            cauldron.getTank().drain(copied.getAmount(), IFluidHandler.FluidAction.EXECUTE);

            float pitch;

            if (capacity <= 250) {
                pitch = 2.0F;
            } else if (capacity <= 500) {
                pitch = 1.0F;
            } else {
                pitch = 0.6F;
            }

            cauldron.getLevel().playSound(
                    null,
                    context.getPlayer().getOnPos(),
                    SoundEvents.BOTTLE_FILL,
                    SoundSource.BLOCKS,
                    0.6F,
                    pitch +  cauldron.getLevel().random.nextFloat() * 0.2F
            );

            return InteractionResult.SUCCESS;
        }

        /*
         *
         * =========================================
         * SAME FLUID CHECK
         * =========================================
         *
         */

        if (!FluidStack.areFluidStackTagsEqual(bottleFluid, cauldronFluid)) {

            return InteractionResult.FAIL;
        }

        /*
         *
         * =========================================
         * ADD MORE FLUID
         * =========================================
         *
         */

        int free = capacity - bottleFluid.getAmount();

        if (free <= 0) {
            return InteractionResult.FAIL;
        }

        FluidStack drained = cauldron.getTank().drain(free, IFluidHandler.FluidAction.EXECUTE);

        bottleFluid.grow(drained.getAmount());

        setFluid(stack, bottleFluid);

        return InteractionResult.SUCCESS;
    }

    /*
     *
     * =========================================
     * TOOLTIP
     * =========================================
     *
     */

    @Override
    public void appendHoverText(@NotNull ItemStack stack,
                                @Nullable Level level,
                                List<Component> tooltip,
                                @NotNull TooltipFlag flag) {

        FluidStack fluid = getFluid(stack);

        if (fluid.isEmpty()) return;

        tooltip.add(
                Component.literal(
                        "Fluid: " +
                                fluid.getAmount() +
                                "mb"
                ).withStyle(ChatFormatting.WHITE)
        );

        PotionLiquid pl =
                PotionFluidHelper.getPotionLiquid(fluid);

        tooltip.add(
                Component.literal(
                        "Power: " + pl.getPower()
                ).withStyle(ChatFormatting.RED)
        );

        tooltip.add(
                Component.literal(
                        "Duration: " +
                                pl.getDuration() / 20
                ).withStyle(ChatFormatting.GREEN)
        );

        tooltip.add(Component.empty());

        List<MobEffect> effects =
                PotionLiquidUtils.resolve(pl);

        if (!effects.isEmpty()) {

            MutableComponent line =
                    Component.literal("Effects: ");

            boolean first = true;

            for (MobEffect effect : effects) {

                if (!first) {

                    line.append(
                            Component.literal(" | ")
                                    .withStyle(
                                            ChatFormatting.DARK_GRAY
                                    )
                    );
                }

                line.append(
                        Component.literal(
                                effect.getDisplayName()
                                        .getString()
                        ).withStyle(style ->
                                style.withColor(
                                        effect.getColor()
                                )
                        )
                );

                first = false;
            }

            tooltip.add(line);
        }

        super.appendHoverText(
                stack,
                level,
                tooltip,
                flag
        );
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "potion";
    }
}