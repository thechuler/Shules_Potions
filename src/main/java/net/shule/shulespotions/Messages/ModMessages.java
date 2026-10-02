package net.shule.shulespotions.Messages;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.shule.shulespotions.Messages.Custom.*;

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
                SyncDecapitatedActivePacket.class,
                SyncDecapitatedActivePacket::encode,
                SyncDecapitatedActivePacket::decode,
                SyncDecapitatedActivePacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                SyncBodyPartsPacket.class,
                SyncBodyPartsPacket::encode,
                SyncBodyPartsPacket::decode,
                SyncBodyPartsPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                ControlHeadPacket.class,
                ControlHeadPacket::encode,
                ControlHeadPacket::decode,
                ControlHeadPacket::handle
        );
    }
}