package net.shule.shulespotions.Items.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.shule.shulespotions.Blocks.Entities.MortarBE;

public class Pestle extends Item {
    public Pestle(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (!(level.getBlockEntity(pos) instanceof MortarBE mortar))
            return InteractionResult.PASS;

        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        boolean lastHit = mortar.isOneBeforeFinish();

        mortar.grind();

        // 🔊 SONIDO
        level.playSound(
                null,
                pos,
                lastHit
                        ? SoundEvents.GRAVEL_HIT
                        : SoundEvents.STONE_HIT,
                net.minecraft.sounds.SoundSource.BLOCKS,
                0.8f,
                lastHit ? 0.7f : 1.2f
        );

        // 💨 PARTICULAS
        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {

            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.6;
            double z = pos.getZ() + 0.5;

            serverLevel.sendParticles(
                    lastHit
                            ? ParticleTypes.HAPPY_VILLAGER
                            : net.minecraft.core.particles.ParticleTypes.CRIT,
                    x, y, z,
                    lastHit ? 25 : 8,
                    0.25, 0.15, 0.25,
                    0.02
            );
        }

        return InteractionResult.SUCCESS;
    }
}
