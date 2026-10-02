package net.shule.shulespotions.Messages.Custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.shule.shulespotions.Entities.entity.PlayerHeadEntity;

import java.util.function.Supplier;

public class ControlHeadPacket {
    private final int headEntityId;
    private final float forward;
    private final float strafe;
    private final boolean jumping;
    private final float yRot;
    private final float xRot;

    public ControlHeadPacket(int headEntityId, float forward, float strafe, boolean jumping, float yRot, float xRot) {
        this.headEntityId = headEntityId;
        this.forward = forward;
        this.strafe = strafe;
        this.jumping = jumping;
        this.yRot = yRot;
        this.xRot = xRot;
    }

    public static void encode(ControlHeadPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.headEntityId);
        buf.writeFloat(msg.forward);
        buf.writeFloat(msg.strafe);
        buf.writeBoolean(msg.jumping);
        buf.writeFloat(msg.yRot);
        buf.writeFloat(msg.xRot);
    }

    public static ControlHeadPacket decode(FriendlyByteBuf buf) {
        return new ControlHeadPacket(
                buf.readInt(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readBoolean(),
                buf.readFloat(),
                buf.readFloat()
        );
    }

    public static void handle(ControlHeadPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                Entity entity = player.level().getEntity(msg.headEntityId);
                if (entity instanceof PlayerHeadEntity head) {
                    if (player.getUUID().equals(head.getPlayerUUID())) {
                        head.setYRot(msg.yRot);
                        head.setYHeadRot(msg.yRot);
                        head.setYBodyRot(msg.yRot);
                        head.setXRot(msg.xRot);

                        float f = msg.strafe * msg.strafe + msg.forward * msg.forward;
                        boolean hasInput = f >= 1.0E-4F;
                        float forwardNorm = 0.0F;
                        float strafeNorm = 0.0F;
                        float sin = Mth.sin(msg.yRot * ((float) Math.PI / 180F));
                        float cos = Mth.cos(msg.yRot * ((float) Math.PI / 180F));

                        if (hasInput) {
                            f = Mth.sqrt(f);
                            if (f < 1.0F) f = 1.0F;
                            forwardNorm = msg.forward / f;
                            strafeNorm = msg.strafe / f;
                        }

                        Vec3 currentMotion = head.getDeltaMovement();
                        if (msg.jumping) {
                            if (head.onGround()) {
                                double jumpY = 0.30D;
                                if (hasInput) {
                                    double hopSpeed = 0.14D;
                                    double mx = (strafeNorm * cos - forwardNorm * sin) * hopSpeed;
                                    double mz = (forwardNorm * cos + strafeNorm * sin) * hopSpeed;
                                    head.setDeltaMovement(mx, jumpY, mz);
                                } else {
                                    head.setDeltaMovement(currentMotion.x, jumpY, currentMotion.z);
                                }
                                head.hasImpulse = true;
                            } else if (head.isInWater()) {
                                double swimSpeed = hasInput ? 0.08D : 0.0D;
                                double mx = hasInput ? (strafeNorm * cos - forwardNorm * sin) * swimSpeed : currentMotion.x;
                                double mz = hasInput ? (forwardNorm * cos + strafeNorm * sin) * swimSpeed : currentMotion.z;
                                head.setDeltaMovement(mx, 0.08D, mz);
                                head.hasImpulse = true;
                            }
                        } else if (hasInput && head.onGround()) {
                            double crawlSpeed = 0.10D;
                            double mx = (strafeNorm * cos - forwardNorm * sin) * crawlSpeed;
                            double mz = (forwardNorm * cos + strafeNorm * sin) * crawlSpeed;
                            head.setDeltaMovement(mx, currentMotion.y, mz);
                            head.hasImpulse = true;
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
