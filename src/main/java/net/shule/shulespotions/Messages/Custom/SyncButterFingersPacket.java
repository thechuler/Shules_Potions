package net.shule.shulespotions.Messages.Custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.shule.shulespotions.Messages.ClientPacketHandler;

import java.util.function.Supplier;

public class SyncButterFingersPacket {
    public final int entityId;
    public final boolean hasButterFingers;

    public SyncButterFingersPacket(int entityId, boolean hasButterFingers) {
        this.entityId = entityId;
        this.hasButterFingers = hasButterFingers;
    }

    public static void encode(SyncButterFingersPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeBoolean(msg.hasButterFingers);
    }

    public static SyncButterFingersPacket decode(FriendlyByteBuf buf) {
        return new SyncButterFingersPacket(buf.readInt(), buf.readBoolean());
    }

    public static void handle(SyncButterFingersPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleButterFingers(msg));
        });
        ctx.get().setPacketHandled(true);
    }
}
