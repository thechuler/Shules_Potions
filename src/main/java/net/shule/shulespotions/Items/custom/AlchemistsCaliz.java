package net.shule.shulespotions.Items.custom;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.shule.shulespotions.Potions.PotionLiquid;

public class AlchemistsCaliz extends Item {
    private PotionLiquid liquid;


    public AlchemistsCaliz(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        return super.useOn(pContext);

    }




    public void setLiquid(PotionLiquid liquid) {
        this.liquid = liquid;
    }

    public PotionLiquid getLiquid() {
        return liquid;
    }


}
