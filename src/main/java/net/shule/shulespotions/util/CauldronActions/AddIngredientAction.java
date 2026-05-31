package net.shule.shulespotions.util.CauldronActions;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.shule.shulespotions.Potions.IngredientStat;
import net.shule.shulespotions.Potions.ItemStatRegistry;
import net.shule.shulespotions.Potions.PotionLiquid;

public class AddIngredientAction extends CauldronAction {

    private final Item item;

    public AddIngredientAction(Item item) {
        this.item = item;
    }

    @Override
    public void apply(CauldronContext ctx) {

        PotionLiquid potion = ctx.getPotion();

        IngredientStat stats = ItemStatRegistry.get(new ItemStack(item));

        potion.addStats(stats);

        ctx.getCauldron().setPotionLiquid(potion);
    }

    public Item getItem() {
        return item;
    }

    @Override
    public CompoundTag save() {

        CompoundTag tag = new CompoundTag();

        tag.putString("Item", BuiltInRegistries.ITEM.getKey(item).toString());

        return tag;
    }

    public static AddIngredientAction load(CompoundTag tag) {

        Item item = BuiltInRegistries.ITEM.get(
                ResourceLocation.parse(
                        tag.getString("Item")));

        return new AddIngredientAction(item);
    }

    @Override
    public String getType() {
        return "add_ingredient";
    }
}