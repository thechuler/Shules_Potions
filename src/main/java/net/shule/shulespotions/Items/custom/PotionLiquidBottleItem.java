package net.shule.shulespotions.Items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.shule.shulespotions.Blocks.Entities.PotionCauldronBE;
import net.shule.shulespotions.Fluids.PotionFluidHelper;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.Potions.PotionLiquidUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PotionLiquidBottleItem extends Item {

    private static final String FLUID_TAG = "StoredFluid";
    private static final String EFFECTS_TAG = "ResolvedEffects";

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
            tag.remove(EFFECTS_TAG);
        }
    }

    /*
     *
     * =========================================
     * RESOLVED EFFECTS
     * =========================================
     *
     */

    public void setResolvedEffects(ItemStack stack,
                                   List<MobEffect> effects) {

        CompoundTag tag = stack.getOrCreateTag();

        ListTag list = new ListTag();

        for (MobEffect effect : effects) {

            CompoundTag effectTag = new CompoundTag();

            effectTag.putString(
                    "Id",
                    BuiltInRegistries.MOB_EFFECT
                            .getKey(effect)
                            .toString()
            );

            list.add(effectTag);
        }

        tag.put(EFFECTS_TAG, list);
    }

    public List<MobEffect> getResolvedEffects(ItemStack stack) {

        List<MobEffect> effects = new ArrayList<>();

        CompoundTag tag = stack.getTag();

        if (tag == null || !tag.contains(EFFECTS_TAG)) {
            return effects;
        }

        ListTag list =
                tag.getList(EFFECTS_TAG, Tag.TAG_COMPOUND);

        for (int i = 0; i < list.size(); i++) {

            CompoundTag effectTag =
                    list.getCompound(i);

            ResourceLocation id =
                    ResourceLocation.parse(
                            effectTag.getString("Id")
                    );

            MobEffect effect =
                    BuiltInRegistries.MOB_EFFECT.get(id);

            if (effect != null) {
                effects.add(effect);
            }
        }

        return effects;
    }

    /*
     *
     * =========================================
     * DRINKING
     * =========================================
     *
     */

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack,
                                              @NotNull Level level,
                                              @NotNull LivingEntity livingEntity) {

        Player player =
                livingEntity instanceof Player p ? p : null;

        if (player instanceof ServerPlayer serverPlayer) {

            CriteriaTriggers.CONSUME_ITEM.trigger(
                    serverPlayer,
                    stack
            );
        }

        if (!level.isClientSide) {
            applyPotion(stack, player);
            consumeFluid(stack, 250);
        }

        return super.finishUsingItem(
                stack,
                level,
                livingEntity
        );
    }

    protected void consumeFluid(ItemStack stack, int amount) {

        FluidStack fluid = getFluid(stack);

        fluid.shrink(amount);

        if (fluid.getAmount() <= 0) {
            removeFluid(stack);
        } else {
            setFluid(stack, fluid);
        }
    }

    public void applyPotion(ItemStack stack, LivingEntity entity) {
        FluidStack fluid = getFluid(stack);
        PotionLiquid pl = PotionFluidHelper.getPotionLiquid(fluid);

        if (fluid.isEmpty()) {
            return;
        }

        List<MobEffect> effects = getResolvedEffects(stack);

        int amplifier = Math.max(0, Math.min(4, pl.getStats().getPurity() / 20));
        
        if (pl.getStats().getDurationSeconds() <= 0) {
            return;
        }



        for (MobEffect effect : effects) {

            if (effect.isInstantenous()) {

                effect.applyInstantenousEffect(
                        entity,
                        entity,
                        entity,
                        amplifier,
                        1.0
                );

            } else {

                entity.addEffect(
                        new MobEffectInstance(
                                effect,
                                pl.getStats().getDurationTicks(),
                                amplifier
                        )
                );
            }
        }


        int vitality = pl.getStats().getVitality();

        float healthChange = (vitality / 100.0f) * entity.getMaxHealth();
        float newHealth = entity.getHealth() + healthChange;

        newHealth = Math.max(0, Math.min(newHealth, entity.getMaxHealth()));

        entity.setHealth(newHealth);


        if (entity instanceof Player player) {

            int flavor = pl.getStats().getFlavor();

            int hungerChange = Math.round((flavor / 100.0f) * 20);

            FoodData foodData = player.getFoodData();

            int newFood = Math.max(
                    0,
                    Math.min(foodData.getFoodLevel() + hungerChange, 20)
            );

            foodData.setFoodLevel(newFood);
        }



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
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player,
            @NotNull InteractionHand hand) {

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

        BlockEntity be = context.getLevel().getBlockEntity(context.getClickedPos());

        if (!(be instanceof PotionCauldronBE cauldron)) {
            return super.useOn(context);
        }

        if (context.getLevel().isClientSide) {
            return InteractionResult.sidedSuccess(true);
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

            copied.setAmount(Math.min(capacity, cauldronFluid.getAmount()));

            setFluid(stack, copied);

            PotionLiquid pl = PotionFluidHelper.getPotionLiquid(copied);

            List<MobEffect> effects = PotionLiquidUtils.resolve(pl);

            setResolvedEffects(stack, effects);

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
                    pitch + cauldron.getLevel()
                            .random.nextFloat() * 0.2F
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

        if (!FluidStack.areFluidStackTagsEqual(
                bottleFluid,
                cauldronFluid)) {

            return InteractionResult.FAIL;
        }

        /*
         *
         * =========================================
         * ADD MORE FLUID
         * =========================================
         *
         */

        int free =
                capacity - bottleFluid.getAmount();

        if (free <= 0) {
            return InteractionResult.FAIL;
        }

        FluidStack drained =
                cauldron.getTank().drain(
                        free,
                        IFluidHandler.FluidAction.EXECUTE
                );

        bottleFluid.grow(drained.getAmount());

        setFluid(stack, bottleFluid);

        return InteractionResult.SUCCESS;
    }



    @Override
    public void appendHoverText(@NotNull ItemStack stack,
                                @Nullable Level level,
                                List<Component> tooltip,
                                @NotNull TooltipFlag flag) {

        FluidStack fluid = getFluid(stack);

        if (fluid.isEmpty()) return;



        tooltip.add(
                Component.translatable(
                        "tooltip.shulespotions.alchemist_monocle_uses",
                        fluid.getAmount() / 250
                ).withStyle(ChatFormatting.WHITE)
        );


        PotionLiquid pl =
                PotionFluidHelper.getPotionLiquid(fluid);



        tooltip.add(
                Component.translatable(
                        "tooltip.shulespotions.alchemist_monocle_duration",
                        pl.getStats().getDurationFormatted()
                ).withStyle(ChatFormatting.GREEN)
        );


        tooltip.add(Component.empty());


        List<MobEffect> effects =
                getResolvedEffects(stack);



        if (!effects.isEmpty()) {

            MutableComponent line =
                    Component.translatable(
                            "tooltip.shulespotions.effects"
                    );


            boolean first = true;

            for (MobEffect effect : effects) {

                if (!first) {

                    line.append(
                            Component.literal(" | ")
                                    .withStyle(ChatFormatting.DARK_GRAY)
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



}