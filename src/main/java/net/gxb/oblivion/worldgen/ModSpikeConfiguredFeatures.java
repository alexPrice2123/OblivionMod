package net.gxb.oblivion.worldgen;

import net.gxb.oblivion.Oblivion;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.List;

public class ModSpikeConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPIRIT_SPIKE_CONFIGURED =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(Oblivion.MOD_ID, "spirit_spike"));

    public static final ResourceKey<PlacedFeature> SPIRIT_SPIKE_PLACED =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(Oblivion.MOD_ID, "spirit_spike"));

    public static void bootstrapConfigured(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(SPIRIT_SPIKE_CONFIGURED,
                new ConfiguredFeature<>(ModFeatures.SPIRIT_SPIKE.get(), NoneFeatureConfiguration.NONE));
    }

    public static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        BlockPredicateFilter groundCheck = BlockPredicateFilter.forPredicate(
                BlockPredicate.matchesBlocks(
                        new Vec3i(0, -1, 0),
                        List.of(Blocks.SNOW_BLOCK, Blocks.SNOW, Blocks.POWDER_SNOW, Blocks.STONE)
                )
        );

        context.register(SPIRIT_SPIKE_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(SPIRIT_SPIKE_CONFIGURED),
                        List.of(
                                RarityFilter.onAverageOnceEvery(6),
                                InSquarePlacement.spread(),
                                PlacementUtils.HEIGHTMAP,
                                groundCheck,
                                BiomeFilter.biome()
                        )
                ));
    }
}