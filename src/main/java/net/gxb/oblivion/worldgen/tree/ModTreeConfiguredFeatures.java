package net.gxb.oblivion.worldgen.tree;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;

public class ModTreeConfiguredFeatures {

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        BlockState log = Blocks.DARK_OAK_LOG.defaultBlockState(); // swap for your real log block
        BlockState leaves = Blocks.DARK_OAK_LEAVES.defaultBlockState(); // swap for your real leaves block

        // SHORT variant — 2-wide trunk, roughly under ~32 blocks tall
        context.register(ModTreeFeatures.SPIRIT_TREE_SHORT_CONFIGURED,
                new ConfiguredFeature<>(
                        Feature.TREE,
                        new TreeConfiguration.TreeConfigurationBuilder(
                                BlockStateProvider.simple(log),
                                new DarkOakTrunkPlacer(8, 6, 4), // base 8 + up to 10 random ≈ 8-18 tall
                                BlockStateProvider.simple(leaves),
                                new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 2), UniformInt.of(14, 20)),
                                new TwoLayersFeatureSize(1, 1, 2)
                        ).build()
                ));

        // TALL variant — scaled-up 2-wide trunk reading as noticeably thicker/taller, 32+ blocks
        context.register(ModTreeFeatures.SPIRIT_TREE_TALL_CONFIGURED,
                new ConfiguredFeature<>(
                        Feature.TREE,
                        new TreeConfiguration.TreeConfigurationBuilder(
                                BlockStateProvider.simple(log),
                                new ThreeByThreeTrunkPlacer(24, 10, 6),
                                BlockStateProvider.simple(leaves),
                                new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 2), UniformInt.of(14, 20)),
                                new TwoLayersFeatureSize(3, 1, 6)
                        ).build()
                ));
    }
}