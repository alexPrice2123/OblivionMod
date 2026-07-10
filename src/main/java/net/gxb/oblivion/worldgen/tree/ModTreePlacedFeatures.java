package net.gxb.oblivion.worldgen.tree;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class ModTreePlacedFeatures {

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(ModTreeFeatures.SPIRIT_TREE_SHORT_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModTreeFeatures.SPIRIT_TREE_SHORT_CONFIGURED),
                        List.of(
                                PlacementUtils.countExtra(6, 0.5F, 8),
                                InSquarePlacement.spread(),
                                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, // Changed here
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
                                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, // Changed here
                                PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING),
                                BiomeFilter.biome()
                        )
                ));
    }
}