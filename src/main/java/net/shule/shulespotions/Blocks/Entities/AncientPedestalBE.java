package net.shule.shulespotions.Blocks.Entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.shule.shulespotions.Blocks.ModBlockEntities;
import net.shule.shulespotions.Blocks.Custom.AncientPedestal;
import net.shule.shulespotions.Items.ModItems;

import javax.annotation.Nullable;
import java.util.List;

public class AncientPedestalBE extends BlockEntity {

    protected ItemStack item = ItemStack.EMPTY;
    protected NonNullList<ItemStack> displayItems = NonNullList.create();
    
    @Nullable
    protected ResourceLocation lootTable;
    protected long lootTableSeed;
    protected boolean hasBeenLooted = false;

    public AncientPedestalBE(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ANCIENT_PEDESTAL_BE.get(), pPos, pBlockState);
    }

    protected AncientPedestalBE(BlockEntityType<?> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Item", this.item.save(new CompoundTag()));
        
        ListTag listTag = new ListTag();
        for (ItemStack stack : this.displayItems) {
            if (!stack.isEmpty()) {
                listTag.add(stack.save(new CompoundTag()));
            }
        }
        tag.put("DisplayItems", listTag);
        
        if (this.lootTable != null) {
            tag.putString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                tag.putLong("LootTableSeed", this.lootTableSeed);
            }
        }
        tag.putBoolean("HasBeenLooted", this.hasBeenLooted);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Item", CompoundTag.TAG_COMPOUND)) {
            this.item = ItemStack.of(tag.getCompound("Item"));
        } else {
            this.item = ItemStack.EMPTY;
        }
        
        this.displayItems.clear();
        if (tag.contains("DisplayItems", Tag.TAG_LIST)) {
            ListTag listTag = tag.getList("DisplayItems", Tag.TAG_COMPOUND);
            for (int i = 0; i < listTag.size(); i++) {
                this.displayItems.add(ItemStack.of(listTag.getCompound(i)));
            }
        }
        
        if (tag.contains("LootTable", Tag.TAG_STRING)) {
            this.lootTable = ResourceLocation.tryParse(tag.getString("LootTable"));
            this.lootTableSeed = tag.getLong("LootTableSeed");
        }

        if (tag.contains("HasBeenLooted")) {
            this.hasBeenLooted = tag.getBoolean("HasBeenLooted");
        } else if (this.getType() == ModBlockEntities.ANCIENT_PEDESTAL_BE.get()
                || this.getType() == ModBlockEntities.OMINOUS_PEDESTAL_BE.get()) {
            this.displayItems.clear();
            this.item = ItemStack.EMPTY;
            this.lootTable = null;
            this.hasBeenLooted = false;
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level != null && !this.level.isClientSide) {
            this.unpackLootTable(null);
        }
    }

    public void unpackLootTable(@Nullable Player pPlayer) {
        if (this.level != null && this.level.getServer() != null) {
            if (this.hasBeenLooted) {
                return;
            }

            ResourceLocation rl = this.lootTable;
            if (rl == null && this.getBlockState().getBlock() instanceof AncientPedestal pedestalBlock) {
                rl = pedestalBlock.getPedestalLootTable();
            }
            
            if (rl != null && this.displayItems.isEmpty() && this.item.isEmpty()) {
                if (this.getType() == ModBlockEntities.ANCIENT_PEDESTAL_BE.get()) {
                    this.displayItems.add(new ItemStack(ModItems.COMMON_LOOT_BUNLDE.get()));
                    this.displayItems.add(new ItemStack(ModItems.UNCOMMON_LOOT_BUNLDE.get()));
                    this.displayItems.add(new ItemStack(ModItems.INUSUAL_LOOT_BUNLDE.get()));
                    this.displayItems.add(new ItemStack(ModItems.RARE_LOOT_BUNLDE.get()));
                    this.displayItems.add(new ItemStack(ModItems.EXOTIC_LOOT_BUNLDE.get()));
                    this.displayItems.add(new ItemStack(ModItems.ANCIENT_LOOT_BUNLDE.get()));
                    this.sync();
                } else {
                    LootTable loottable = this.level.getServer().getLootData().getLootTable(rl);
                    this.lootTable = null; 
                    
                    LootParams.Builder builder = new LootParams.Builder((ServerLevel)this.level)
                            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition));
                    if (pPlayer != null) {
                        builder.withLuck(pPlayer.getLuck()).withParameter(LootContextParams.THIS_ENTITY, pPlayer);
                    }

                    List<ItemStack> list = loottable.getRandomItems(builder.create(LootContextParamSets.CHEST), this.lootTableSeed);
                    if (!list.isEmpty()) {
                        for (ItemStack stack : list) {
                            if (!stack.isEmpty()) {
                                this.displayItems.add(stack);
                            }
                        }
                        this.sync();
                    }
                }
            }
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

    public boolean hasItem() {
        return !this.item.isEmpty() || !this.displayItems.isEmpty();
    }

    public ItemStack getItem() {
        if (!this.item.isEmpty()) {
            return this.item;
        }
        if (!this.displayItems.isEmpty()) {
            if (this.level != null) {
                long time = this.level.getGameTime();
                int index = (int) ((time / 20) % this.displayItems.size());
                return this.displayItems.get(index);
            }
            return this.displayItems.get(0);
        }
        return ItemStack.EMPTY;
    }
    
    public NonNullList<ItemStack> getDisplayItems() {
        return displayItems;
    }

    public double getItemRenderYOffset() {
        return 0.75D;
    }

    public void sync() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    public boolean setItem(ItemStack newItem) {
        if (!this.item.isEmpty() || newItem.isEmpty()) {
            return false;
        }
        this.item = newItem.copy();
        this.displayItems.clear();
        this.hasBeenLooted = false;
        sync();
        return true;
    }

    public ItemStack removeItem() {
        return removeItem(null);
    }

    public ItemStack removeItem(@Nullable Player player) {
        this.hasBeenLooted = true;
        if (!this.displayItems.isEmpty()) {
            ItemStack removed = ItemStack.EMPTY;

            if (this.getType() == ModBlockEntities.ANCIENT_PEDESTAL_BE.get() && this.level instanceof ServerLevel serverLevel) {
                ResourceLocation rl = this.lootTable;
                if (rl == null && this.getBlockState().getBlock() instanceof AncientPedestal pedestalBlock) {
                    rl = pedestalBlock.getPedestalLootTable();
                }

                if (rl != null) {
                    LootTable loottable = serverLevel.getServer().getLootData().getLootTable(rl);
                    LootParams.Builder builder = new LootParams.Builder(serverLevel)
                            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition));
                    if (player != null) {
                        builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
                    }
                    List<ItemStack> list = loottable.getRandomItems(builder.create(LootContextParamSets.CHEST), this.lootTableSeed);
                    if (!list.isEmpty()) {
                        removed = list.get(0).copy();
                    }
                }
            }

            if (removed.isEmpty()) {
                int index = this.level != null ? this.level.random.nextInt(this.displayItems.size()) : 0;
                removed = this.displayItems.get(index).copy();
            }

            this.displayItems.clear();
            this.lootTable = null;
            sync();
            this.OnItemRemoved();
            return removed;
        }
        if (!this.item.isEmpty()) {
            ItemStack removed = this.item.copy();
            this.item = ItemStack.EMPTY;
            sync();
            this.OnItemRemoved();
            return removed;
        }
        return ItemStack.EMPTY;
    }

    public void clearItem() {
        this.item = ItemStack.EMPTY;
        this.displayItems.clear();
        this.hasBeenLooted = true;
        sync();
    }

    public boolean hasBeenLooted() {
        return this.hasBeenLooted;
    }

    public void setHasBeenLooted(boolean hasBeenLooted) {
        this.hasBeenLooted = hasBeenLooted;
    }

    public void OnItemRemoved() {

    }
}