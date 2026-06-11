package net.shule.shulespotions.Entities.Projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.shule.shulespotions.Blocks.Entities.PotionSplashBE;
import net.shule.shulespotions.Blocks.ModBlocks;

public class PotionSplashProjectile extends ThrowableProjectile {

    public PotionSplashProjectile(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

    }

    private float scale = 1.0f;

    private static final EntityDataAccessor<Integer> POTION_COLOR = SynchedEntityData.defineId(PotionSplashProjectile.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData() {
        this.entityData.define(POTION_COLOR, 0x23b823);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);

        if (level().isClientSide)
            return;

        BlockPos hitPos = pResult.getBlockPos();
        BlockState hitState = level().getBlockState(hitPos);

        if (!hitState.isCollisionShapeFullBlock(level(), hitPos)) {
            discard();
            return;
        }

        BlockPos splashPos = hitPos.relative(pResult.getDirection());
        if (level().getBlockState(splashPos).isAir()) {

            BlockState splashState =
                    ModBlocks.POTION_SPLASH.get()
                            .defaultBlockState()
                            .setValue(
                                    MultifaceBlock.getFaceProperty(
                                            pResult.getDirection().getOpposite()
                                    ),
                                    true
                            );

            level().setBlock(
                    splashPos,
                    splashState,
                    Block.UPDATE_ALL
            );

            if (level().getBlockEntity(splashPos) instanceof PotionSplashBE be) {
                be.setColor(getPotionColor());
            }
        }

        discard();
    }



    @Override
    protected float getGravity() {
        return 0.06F;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("PotionColor", getPotionColor());
    }
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
       if (tag.contains("PotionColor")) {
            setPotionColor(tag.getInt("PotionColor"));
        }
    }

    public void setPotionColor(int color) {
        this.entityData.set(POTION_COLOR, color);
    }

    public int getPotionColor() {
        return this.entityData.get(POTION_COLOR);
    }
}
