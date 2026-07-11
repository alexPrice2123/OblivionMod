package net.gxb.oblivion.worldgen.tree;

import net.gxb.oblivion.Oblivion;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModTreeFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> SPIRIT_TREE_SHORT_CONFIGURED =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(Oblivion.MOD_ID, "spirit_tree_short"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPIRIT_TREE_TALL_CONFIGURED =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(Oblivion.MOD_ID, "spirit_tree_tall"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPIRIT_SPIKES_CONFIGURED =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(Oblivion.MOD_ID, "spirit_spikes"));

    public static final ResourceKey<PlacedFeature> SPIRIT_TREE_SHORT_PLACED =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(Oblivion.MOD_ID, "spirit_tree_short"));
    public static final ResourceKey<PlacedFeature> SPIRIT_TREE_TALL_PLACED =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(Oblivion.MOD_ID, "spirit_tree_tall"));
    public static final ResourceKey<PlacedFeature> SPIRIT_SPIKES_PLACED =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(Oblivion.MOD_ID, "spirit_spikes"));
}