package net.shule.shulespotions.Messages;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncPotionSplashColorPacket {

    private final int entityId;
    private final int color;

    public SyncPotionSplashColorPacket(int entityId, int color) {
        this.entityId = entityId;
        this.color = color;
    }

    public static void encode(SyncPotionSplashColorPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.color);
    }

    public static SyncPotionSplashColorPacket decode(FriendlyByteBuf buf) {
        return new SyncPotionSplashColorPacket(buf.readInt(), buf.readInt());
    }

    public static void handle(SyncPotionSplashColorPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            Minecraft mc = Minecraft.getInstance();

            if (mc.level == null)
                return;

            Entity entity = mc.level.getEntity(msg.entityId);

            if (entity == null)
                return;

            entity.getPersistentData().putInt("PotionSplashColor", msg.color);
        });

        ctx.get().setPacketHandled(true);
    }
}