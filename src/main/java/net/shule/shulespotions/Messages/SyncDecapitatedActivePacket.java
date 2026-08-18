package net.shule.shulespotions.Messages;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncDecapitatedActivePacket {
    private final int entityId;
    private final boolean isActive;

    public SyncDecapitatedActivePacket(int entityId, boolean isActive) {
        this.entityId = entityId;
        this.isActive = isActive;
    }

    public static void encode(SyncDecapitatedActivePacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeBoolean(msg.isActive);
    }

    public static SyncDecapitatedActivePacket decode(FriendlyByteBuf buf) {
        return new SyncDecapitatedActivePacket(buf.readInt(), buf.readBoolean());
    }

    public static void handle(SyncDecapitatedActivePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                Entity entity = level.getEntity(msg.entityId);
                if (entity != null) {
                    entity.getPersistentData().putBoolean("HasDecapitated", msg.isActive);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
