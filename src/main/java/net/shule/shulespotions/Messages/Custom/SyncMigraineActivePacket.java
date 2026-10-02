package net.shule.shulespotions.Messages.Custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.shule.shulespotions.Messages.ClientPacketHandler;

import java.util.function.Supplier;

public class SyncMigraineActivePacket {
    public final int entityId;
    public final int amplifier;
    public final boolean isActive;

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
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleMigraineActive(msg));
        });
        ctx.get().setPacketHandled(true);
    }
}
