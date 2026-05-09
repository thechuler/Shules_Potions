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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.shule.shulespotions.Blocks.Custom.PotionCauldron;
import net.shule.shulespotions.Blocks.ModBlockEntities;
import net.shule.shulespotions.Potions.IngredientStat;
import net.shule.shulespotions.Potions.ItemStatRegistry;
import net.shule.shulespotions.Potions.PotionLiquid;
import net.shule.shulespotions.util.ColorUtils;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.shule.shulespotions.util.ColorUtils.lerpColor;
import static net.shule.shulespotions.util.ColorUtils.randomColor;

public class PotionCauldronBE extends BlockEntity {

    private PotionLiquid pl;
    private int liquidLevel;
    private final List<Item> ingredients = new ArrayList<>();


    private int renderColor;
    private int startColor;
    private int targetColor;
    private float lerpProgress = 1.0f;


    public PotionCauldronBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POTION_CAULDRON_BE.get(), pos, state);
        liquidLevel = 3;
        pl = new PotionLiquid();
        renderColor = randomColor();
    }


    public void checkItem(ItemStack item) {
        if (this.level == null || this.level.isClientSide) return;

        if (ingredients.size() >= getMaxIngredients()) return;

        ingredients.add(item.getItem());

        int color = randomColor();
        startColorTransition(color);
        pl.setColor(color);
        // 1. Obtener stats del item
        IngredientStat stats = ItemStatRegistry.get(item);

        // 2. Sumarlos al líquido
        this.pl.addStats(stats);

        this.setChanged();
        sync();

        // 3. Debug
        sendDebugToNearbyPlayers(item, stats);
    }


    public PotionLiquid getPotionLiquid() {
        return pl;
    }


    private void sendDebugToNearbyPlayers(ItemStack item, IngredientStat addedStats) {

        double radius = 10.0;

        String message = "Item: " + item.getHoverName().getString() +
                " | +" +
                " P:" + addedStats.getPurity() +
                " V:" + addedStats.getVitality() +
                " F:" + addedStats.getFlavor() +
                " S:" + addedStats.getStability() +
                " || TOTAL ->" +
                " P:" + pl.getStats().getPurity() +
                " V:" + pl.getStats().getVitality() +
                " F:" + pl.getStats().getFlavor() +
                " S:" + pl.getStats().getStability();

        for (Player player : this.level.players()) {
            if (player.distanceToSqr(
                    this.worldPosition.getX() + 0.5,
                    this.worldPosition.getY() + 0.5,
                    this.worldPosition.getZ() + 0.5
            ) <= radius * radius) {

                player.sendSystemMessage(Component.literal(message));
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
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("sppotionliquid", this.pl.save());
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
        renderColor = pTag.getInt("SPcolor");
        if (pTag.contains("sppotionliquid")) {
            this.pl = PotionLiquid.load(pTag.getCompound("sppotionliquid"));
        } else {
            this.pl = new PotionLiquid();
        }

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


    public void setLiquidLevel(int liquidLevel) {
        this.liquidLevel = liquidLevel;
    }

    public int getLiquidLevel() {
        return liquidLevel;
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




}
