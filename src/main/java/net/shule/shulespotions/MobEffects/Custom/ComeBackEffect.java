package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ComeBackEffect extends MobEffect {
    public ComeBackEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource,
                                        LivingEntity entity, int amplifier, double health) {

        if (!(entity instanceof ServerPlayer player))
            return;

        BlockPos respawnPos = player.getRespawnPosition();

        if (respawnPos != null) {

            ServerLevel respawnLevel = player.server.getLevel(player.getRespawnDimension());

            if (respawnLevel != null) {

                Optional<Vec3> respawn = Player.findRespawnPositionAndUseSpawnBlock(
                        respawnLevel,
                        respawnPos,
                        player.getRespawnAngle(),
                        player.isRespawnForced(),
                        false
                );

                if (respawn.isPresent()) {
                    Vec3 pos = respawn.get();

                    player.teleportTo(
                            respawnLevel,
                            pos.x,
                            pos.y,
                            pos.z,
                            player.getYRot(),
                            player.getXRot()
                    );

                    return;
                }
            }
        }


        ServerLevel overworld = player.server.getLevel(Level.OVERWORLD);

        if (overworld != null) {
            BlockPos worldSpawn = overworld.getSharedSpawnPos();

            player.teleportTo(overworld, worldSpawn.getX() + 0.5,
                    worldSpawn.getY(), worldSpawn.getZ() + 0.5,
                    player.getYRot(), player.getXRot());
        }
    }
}
