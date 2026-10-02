package net.shule.shulespotions.Messages.Custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.shule.shulespotions.Messages.ClientPacketHandler;

import java.util.function.Supplier;

public class SyncPotionSplashColorPacket {

    public final int entityId;
    public final int color;

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
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handlePotionSplashColor(msg));
        });
        ctx.get().setPacketHandled(true);
    }
}