package net.gxb.oblivion.worldgen.tree;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.ArrayList;
import java.util.List;

public class ModTreePlacedFeatures {

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        // 1. River Avoidance Filter (6-block scanning grid)
        List<BlockPredicate> waterChecks = new ArrayList<>();
        int radius = 6;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Vec3i offset = new Vec3i(x, -1, z);
                waterChecks.add(BlockPredicate.not(BlockPredicate.matchesBlocks(offset, Blocks.WATER)));
            }
        }
        BlockPredicateFilter avoidWater = BlockPredicateFilter.forPredicate(BlockPredicate.allOf(waterChecks));

        // 2. High Altitude Filter: Restricts generation to Y-levels between 90 and 256
        HeightRangePlacement peakAltitudeFilter = HeightRangePlacement.uniform(
                VerticalAnchor.absolute(90),
                VerticalAnchor.absolute(256)
        );

        context.register(ModTreeFeatures.SPIRIT_TREE_SHORT_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModTreeFeatures.SPIRIT_TREE_SHORT_CONFIGURED),
                        List.of(
                                PlacementUtils.countExtra(6, 0.5F, 8),
                                InSquarePlacement.spread(),
                                peakAltitudeFilter,                  // <--- Restricts checks to upper mountains
                                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                                avoidWater,
                                PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING),
                                BiomeFilter.biome()
                        )
                ));

        context.register(ModTreeFeatures.SPIRIT_TREE_TALL_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModTreeFeatures.SPIRIT_TREE_TALL_CONFIGURED),
                        List.of(
                                PlacementUtils.countExtra(3, 0.25F, 4),
                                InSquarePlacement.spread(),
                                peakAltitudeFilter,                  // <--- Restricts checks to upper mountains
                                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                                avoidWater,
                                PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING),
                                BiomeFilter.biome()
                        )
                ));
    }
}