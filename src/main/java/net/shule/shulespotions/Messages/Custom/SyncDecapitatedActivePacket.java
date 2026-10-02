package net.shule.shulespotions.Messages.Custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.shule.shulespotions.Messages.ClientPacketHandler;

import java.util.function.Supplier;

public class SyncDecapitatedActivePacket {
    public final int entityId;
    public final boolean isActive;

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
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleDecapitatedActive(msg));
        });
        ctx.get().setPacketHandled(true);
    }
}
