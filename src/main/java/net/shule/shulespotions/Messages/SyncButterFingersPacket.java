package net.shule.shulespotions.Messages;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

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
            if (Minecraft.getInstance().level != null) {
                Entity entity = Minecraft.getInstance().level.getEntity(msg.entityId);
                if (entity != null) {
                    if (msg.hasButterFingers) {
                        entity.getPersistentData().putBoolean("shulespotions:has_butter_fingers", true);
                    } else {
                        entity.getPersistentData().remove("shulespotions:has_butter_fingers");
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
