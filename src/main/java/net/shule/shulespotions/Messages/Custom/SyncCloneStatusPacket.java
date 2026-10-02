package net.shule.shulespotions.Messages.Custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.shule.shulespotions.Messages.ClientPacketHandler;

import java.util.function.Supplier;

public class SyncCloneStatusPacket {

    public final int entityId;
    public final boolean isClone;

    public SyncCloneStatusPacket(int entityId, boolean isClone) {
        this.entityId = entityId;
        this.isClone = isClone;
    }

    public static void encode(SyncCloneStatusPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeBoolean(msg.isClone);
    }

    public static SyncCloneStatusPacket decode(FriendlyByteBuf buf) {
        return new SyncCloneStatusPacket(buf.readInt(), buf.readBoolean());
    }

    public static void handle(SyncCloneStatusPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleCloneStatus(msg));
        });
        ctx.get().setPacketHandled(true);
    }
}
