package net.shule.shulespotions.Blocks.Entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.shule.shulespotions.Blocks.ModBlockEntities;

public class SpikeBE extends BlockEntity {
    public SpikeBE( BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SPIKE_BE.get(), pPos, pBlockState);
    }
    private ItemStack storedPotion = ItemStack.EMPTY;


    public void setPotionData(ItemStack potionStack) {
        this.storedPotion = potionStack.copy();
        this.sync();
    }

    public ItemStack getStoredPotion() {
        return storedPotion;
    }

    public void sync() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.storedPotion.isEmpty()) {
            tag.put("StoredPotion", this.storedPotion.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains("StoredPotion")) {
            this.storedPotion = ItemStack.of(tag.getCompound("StoredPotion"));
        } else {
            this.storedPotion = ItemStack.EMPTY;
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            load(tag);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }
}
