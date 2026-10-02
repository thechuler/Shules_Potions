package net.shule.shulespotions.Messages.Custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.shule.shulespotions.Messages.ClientPacketHandler;

import java.util.function.Supplier;

public class SyncBodyPartsPacket {
    public final int entityId;
    public final byte missingLimbsMask;
    public final int headEntityId;

    public SyncBodyPartsPacket(int entityId, byte missingLimbsMask, int headEntityId) {
        this.entityId = entityId;
        this.missingLimbsMask = missingLimbsMask;
        this.headEntityId = headEntityId;
    }

    public static void encode(SyncBodyPartsPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeByte(msg.missingLimbsMask);
        buf.writeInt(msg.headEntityId);
    }

    public static SyncBodyPartsPacket decode(FriendlyByteBuf buf) {
        return new SyncBodyPartsPacket(buf.readInt(), buf.readByte(), buf.readInt());
    }

    public static void handle(SyncBodyPartsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleBodyParts(msg));
        });
        ctx.get().setPacketHandled(true);
    }
}
