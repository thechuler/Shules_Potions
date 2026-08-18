package net.shule.shulespotions.MobEffects.Custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


import static net.minecraft.world.item.BoneMealItem.applyBonemeal;

public class FertilizerEffect extends MobEffect {
    public FertilizerEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }


    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Level level = pLivingEntity.level();
        boolean applied = false;
        BlockPos center = pLivingEntity.blockPosition();
        

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos currentPos = center.offset(x, y, z);
                    if (applyBonemeal(ItemStack.EMPTY, level, currentPos, null)) {
                        if (!level.isClientSide) {
                            level.levelEvent(1505, currentPos, 0);
                        }
                        applied = true;
                    }
                }
            }
        }

        if (applied) {
            return;
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int time = Math.max(10, 60 - (pAmplifier * 10));
        return pDuration % time == 0;
    }
}
