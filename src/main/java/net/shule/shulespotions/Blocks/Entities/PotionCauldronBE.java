package net.shule.shulespotions.Blocks.Entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.shule.shulespotions.Blocks.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class PotionCauldronBE extends BlockEntity {
    private float LIQUID_LEVEL;



    public PotionCauldronBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POTION_CAULDRON_BE.get(), pos, state);
        LIQUID_LEVEL = 0;
    }


    public void setLiquidLevel(float level) {
        this.LIQUID_LEVEL = level;
    }

    public float getLiquid_Level() {
        return LIQUID_LEVEL;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putFloat("LLevel", LIQUID_LEVEL);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        LIQUID_LEVEL = pTag.getFloat("LLevel");
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
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }
}

