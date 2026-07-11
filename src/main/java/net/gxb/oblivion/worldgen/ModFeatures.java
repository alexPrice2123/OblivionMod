package net.gxb.oblivion.worldgen;

import net.gxb.oblivion.Oblivion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(BuiltInRegistries.FEATURE, Oblivion.MOD_ID);

    public static final DeferredHolder<Feature<?>, ModSpikeFeature> SPIRIT_SPIKE =
            FEATURES.register("spirit_spike", () -> new ModSpikeFeature(NoneFeatureConfiguration.CODEC));
}