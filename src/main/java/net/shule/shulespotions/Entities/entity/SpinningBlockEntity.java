package net.shule.shulespotions.Entities.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import net.shule.shulespotions.Entities.ModEntities;

public class SpinningBlockEntity extends Entity {

    private static final EntityDataAccessor<Integer> DATA_BLOCK_STATE_ID = SynchedEntityData.defineId(SpinningBlockEntity.class, EntityDataSerializers.INT);

    public SpinningBlockEntity(EntityType<? extends Entity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.blocksBuilding = true;
    }

    public SpinningBlockEntity(Level pLevel, double pX, double pY, double pZ, BlockState pState) {
        this(ModEntities.SPINNING_BLOCK.get(), pLevel);
        this.setPos(pX, pY, pZ);
        this.setBlockState(pState);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_BLOCK_STATE_ID, Block.getId(Blocks.SAND.defaultBlockState()));
    }

    public BlockState getBlockState() {
        return Block.stateById(this.entityData.get(DATA_BLOCK_STATE_ID));
    }

    public void setBlockState(BlockState pState) {
        this.entityData.set(DATA_BLOCK_STATE_ID, Block.getId(pState));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getBlockState().isAir()) {
            this.discard();
            return;
        }

        this.move(MoverType.SELF, this.getDeltaMovement());

        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }
        

        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));

        if (!this.level().isClientSide) {

            if (this.onGround()  || this.tickCount > 100) {
                BlockPos pos = this.blockPosition();
                

                if (!this.level().getBlockState(pos).canBeReplaced()) {
                    pos = pos.above();
                }
                
                if (this.level().getBlockState(pos).canBeReplaced() || this.level().getBlockState(pos).isAir()) {
                    this.level().setBlock(pos, this.getBlockState(), 3);
                } else {

                    Block.dropResources(this.getBlockState(), this.level(), pos);
                }
                
                this.discard();
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.setBlockState(NbtUtils.readBlockState(this.level().holderLookup(net.minecraft.core.registries.Registries.BLOCK), pCompound.getCompound("BlockState")));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.put("BlockState", NbtUtils.writeBlockState(this.getBlockState()));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
