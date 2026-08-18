package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.shule.shulespotions.Entities.entity.SpinningBlockEntity;
import net.shule.shulespotions.MobEffects.ModMobEffects;
import net.shule.shulespotions.ShulesPotions;

@Mod.EventBusSubscriber(modid = ShulesPotions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HighGravityEffect extends MobEffect {

    public HighGravityEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        // Reducimos la velocidad progresivamente. -0.15 significa -15% por nivel (debido a MULTIPLY_TOTAL y como escala el multiplicador internamente)
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                "a1b2c3d4-e5f6-7890-1234-56789abcdef0",
                -0.15D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.isNoGravity() && !pLivingEntity.onGround()) {
            double extraGravity = 0.04D + (0.01D * pAmplifier);
            Vec3 vec3 = pLivingEntity.getDeltaMovement();
            pLivingEntity.setDeltaMovement(vec3.x, vec3.y - extraGravity, vec3.z);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();

        if (level.isClientSide) return;

        if (entity.hasEffect(ModMobEffects.HIGH_GRAVITY.get())) {
            int amplifier = entity.getEffect(ModMobEffects.HIGH_GRAVITY.get()).getAmplifier();

            if (amplifier >= 2 && event.getDistance() > 3.0F) {
                int radius = Math.min(5, (int)(event.getDistance() / 3));
                BlockPos center = entity.blockPosition().below();

                level.playSound(null, center, net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x*x + z*z <= radius*radius) {

                        if (level.random.nextFloat() > 0.3f) {

                            BlockPos pos = center.offset(x, 0, z);
                            BlockState state = level.getBlockState(pos);


                            if (!state.isAir() && state.getDestroySpeed(level, pos) != -1.0F && state.getBlock() != Blocks.WATER && state.getBlock() != Blocks.LAVA) {

                                if (level.random.nextBoolean()) {
                                    level.destroyBlock(pos, false);
                                } else {

                                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                                    SpinningBlockEntity spinningBlock = new SpinningBlockEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, state);

                                    double dX = (pos.getX() - center.getX()) * 0.15D + (level.random.nextDouble() - 0.5D) * 0.1D;
                                    double dY = 0.4D + level.random.nextDouble() * 0.3D + (event.getDistance() * 0.02D);
                                    double dZ = (pos.getZ() - center.getZ()) * 0.15D + (level.random.nextDouble() - 0.5D) * 0.1D;
                                    
                                    spinningBlock.setDeltaMovement(dX, dY, dZ);
                                    level.addFreshEntity(spinningBlock);
                                }
                            }
                        }
                    }
                }
            }
            }
        }
    }
}
