package net.shule.shulespotions.Messages;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncCloneStatusPacket {

    private final int entityId;
    private final boolean isClone;

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

            Minecraft mc = Minecraft.getInstance();

            if (mc.level == null)
                return;

            Entity entity = mc.level.getEntity(msg.entityId);

            if (entity == null)
                return;

            entity.getPersistentData().putBoolean("shulespotions:is_clone", msg.isClone);
        });

        ctx.get().setPacketHandled(true);
    }
}
