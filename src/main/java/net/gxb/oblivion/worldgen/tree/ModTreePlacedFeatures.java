package net.gxb.oblivion.worldgen.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.core.Vec3i;

import java.util.List;

public class ModTreePlacedFeatures {

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        BlockPredicateFilter avoidWater = BlockPredicateFilter.forPredicate(
                BlockPredicate.allOf(
                        BlockPredicate.not(BlockPredicate.matchesBlocks(new Vec3i(0, -1, 0), List.of(Blocks.WATER))),
                        BlockPredicate.not(BlockPredicate.matchesBlocks(new Vec3i(1, -1, 0), List.of(Blocks.WATER))),
                        BlockPredicate.not(BlockPredicate.matchesBlocks(new Vec3i(-1, -1, 0), List.of(Blocks.WATER))),
                        BlockPredicate.not(BlockPredicate.matchesBlocks(new Vec3i(0, -1, 1), List.of(Blocks.WATER))),
                        BlockPredicate.not(BlockPredicate.matchesBlocks(new Vec3i(0, -1, -1), List.of(Blocks.WATER)))
                )
        );


        context.register(ModTreeFeatures.SPIRIT_TREE_SHORT_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModTreeFeatures.SPIRIT_TREE_SHORT_CONFIGURED),
                        List.of(
                                PlacementUtils.countExtra(6, 0.5F, 8),
                                InSquarePlacement.spread(),
                                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                                avoidWater, // <--- Added water check here
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
                                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                                avoidWater, // <--- Added water check here
                                PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING),
                                BiomeFilter.biome()
                        )
                ));
    }
}