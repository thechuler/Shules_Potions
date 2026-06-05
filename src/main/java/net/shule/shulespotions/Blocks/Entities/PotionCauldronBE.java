package net.shule.shulespotions.Blocks.Entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.shule.shulespotions.Blocks.Custom.PotionCauldron;
import net.shule.shulespotions.Blocks.ModBlockEntities;
import net.shule.shulespotions.Fluids.ModFluids;
import net.shule.shulespotions.Fluids.PotionFluidHelper;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.Potions.PotionLiquidUtils;
import net.shule.shulespotions.StabilityEvent.Events.PotionExplodeEvent;
import net.shule.shulespotions.util.CauldronActions.AddIngredientAction;
import net.shule.shulespotions.util.CauldronActions.CauldronAction;
import net.shule.shulespotions.util.CauldronActions.CauldronActionRegistry;
import net.shule.shulespotions.util.CauldronActions.CauldronContext;
import org.jetbrains.annotations.NotNull;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import static net.shule.shulespotions.util.ColorUtils.lerpColor;
import static net.shule.shulespotions.util.ColorUtils.randomColor;

public class PotionCauldronBE extends BlockEntity {



    private final List<CauldronAction> actions = new ArrayList<>();
    private int renderColor;
    private int startColor;
    private int targetColor;
    private float lerpProgress = 1.0f;
    private final FluidTank tank = new FluidTank(1000) {

        @Override
        protected void onContentsChanged() {
            if (isEmpty() && !actions.isEmpty()) {
                actions.clear();
            }
            setChanged();

            if (level != null && !level.isClientSide) {
                sync();
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            Fluid fluid = stack.getFluid();
            return fluid == Fluids.WATER ||
                    fluid == ModFluids.SOURCE_POTION_FLUID.get();
        }

    };


    private final LazyOptional<IFluidHandler> fluidHandler =
            LazyOptional.of(() -> tank);


    public PotionCauldronBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POTION_CAULDRON_BE.get(), pos, state);
        int color = randomColor();
        renderColor = color;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }


    public void checkItem(ItemStack item) {

        if (level == null || level.isClientSide) return;

        if (actions.size() >= getMaxIngredients()) return;

        if (tank.isEmpty()) return;

        FluidStack fluid = tank.getFluid();

        if (fluid.getFluid() == Fluids.WATER) {

            FluidStack potionFluid = new FluidStack(
                    ModFluids.SOURCE_POTION_FLUID.get(),
                    fluid.getAmount()
            );

            PotionFluidHelper.withPotionLiquid(
                    potionFluid,
                    new PotionLiquid()
            );

            tank.setFluid(potionFluid);
        }

        applyAction(new AddIngredientAction(item.getItem()), null);
    }



    public void applyAction(CauldronAction action, @Nullable Player player) {

        CauldronContext ctx = new CauldronContext(this, player);

        action.apply(ctx);

        actions.add(action);

        setChanged();
        sync();
    }




    public PotionLiquid getPotionLiquid() {
        return PotionFluidHelper.getPotionLiquid(this.tank.getFluid());
    }


    public void setPotionLiquid(PotionLiquid potion) {

        PotionLiquid oldPotion = getPotionLiquid();
        int oldStability = oldPotion.getStats().getStability();
        int newStability = potion.getStats().getStability();

        if (oldStability != newStability) {
            onStabilityChanged(oldStability, newStability);
        }

        FluidStack fluid = this.tank.getFluid();

        int color = PotionLiquidUtils.generatePotionColor(potion);

        potion.setColor(color);

        PotionFluidHelper.withPotionLiquid(fluid, potion);

        startColorTransition(color);


        sync();
        setChanged();
    }


    public void onStabilityChanged(int old, int current){
        CauldronContext ctx = new CauldronContext(this, null);

        PotionExplodeEvent event = new PotionExplodeEvent();

        if (event.canTrigger(ctx)) {
            float chance = event.getChance(ctx);

            if (level.random.nextFloat() < chance) {
                event.trigger(ctx);
            }
        }
    }




    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }


    public void sync() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }


    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }


    @Override
    protected void saveAdditional(CompoundTag tag) {

        super.saveAdditional(tag);

        tag.put("SPTank", tank.writeToNBT(new CompoundTag()));
        tag.putInt("SPcolor", renderColor);

        ListTag actionList = new ListTag();

        for (CauldronAction action : actions) {

            CompoundTag actionTag = new CompoundTag();

            actionTag.putString(
                    "Type",
                    action.getType()
            );

            actionTag.put(
                    "Data",
                    action.save()
            );

            actionList.add(actionTag);
        }

        tag.put("Actions", actionList);
    }

    @Override
    public void load(CompoundTag tag) {

        super.load(tag);

        tank.readFromNBT(tag.getCompound("SPTank"));

        renderColor = tag.getInt("SPcolor");

        actions.clear();

        if (tag.contains("Actions")) {

            ListTag actionList =
                    tag.getList("Actions", Tag.TAG_COMPOUND);

            for (int i = 0; i < actionList.size(); i++) {

                CompoundTag actionTag =
                        actionList.getCompound(i);

                String type =
                        actionTag.getString("Type");

                CompoundTag data =
                        actionTag.getCompound("Data");

                CauldronAction action =
                        CauldronActionRegistry.load(
                                type,
                                data
                        );

                actions.add(action);
            }
        }
    }


    public int getMaxIngredients() {
        if (this.level == null) return 0;

        BlockState state = this.getBlockState();
        if (state.getBlock() instanceof PotionCauldron cauldron) {
            return cauldron.getMAX_INGREDIENT_COUNT();
        }

        return 0;
    }




    public static void tick(Level level, PotionCauldronBE be) {
        if (level.isClientSide) return;

        if (be.lerpProgress < 1.0f) {
            be.lerpProgress += 0.05f;

            if (be.lerpProgress > 1.0f) be.lerpProgress = 1.0f;

            be.renderColor = lerpColor(be.startColor, be.targetColor, be.lerpProgress);

            be.setChanged();
            be.sync();
        }


    }



    public void startColorTransition(int newColor) {
        this.startColor = this.renderColor;
        this.targetColor = newColor;
        this.lerpProgress = 0.0f;
    }

    public int getRenderColor() {
        return this.renderColor;
    }

    public void setRenderColor(int renderColor) {
        this.renderColor = renderColor;
        setChanged();
        sync();
    }

    public List<CauldronAction> getActions() {
        return actions;
    }

    public FluidTank getTank() {
        return tank;
    }
}
