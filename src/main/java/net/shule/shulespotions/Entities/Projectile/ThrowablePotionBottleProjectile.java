package net.shule.shulespotions.Entities.Projectile;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fluids.FluidStack;
import net.shule.shulespotions.Entities.ModEntities;
import net.shule.shulespotions.Fluids.PotionFluidHelper;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.Items.custom.PotionLiquidBottleItem;
import net.shule.shulespotions.Potions.PotionLiquid;

import java.util.List;

public class ThrowablePotionBottleProjectile extends ThrowableItemProjectile {

    public ThrowablePotionBottleProjectile(Level level, LivingEntity owner) {
        super(ModEntities.THROWABLE_POTION_BOTTLE_PROJECTILE.get(), owner, level);
    }

    public ThrowablePotionBottleProjectile(EntityType<? extends ThrowablePotionBottleProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.THROWABLE_POTION_BOTTLE.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (level().isClientSide) {
            return;
        }

        ItemStack stack = getItem();

        if (!(stack.getItem() instanceof PotionLiquidBottleItem bottle)) {
            discard();
            return;
        }

        FluidStack fluid = bottle.getFluid(stack);
        PotionLiquid liquid = PotionFluidHelper.getPotionLiquid(fluid);


        level().playSound(
                null,
                getX(),
                getY(),
                getZ(),
                SoundEvents.SPLASH_POTION_BREAK,
                SoundSource.NEUTRAL,
                1.0F,
                1.0F
        );


        level().levelEvent(
                2002,
                blockPosition(),
                liquid.getStats().getColor()
        );


        AABB area = getBoundingBox().inflate(4.0D);

        for (LivingEntity entity : level().getEntitiesOfClass(LivingEntity.class, area)) {
            bottle.applyPotion(stack, entity);
            
            // Añadir el efecto visual de "empapado en poción" y sincronizar el color
            entity.getPersistentData().putInt("PotionSplashColor", liquid.getStats().getColor());
            entity.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    net.shule.shulespotions.MobEffects.ModMobEffects.POTION_SPLASHED.get(),
                    20 * 15, 0, false, false
            ));
            
            net.shule.shulespotions.Messages.ModMessages.INSTANCE.send(
                    net.minecraftforge.network.PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                    new net.shule.shulespotions.Messages.SyncPotionSplashColorPacket(entity.getId(), liquid.getStats().getColor())
            );
        }

        discard();
    }
}