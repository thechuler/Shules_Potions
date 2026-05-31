package net.shule.shulespotions.util.CauldronActions;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CauldronActionRegistry {

    private static final Map<String, Function<CompoundTag, CauldronAction>>
            LOADERS = new HashMap<>();

    public static void register(String id, Function<CompoundTag, CauldronAction> loader){
        LOADERS.put(id, loader);
    }

    public static CauldronAction load(String id, CompoundTag tag) {
        Function<CompoundTag, CauldronAction> loader = LOADERS.get(id);

        if (loader == null) {
            throw new IllegalStateException(
                    "Unknown action type: " + id
            );
        }

        return loader.apply(tag);
    }
}