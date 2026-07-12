package net.gxb.oblivion.worldgen.tree;

import net.gxb.oblivion.block.ModBlocks;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer; // Required import for 1x1 trunks

public class ModTreeConfiguredFeatures {

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        BlockState log = ModBlocks.SPIRIT_LOG.get().defaultBlockState();
        BlockState leaves = ModBlocks.SPIRIT_LEAVES.get().defaultBlockState();

        // SHORT variant — Solid 2x2 trunk
        context.register(ModTreeFeatures.SPIRIT_TREE_SHORT_CONFIGURED,
                new ConfiguredFeature<>(
                        Feature.TREE,
                        new TreeConfiguration.TreeConfigurationBuilder(
                                BlockStateProvider.simple(log),
                                new DarkOakTrunkPlacer(8, 6, 4),
                                BlockStateProvider.simple(leaves),
                                new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 2), UniformInt.of(14, 20)),
                                new TwoLayersFeatureSize(1, 1, 2)
                        ).build()
                ));

        // TALL variant — Scaled-up solid 2x2 trunk
        context.register(ModTreeFeatures.SPIRIT_TREE_TALL_CONFIGURED,
                new ConfiguredFeature<>(
                        Feature.TREE,
                        new TreeConfiguration.TreeConfigurationBuilder(
                                BlockStateProvider.simple(log),
                                new TaperingTrunkPlacer(24, 10, 6, 3),
                                BlockStateProvider.simple(leaves),
                                new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 2), UniformInt.of(14, 20)),
                                new TwoLayersFeatureSize(3, 1, 6)
                        ).build()
                ));
    }
}