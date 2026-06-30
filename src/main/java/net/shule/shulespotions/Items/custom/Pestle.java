package net.shule.shulespotions.Items.custom;

import net.minecraft.core.BlockPos;
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

        mortar.grind();

        return InteractionResult.SUCCESS;
    }
}
