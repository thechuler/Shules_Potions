package net.shule.shulespotions.Messages;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE =
            NetworkRegistry.newSimpleChannel(ResourceLocation.fromNamespaceAndPath("shulespotions", "messages"),
                    () -> PROTOCOL_VERSION,
                    PROTOCOL_VERSION::equals,
                    PROTOCOL_VERSION::equals
            );

    private static int packetId = 0;

    public static void register() {

        INSTANCE.registerMessage(
                packetId++,
                SyncPotionSplashColorPacket.class,
                SyncPotionSplashColorPacket::encode,
                SyncPotionSplashColorPacket::decode,
                SyncPotionSplashColorPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                SyncCloneStatusPacket.class,
                SyncCloneStatusPacket::encode,
                SyncCloneStatusPacket::decode,
                SyncCloneStatusPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                SyncButterFingersPacket.class,
                SyncButterFingersPacket::encode,
                SyncButterFingersPacket::decode,
                SyncButterFingersPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                SyncMigraineActivePacket.class,
                SyncMigraineActivePacket::encode,
                SyncMigraineActivePacket::decode,
                SyncMigraineActivePacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                net.shule.shulespotions.Messages.SyncDecapitatedActivePacket.class,
                net.shule.shulespotions.Messages.SyncDecapitatedActivePacket::encode,
                net.shule.shulespotions.Messages.SyncDecapitatedActivePacket::decode,
                net.shule.shulespotions.Messages.SyncDecapitatedActivePacket::handle
        );
    }
}