package net.shule.shulespotions.dataGen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.shule.shulespotions.Items.ModItems;
import net.shule.shulespotions.Loot.AddItemModifier;
import net.shule.shulespotions.ShulesPotions;

import java.util.List;

public class ProviderLootModifier extends GlobalLootModifierProvider {
    public ProviderLootModifier(PackOutput output ) {
        super(output, ShulesPotions.MODID);
    }

    @Override
    protected void start() {

        add("bastion_fragment_treasure",
                new AddItemModifier(
                        new LootItemCondition[] {
                                LootTableIdCondition.builder(
                                        ResourceLocation.fromNamespaceAndPath(
                                                "minecraft",
                                                "chests/bastion_treasure"
                                        )
                                ).build()
                        },
                        ModItems.BASTION_FRAGMENT.get(),
                        2,
                        5,
                        0.5f
                ));


        add("bastion_fragment_bridge",
                new AddItemModifier(
                        new LootItemCondition[] {
                                LootTableIdCondition.builder(
                                        ResourceLocation.fromNamespaceAndPath(
                                                "minecraft",
                                                "chests/bastion_bridge"
                                        )
                                ).build()
                        },
                        ModItems.BASTION_FRAGMENT.get(),
                        2,
                        5,
                        0.4f
                ));


        add("bastion_fragment_hoglin_stable",
                new AddItemModifier(
                        new LootItemCondition[] {
                                LootTableIdCondition.builder(
                                        ResourceLocation.fromNamespaceAndPath(
                                                "minecraft",
                                                "chests/bastion_hoglin_stable"
                                        )
                                ).build()
                        },
                        ModItems.BASTION_FRAGMENT.get(),
                        2,
                        5,
                        0.3f
                ));


        add("bastion_fragment_other",
                new AddItemModifier(
                        new LootItemCondition[] {
                                LootTableIdCondition.builder(
                                        ResourceLocation.fromNamespaceAndPath(
                                                "minecraft",
                                                "chests/bastion_other"
                                        )
                                ).build()
                        },
                        ModItems.BASTION_FRAGMENT.get(),
                        2,
                        5,
                        0.3f
                ));




        add("ghast_hearth",
                new AddItemModifier(
                        new LootItemCondition[] {
                                LootTableIdCondition.builder(
                                        ResourceLocation.fromNamespaceAndPath(
                                                "minecraft",
                                                "entities/ghast"
                                        )
                                ).build()
                        },
                        ModItems.GHAST_HEART.get(),
                        1,
                        1,
                        0.1f
                ));

        add("allay_essence",
                new AddItemModifier(
                        new LootItemCondition[] {
                                LootTableIdCondition.builder(
                                        ResourceLocation.fromNamespaceAndPath(
                                                "minecraft",
                                                "entities/allay"
                                        )
                                ).build()
                        },
                        ModItems.ALLAY_ESSENCE.get(),
                        1,
                        1,
                        1f
                ));
        add("bat_ear",
                new AddItemModifier(
                        new LootItemCondition[] {
                                LootTableIdCondition.builder(
                                        ResourceLocation.fromNamespaceAndPath(
                                                "minecraft",
                                                "entities/bat"
                                        )
                                ).build()
                        },
                        ModItems.BAT_EAR.get(),
                        1,
                        2,
                        1f
                ));





        add("mandrake_from_short_grass",
                new AddItemModifier(
                        new LootItemCondition[] {
                                LootTableIdCondition.builder(
                                        ResourceLocation.fromNamespaceAndPath(
                                                "minecraft",
                                                "blocks/short_grass"
                                        )
                                ).build()
                        },
                        ModItems.MANDRAKE_SEED.get(),
                        1,
                        2,
                        0.07f
                ));


    }
}
