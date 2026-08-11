package net.gxb.oblivion.worldgen;

import net.gxb.oblivion.Oblivion;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class ModRidgeConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> RIDGE_FILLER_CONFIGURED =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(Oblivion.MOD_ID, "ridge_filler"));

    public static final ResourceKey<PlacedFeature> RIDGE_FILLER_PLACED =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(Oblivion.MOD_ID, "ridge_filler"));

    public static void bootstrapConfigured(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(RIDGE_FILLER_CONFIGURED,
                new ConfiguredFeature<>(ModFeatures.RIDGE_FILLER.get(), NoneFeatureConfiguration.NONE));
    }

    public static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        BlockPredicateFilter groundCheck = BlockPredicateFilter.forPredicate(
                BlockPredicate.matchesBlocks(
                        new Vec3i(0, -1, 0),
                        List.of(Blocks.SNOW_BLOCK, Blocks.SNOW, Blocks.POWDER_SNOW, Blocks.STONE, Blocks.TUFF, Blocks.DEEPSLATE)
                )
        );

        context.register(RIDGE_FILLER_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(RIDGE_FILLER_CONFIGURED),
                        List.of(
                                // No rarity filter — this runs on effectively every chunk,
                                // with several bumps scattered per chunk to rough up the ground.
                                CountPlacement.of(UniformInt.of(4, 8)),
                                InSquarePlacement.spread(),
                                PlacementUtils.HEIGHTMAP,
                                groundCheck,
                                BiomeFilter.biome()
                        )
                ));
    }
}
