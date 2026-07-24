package net.shule.shulespotions.Loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {

    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
                    .and(ForgeRegistries.ITEMS.getCodec()
                            .fieldOf("item")
                            .forGetter(m -> m.item))
                    .and(Codec.INT.fieldOf("min")
                            .forGetter(m -> m.min))
                    .and(Codec.INT.fieldOf("max")
                            .forGetter(m -> m.max))
                    .and(Codec.FLOAT.fieldOf("chance")
                            .forGetter(m -> m.chance))
                    .apply(inst, AddItemModifier::new))
    );

    private final Item item;
    private final int min;
    private final int max;
    private final float chance;

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, int min, int max, float chance) {
        super(conditionsIn);
        this.item = item;
        this.min = min;
        this.max = max;
        this.chance = chance;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
        }

        if (context.getRandom().nextFloat() > chance) {
            return generatedLoot;
        }

        int amount = min;

        if (max > min) {
            amount += context.getRandom().nextInt(max - min + 1);
        }

        generatedLoot.add(new ItemStack(item, amount));

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}