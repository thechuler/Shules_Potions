package net.shule.shulespotions.Blocks.Entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.shule.shulespotions.Blocks.ModBlockEntities;
import net.shule.shulespotions.Items.ModItems;

public class SpoonRackBE extends BlockEntity {

    private final NonNullList<ItemStack> spoons = NonNullList.withSize(3, ItemStack.EMPTY);

    public SpoonRackBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SPOON_RACK_BE.get(), pos, state);
    }

    public ItemStack getSpoon(int slot) {
        return spoons.get(slot);
    }

    public void setSpoon(int slot, ItemStack stack) {
        spoons.set(slot, stack.copy());
        setChanged();
    }


    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        ContainerHelper.saveAllItems(tag, spoons);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        for (int i = 0; i < spoons.size(); i++) {
            spoons.set(i, ItemStack.EMPTY);
        }

        ContainerHelper.loadAllItems(tag, spoons);
    }

    public boolean addSpoon(ItemStack stack) {

        for (int i = 0; i < spoons.size(); i++) {

            if (spoons.get(i).isEmpty()) {

                spoons.set(i, stack.copyWithCount(1));

                setChanged();

                if(level != null) {level.sendBlockUpdated(worldPosition, getBlockState(),
                            getBlockState(), 3
                    );
                }
                return true;
            }
        }

        return false;
    }



    public ItemStack removeLastSpoon() {

        for (int i = spoons.size() - 1; i >= 0; i--) {

            ItemStack stack = spoons.get(i);

            if (!stack.isEmpty()) {

                ItemStack removed = stack.copy();

                spoons.set(i, ItemStack.EMPTY);

                setChanged();

                if(level != null) {
                    level.sendBlockUpdated(
                            worldPosition,
                            getBlockState(),
                            getBlockState(),
                            Block.UPDATE_ALL
                    );
                }

                return removed;
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }


}