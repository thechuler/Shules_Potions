package net.shule.shulespotions.Blocks.Entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import net.shule.shulespotions.Blocks.ModBlockEntities;
import net.shule.shulespotions.Particles.ModParticles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OminousPedestalBE extends AncientPedestalBE {

    private boolean isActiveEvent = false;
    private boolean eventCompleted = false;
    private int currentWave = 0;
    private List<UUID> spawnedEnemies = new ArrayList<>();
    private int tickCounter = 0;

    public OminousPedestalBE(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.OMINOUS_PEDESTAL_BE.get(), pPos, pBlockState);
    }

    @Override
    public double getItemRenderYOffset() {
        return 1.50D;
    }

    @Override
    public boolean setItem(ItemStack newItem) {
        boolean success = super.setItem(newItem);
        if (success && !level.isClientSide()) {
            startEvent();
        }
        return success;
    }

    public void startEvent() {
        this.isActiveEvent = true;
        this.eventCompleted = false;
        this.currentWave = 0;
        this.spawnedEnemies.clear();
        this.sync();
    }

    public void completeEvent() {
        this.isActiveEvent = false;
        this.eventCompleted = true;
        this.currentWave = 0;
        this.spawnedEnemies.clear();

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, worldPosition, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.BLOCKS, 1.0F, 1.0F);

            for (int i = 0; i < 360; i += 10) {
                double angle = i * Math.PI / 180.0;
                for(double r = 1.0; r <= 4.0; r += 1.0) {
                    double x = worldPosition.getX() + 0.5 + Math.cos(angle) * r;
                    double z = worldPosition.getZ() + 0.5 + Math.sin(angle) * r;
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, worldPosition.getY() + 0.5, z, 1, 0, 0, 0, 0);
                }
            }
        }
        this.sync();
    }

    public void resetEvent() {
        if (level instanceof ServerLevel serverLevel) {
            for (UUID uuid : spawnedEnemies) {
                Entity entity = serverLevel.getEntity(uuid);
                if (entity != null) {
                    entity.discard();
                }
            }
        }
        this.spawnedEnemies.clear();
        this.currentWave = 0;
        this.isActiveEvent = false;
        this.eventCompleted = false;
        this.tickCounter = 0;
        this.sync();
    }

    private void checkEnemies() {
        if (level instanceof ServerLevel serverLevel) {
            spawnedEnemies.removeIf(uuid -> {
                Entity entity = serverLevel.getEntity(uuid);
                return entity == null || !entity.isAlive();
            });
        }
    }

    private void spawnWave(int waveNumber) {
        this.currentWave = waveNumber;
        int numEnemies = waveNumber * 2 + 1;

        if (level instanceof ServerLevel serverLevel) {
            TagKey<EntityType<?>> tagKey = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("shulespotions", "ominous_pedestal_enemies"));
            var optionalList = ForgeRegistries.ENTITY_TYPES.tags().getTag(tagKey);

            List<EntityType<?>> validTypes = new ArrayList<>();
            if (!optionalList.isEmpty()) {
                validTypes.addAll(optionalList.stream().toList());
            } else {
                validTypes.add(EntityType.ZOMBIE);
            }

            int spawnRange = 4;

            for (int i = 0; i < numEnemies; i++) {
                EntityType<?> type = validTypes.get(serverLevel.random.nextInt(validTypes.size()));
                

                for (int attempts = 0; attempts < 10; attempts++) {

                    double spawnX = worldPosition.getX() + (serverLevel.random.nextDouble() - serverLevel.random.nextDouble()) * spawnRange + 0.5D;
                    double spawnZ = worldPosition.getZ() + (serverLevel.random.nextDouble() - serverLevel.random.nextDouble()) * spawnRange + 0.5D;

                    double spawnY = worldPosition.getY() + serverLevel.random.nextInt(2);


                    if (serverLevel.noCollision(type.getAABB(spawnX, spawnY, spawnZ))) {
                        Entity entity = type.create(serverLevel);
                        
                        if (entity instanceof Mob mob) {
                            mob.setPos(spawnX, spawnY, spawnZ);
                            mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(BlockPos.containing(spawnX, spawnY, spawnZ)), MobSpawnType.EVENT, null, null);
                            serverLevel.addFreshEntity(mob);
                            spawnedEnemies.add(mob.getUUID());
                            break;
                        }
                    }
                }
            }
        }
        this.sync();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, OminousPedestalBE pBlockEntity) {
        pBlockEntity.serverTick();
    }

    private void serverTick() {
        if (level == null || level.isClientSide) return;

        if (this.hasItem() && !eventCompleted && !isActiveEvent) {
            AABB triggerArea = new AABB(worldPosition).inflate(14.0);
            List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, triggerArea, 
                p -> p.isAlive() && !p.isSpectator() && !p.isCreative());
            if (!nearbyPlayers.isEmpty()) {
                startEvent();
            }
        }

        if (!isActiveEvent) return;

        tickCounter++;


        if (tickCounter % 20 == 0) {
            AABB searchArea = new AABB(worldPosition).inflate(20.0);
            List<Player> players = level.getEntitiesOfClass(Player.class, searchArea, 
                p -> p.isAlive() && !p.isSpectator() && !p.isCreative());

            if (players.isEmpty()) {
                resetEvent();
                return;
            }

            checkEnemies();

            if (spawnedEnemies.isEmpty()) {
                if (currentWave >= 3) {
                    completeEvent();
                } else {
                    spawnWave(currentWave + 1);
                }
            }
        }


        if (tickCounter % 5 == 0 && level instanceof ServerLevel serverLevel) {
            for (UUID uuid : spawnedEnemies) {
                Entity entity = serverLevel.getEntity(uuid);
                if (entity instanceof LivingEntity livingEntity && livingEntity.isAlive()) {
                    serverLevel.sendParticles(ModParticles.OMINOUS_FIRE.get(),
                            entity.getX(), entity.getY() + entity.getBbHeight() + 0.5D, entity.getZ(),
                            1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("IsActiveEvent", isActiveEvent);
        tag.putBoolean("EventCompleted", eventCompleted);
        tag.putInt("CurrentWave", currentWave);

        ListTag listTag = new ListTag();
        for (UUID uuid : spawnedEnemies) {
            listTag.add(NbtUtils.createUUID(uuid));
        }
        tag.put("SpawnedEnemies", listTag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.isActiveEvent = tag.getBoolean("IsActiveEvent");
        this.eventCompleted = tag.getBoolean("EventCompleted");
        this.currentWave = tag.getInt("CurrentWave");

        this.spawnedEnemies.clear();
        if (tag.contains("SpawnedEnemies", Tag.TAG_LIST)) {
            ListTag listTag = tag.getList("SpawnedEnemies", Tag.TAG_INT_ARRAY);
            for (int i = 0; i < listTag.size(); i++) {
                this.spawnedEnemies.add(NbtUtils.loadUUID(listTag.get(i)));
            }
        }
    }

    public boolean isEventCompleted() {
        return eventCompleted;
    }

    public boolean isActiveEvent() {
        return isActiveEvent;
    }
    
    public int getCurrentWave() {
        return currentWave;
    }
}
