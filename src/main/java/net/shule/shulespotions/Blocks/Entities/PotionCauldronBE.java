package net.shule.shulespotions.Blocks.Entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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
import net.shule.shulespotions.Potions.IngredientStat;
import net.shule.shulespotions.Potions.ItemStatRegistry;
import net.shule.shulespotions.Potions.PotionLiquid;
import org.jetbrains.annotations.NotNull;


import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;


import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;

import static net.shule.shulespotions.Potions.PotionLiquidUtils.generatePotionColor;
import static net.shule.shulespotions.util.ColorUtils.lerpColor;
import static net.shule.shulespotions.util.ColorUtils.randomColor;

public class PotionCauldronBE extends BlockEntity {


    private final List<Item> ingredients = new ArrayList<>();
    private int renderColor;
    private int startColor;
    private int targetColor;
    private float lerpProgress = 1.0f;
    private final FluidTank tank = new FluidTank(1000) {

        @Override
        protected void onContentsChanged() {
            if (isEmpty() && !ingredients.isEmpty()) {
                ingredients.clear();
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

        if (ingredients.size() >= getMaxIngredients()) return;

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

            fluid = tank.getFluid();
        }


        PotionLiquid pl = PotionFluidHelper.getPotionLiquid(fluid);


        IngredientStat stats = ItemStatRegistry.get(item);

        pl.addStats(stats);


        int color = generatePotionColor(
                pl.getStats().getPurity(),
                pl.getStats().getVitality(),
                pl.getStats().getFlavor(),
                pl.getStats().getStability()
        );

        pl.setColor(color);


        PotionFluidHelper.withPotionLiquid(fluid, pl);


        ingredients.add(item.getItem());

        startColorTransition(color);

        setChanged();
        sync();
    }

    public PotionLiquid getPotionLiquid() {
        return PotionFluidHelper.getPotionLiquid(this.tank.getFluid());
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
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("SPTank", tank.writeToNBT(new CompoundTag()));
        pTag.putInt("SPcolor", renderColor);

        ListTag list = new ListTag();

        for (Item item : ingredients) {
            list.add(StringTag.valueOf(
                    BuiltInRegistries.ITEM.getKey(item).toString()
            ));
        }

        pTag.put("spingredients", list);
    }


    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        tank.readFromNBT(pTag.getCompound("SPTank"));
        renderColor = pTag.getInt("SPcolor");
        ingredients.clear();

        if (pTag.contains("spingredients")) {
            ListTag list = pTag.getList("spingredients", pTag.TAG_STRING);

            for (int i = 0; i < list.size(); i++) {
                String id = list.getString(i);
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
                ingredients.add(item);
            }
        }
    }


    private int getMaxIngredients() {
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

    public List<Item> getIngredients() {
        return ingredients;
    }

    public FluidTank getTank() {
        return tank;
    }
}
