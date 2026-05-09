package net.shule.shulespotions.Potions;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemStatRegistry {

    private static final Map<Item, IngredientStat> ITEM_STATS = new HashMap<>();

    public static void clear() {
        ITEM_STATS.clear();
    }

    public static void register(Item item, IngredientStat stats) {
        ITEM_STATS.put(item, stats);
    }

    public static IngredientStat get(ItemStack stack) {

        IngredientStat base = ITEM_STATS.get(stack.getItem());

        if (base == null) {
            return new IngredientStat();
        }

        return new IngredientStat(
                base.getPurity(),
                base.getVitality(),
                base.getFlavor(),
                base.getStability()
        );
    }
}
