package net.shule.shulespotions.Potions;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ItemStatRegistry {

    private static final Map<Item, IngredientStat> ITEM_STATS = new HashMap<>();

    private static final Map<TagKey<Item>,IngredientStat> TAG_STATS = new HashMap<>();

    private static Map<Item, IngredientStat> CACHED_ENTRIES = null;

    public static void clear() {
        ITEM_STATS.clear();
        TAG_STATS.clear();
        CACHED_ENTRIES = null;
    }

    public static void register(Item item, IngredientStat stats) {
        ITEM_STATS.put(item, stats);
        CACHED_ENTRIES = null;
    }

    public static void registerTag(TagKey<Item> tag, IngredientStat stats) {
        TAG_STATS.put(tag, stats);
        CACHED_ENTRIES = null;
    }

    public static void invalidateCache() {
        CACHED_ENTRIES = null;
    }

    private static Map<Item, IngredientStat> buildCombinedEntries() {
        Map<Item, IngredientStat> combined = new HashMap<>();


        for (Map.Entry<TagKey<Item>, IngredientStat> entry : TAG_STATS.entrySet()) {
            BuiltInRegistries.ITEM.getTag(entry.getKey()).ifPresent(holders -> {
                for (Holder<Item> holder : holders) {
                    combined.put(holder.value(), entry.getValue());
                }
            });
        }
        combined.putAll(ITEM_STATS);

        return Collections.unmodifiableMap(combined);
    }

    public static IngredientStat get(ItemStack stack) {

        IngredientStat base = ITEM_STATS.get(stack.getItem());
        if (base == null) {
            base = stack.getTags().filter(TAG_STATS::containsKey).findFirst().map(TAG_STATS::get).orElse(null);
            if(base == null){
                return new IngredientStat();
            }

        }

        return new IngredientStat(base);
    }

    public static boolean hasStats(Item item) {
        if (ITEM_STATS.containsKey(item)) {
            return true;
        }
        return item.builtInRegistryHolder().tags().anyMatch(TAG_STATS::containsKey);
    }

    public static Map<Item, IngredientStat> getEntries() {
        if (CACHED_ENTRIES == null) {
            CACHED_ENTRIES = buildCombinedEntries();
        }
        return CACHED_ENTRIES;
    }
}
