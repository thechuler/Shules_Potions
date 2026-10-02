package net.shule.shulespotions.Blocks.Entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import net.shule.shulespotions.Blocks.Custom.SpitterTrap;
import net.shule.shulespotions.Blocks.ModBlockEntities;
import net.shule.shulespotions.Fluids.PotionFluidHelper;
import net.shule.shulespotions.Items.custom.PotionLiquidBottleItem;
import net.shule.shulespotions.Particles.ModParticles;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.Sounds.ModSounds;

import java.util.List;

public class SpitterTrapBE extends BlockEntity {

    private static final int MAX_RANGE = 7;
    private static final int TICKS_PER_STEP = 1;

    private int streamProgress = 0;
    private int stepTimer = 0;
    private int animationTimer = 0;
    private Direction firingDirection = Direction.NORTH;
    
    private ItemStack storedPotion = ItemStack.EMPTY;

    public SpitterTrapBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SPITTER_TRAP_BE.get(), pos, state);
    }

    public void startAnimationTimer(int ticks) {
        this.animationTimer = ticks;
        this.sync();
    }

    public int getAnimationTimer() {
        return this.animationTimer;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SpitterTrapBE be) {
        if (!level.isClientSide) {
            if (be.animationTimer > 0) {
                be.animationTimer--;
                if (be.animationTimer == 0 && state.hasProperty(SpitterTrap.SPITTING)) {
                    level.setBlock(pos, state.setValue(SpitterTrap.SPITTING, Boolean.FALSE), Block.UPDATE_CLIENTS);
                }
            }

            if (be.streamProgress > 0) {
                be.stepTimer++;
                if (be.stepTimer >= TICKS_PER_STEP) {
                    be.stepTimer = 0;
                    if (level instanceof ServerLevel serverLevel) {
                        boolean continueShooting = be.spawnParticleStep(serverLevel, pos, be.firingDirection, be.streamProgress);
                        if (continueShooting && be.streamProgress < MAX_RANGE) {
                            be.streamProgress++;
                        } else {
                            be.streamProgress = 0;
                        }
                    }
                }
            }
        } else {
            if (be.animationTimer > 0) {
                be.animationTimer--;
            }
        }
    }

    public void shoot(ServerLevel level, BlockPos pos, Direction facing) {
        level.playSound(null, pos, ModSounds.BLOWING.get(), SoundSource.BLOCKS, 1F, 1F);

        this.firingDirection = facing;
        this.stepTimer = 0;

        boolean canContinue = spawnParticleStep(level, pos, facing, 1);
        this.streamProgress = canContinue ? 2 : 0;
    }

    private boolean spawnParticleStep(ServerLevel level, BlockPos pos, Direction facing, int distance) {
        BlockPos targetPos = pos.relative(facing, distance);
        BlockState targetState = level.getBlockState(targetPos);

        if (!targetState.isAir() && targetState.isSolidRender(level, targetPos)) {
            return false;
        }

        double startX = pos.getX() + 0.5D;
        double startY = pos.getY() + 0.5D;
        double startZ = pos.getZ() + 0.5D;

        double px = startX + facing.getStepX() * distance;
        double py = startY + facing.getStepY() * distance;
        double pz = startZ + facing.getStepZ() * distance;

        double r = 0.75D;
        double g = 0.20D;
        double b = 0.85D;

        if (!this.storedPotion.isEmpty() && this.storedPotion.getItem() instanceof PotionLiquidBottleItem bottle) {
            FluidStack fluid = bottle.getFluid(this.storedPotion);
            if (!fluid.isEmpty()) {
                PotionLiquid pl = PotionFluidHelper.getPotionLiquid(fluid);
                if (pl.getStats() != null) {
                    int color = pl.getStats().getColor();
                    r = ((color >> 16) & 0xFF) / 255.0D;
                    g = ((color >> 8) & 0xFF) / 255.0D;
                    b = (color & 0xFF) / 255.0D;
                }
            }
        }

        double sizeScale = 0.5D + (distance - 1) * (1.3D / (MAX_RANGE - 1));

        for (int p = 0; p < 8; p++) {
            double ox = (level.random.nextDouble() - 0.5) * 0.45;
            double oy = (level.random.nextDouble() - 0.5) * 0.45;
            double oz = (level.random.nextDouble() - 0.5) * 0.45;

            level.sendParticles(
                    ModParticles.POTION_SMOKE.get(),
                    px + ox,
                    py + oy,
                    pz + oz,
                    0,
                    r,
                    g,
                    b,
                    sizeScale
            );
        }

        AABB aabb = new AABB(targetPos);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb);
        for (LivingEntity entity : entities) {
            if (!this.storedPotion.isEmpty() && this.storedPotion.getItem() instanceof PotionLiquidBottleItem bottle) {
                bottle.applyPotion(this.storedPotion, null, null, entity, true);
            }
        }

        return true;
    }

    public void setPotionData(ItemStack potionStack) {
        this.storedPotion = potionStack.copy();
        this.sync();
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
        tag.putInt("StreamProgress", this.streamProgress);
        tag.putInt("AnimationTimer", this.animationTimer);
        tag.putInt("FiringDirection", this.firingDirection.get3DDataValue());
        
        if (!this.storedPotion.isEmpty()) {
            tag.put("StoredPotion", this.storedPotion.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.streamProgress = tag.getInt("StreamProgress");
        this.animationTimer = tag.getInt("AnimationTimer");
        if (tag.contains("FiringDirection")) {
            this.firingDirection = Direction.from3DDataValue(tag.getInt("FiringDirection"));
        }
        
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
