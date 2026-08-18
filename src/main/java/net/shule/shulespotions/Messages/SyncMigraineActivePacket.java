package net.shule.shulespotions.Messages;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncMigraineActivePacket {
    private final int entityId;
    private final int amplifier;
    private final boolean isActive;

    public SyncMigraineActivePacket(int entityId, int amplifier, boolean isActive) {
        this.entityId = entityId;
        this.amplifier = amplifier;
        this.isActive = isActive;
    }

    public static void encode(SyncMigraineActivePacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.amplifier);
        buf.writeBoolean(msg.isActive);
    }

    public static SyncMigraineActivePacket decode(FriendlyByteBuf buf) {
        return new SyncMigraineActivePacket(buf.readInt(), buf.readInt(), buf.readBoolean());
    }

    public static void handle(SyncMigraineActivePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                Entity entity = level.getEntity(msg.entityId);
                if (entity != null) {
                    if (msg.isActive) {
                        entity.getPersistentData().putBoolean("HasMigraine", true);
                        entity.getPersistentData().putInt("MigraineAmplifier", msg.amplifier);
                        entity.getPersistentData().putInt("MigraineStartTick", entity.tickCount);
                    } else {
                        entity.getPersistentData().putBoolean("HasMigraine", false);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
