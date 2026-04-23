package net.shule.shulespotions.Items.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Potions.PotionLiquid;

public abstract class PotionLiquidContainerItem extends Item  {

    public PotionLiquidContainerItem(Properties pProperties) {
        super(pProperties);
    }

    public void setPotionLiquid(ItemStack stack, PotionLiquid liquid) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put("PotionLiquid", liquid.save());
    }

    public PotionLiquid getPotionLiquid(ItemStack stack) {
        CompoundTag tag = stack.getTag();

        if (tag != null && tag.contains("PotionLiquid")) {
            return PotionLiquid.load(tag.getCompound("PotionLiquid"));
        }
        return null;
    }


    public boolean hasPotionLiquid(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("PotionLiquid");
    }

    public void removePotionLiquid(ItemStack stack) {
        CompoundTag tag = stack.getTag();

        if (tag != null && tag.contains("PotionLiquid")) {
            tag.remove("PotionLiquid");

            if (tag.isEmpty()) {
                stack.setTag(null);
            }
        }
    }

}
