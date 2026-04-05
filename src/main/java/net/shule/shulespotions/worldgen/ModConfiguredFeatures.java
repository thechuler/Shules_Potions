package net.shule.shulespotions.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.shule.shulespotions.ShulesPotions;


public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> GRONITE_ORE = registerKey("gronite_ore");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
      /*
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherrackReplacables = new BlockMatchTest(Blocks.NETHERRACK);
        RuleTest endReplaceables = new BlockMatchTest(Blocks.END_STONE);

        List<OreConfiguration.TargetBlockState> overworldPiedraCargada = List.of(OreConfiguration.target(stoneReplaceable,
                ModBlocks.GRONITE_ORE.get().defaultBlockState()));


        register(context, GRONITE_ORE, Feature.ORE, new OreConfiguration(overworldPiedraCargada, 3));

   */
    }





    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,ResourceLocation.fromNamespaceAndPath(ShulesPotions.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }



}
