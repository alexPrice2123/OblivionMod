package net.gxb.oblivion.worldgen.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.gxb.oblivion.worldgen.tree.ModTreeFeatures;
import net.gxb.oblivion.worldgen.ModSpikeConfiguredFeatures;


public class ModBiomesGenerator {

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(ModBiomes.ANCIENT_FOREST, ancientForest(placedFeatures, carvers));
        context.register(ModBiomes.SPIRIT_PEAKS, spiritPeaks(placedFeatures, carvers));

    }

    private static Biome ancientForest(HolderGetter<PlacedFeature> placedFeatures,
                                       HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);

        BiomeGenerationSettings.Builder genBuilder =
                new BiomeGenerationSettings.Builder(placedFeatures, carvers);

        BiomeDefaultFeatures.addDefaultCarversAndLakes(genBuilder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(genBuilder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(genBuilder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(genBuilder);
        BiomeDefaultFeatures.addDefaultOres(genBuilder);
        BiomeDefaultFeatures.addDefaultSoftDisks(genBuilder);
        genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModTreeFeatures.SPIRIT_TREE_SHORT_PLACED);
        genBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModTreeFeatures.SPIRIT_TREE_TALL_PLACED);
        BiomeDefaultFeatures.addJungleGrass(genBuilder);
        BiomeDefaultFeatures.addDefaultMushrooms(genBuilder);


        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.7F)
                .downfall(0.5F)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x225ad3)
                        .waterFogColor(0x2267b4)
                        .fogColor(0xC0D8FF)
                        .skyColor(0x8DB1FF)
                        .grassColorOverride(0x466e2c)
                        .foliageColorOverride(0x466e2c)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(genBuilder.build())
                .build();
    }

    private static Biome spiritPeaks(HolderGetter<PlacedFeature> placedFeatures,
                                     HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);

        BiomeGenerationSettings.Builder genBuilder =
                new BiomeGenerationSettings.Builder(placedFeatures, carvers);

        BiomeDefaultFeatures.addDefaultCarversAndLakes(genBuilder);
        BiomeDefaultFeatures.addMountainTrees(genBuilder); // sparse spruce, mountain-appropriate
        BiomeDefaultFeatures.addDefaultOres(genBuilder);
        BiomeDefaultFeatures.addFossilDecoration(genBuilder); // mountains-flavored ore, thematically fits "spirit"/rare
        genBuilder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ModSpikeConfiguredFeatures.SPIRIT_SPIKE_PLACED);
        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.2F)   // colder, mountain-appropriate
                .downfall(0.3F)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x225ad3)
                        .waterFogColor(0x2267b4)
                        .fogColor(0xC0D8FF)
                        .skyColor(0x8DB1FF)
                        .grassColorOverride(0x466e2c)
                        .foliageColorOverride(0x466e2c)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(genBuilder.build())
                .build();
    }
}