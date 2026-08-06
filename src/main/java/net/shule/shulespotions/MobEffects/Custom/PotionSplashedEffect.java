package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.network.PacketDistributor;
import net.shule.shulespotions.Messages.ModMessages;
import net.shule.shulespotions.Messages.SyncPotionSplashColorPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.shule.shulespotions.Blocks.Entities.PotionSplashBE;
import net.shule.shulespotions.Blocks.ModBlocks;

public class PotionSplashedEffect extends MobEffect {
    public PotionSplashedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        

        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                "7107DE5E-7CE8-4030-940E-514C1F160890",
                -0.35D,
               AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }


    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        
        pLivingEntity.getPersistentData().remove("PotionSplashColor");
        
        if (!pLivingEntity.level().isClientSide) {
            ModMessages.INSTANCE.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> pLivingEntity),
                new SyncPotionSplashColorPacket(pLivingEntity.getId(), -1)
            );
        }
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int pAmplifier) {
        if (!entity.level().isClientSide) {
            
            Level level = entity.level();
            BlockPos pos = entity.blockPosition();
            BlockPos posBelow = pos.below();
            
            if (level.getBlockState(posBelow).isFaceSturdy(level, posBelow, Direction.UP) 
                    && level.getBlockState(pos).canBeReplaced()) {
                
                BlockState splashState = 
                        ModBlocks.POTION_SPLASH.get().defaultBlockState()
                        .setValue(MultifaceBlock.getFaceProperty(Direction.DOWN), true);
                
                level.setBlock(pos, splashState, 3);
                
                if (level.getBlockEntity(pos) instanceof PotionSplashBE be) {
                    int color = entity.getPersistentData().getInt("PotionSplashColor");
                    be.setColor(color);
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true; // Le decimos a Minecraft que queremos procesar applyEffectTick todos los ticks
    }
}
