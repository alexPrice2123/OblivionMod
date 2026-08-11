package net.gxb.oblivion.worldgen;

import com.mojang.datafixers.util.Pair;
import net.gxb.oblivion.worldgen.biome.ModBiomes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class ModRegion extends Region {

    public ModRegion(ResourceLocation name, RegionType regionType, int weight) {
        super(name, regionType, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {

        // Ancient Forest
        biome(mapper, ModBiomes.ANCIENT_FOREST,
                Climate.Parameter.span(0.35F, 0.4F),
                Climate.Parameter.span(0.6F, 0.7F),
                Climate.Parameter.span(0.3F, 0.8F),
                Climate.Parameter.span(-1.0F, -0.9F),
                Climate.Parameter.point(0.0F),           // Normal inland surface depth
                Climate.Parameter.span(0.4F, 1.0F));

        // Spirit Peaks — Maximized for sudden vertical elevation spikes
        biome(mapper, ModBiomes.SPIRIT_PEAKS,
                Climate.Parameter.span(-0.5F, -0.3F),   // Cool alpine temps
                Climate.Parameter.span(0.4F, 0.8F),     // Humidity
                Climate.Parameter.point(1.0F),          // Force absolute max Continentalness (Inland landmass)
                Climate.Parameter.point(-1.0F),         // Force absolute minimum Erosion (Zero erosion = highest possible spires)
                Climate.Parameter.point(0.0F),          // Baseline depth anchor
                Climate.Parameter.point(1.0F));         // Force max Weirdness (Triggers absolute highest noise peaks)
    }

    private void biome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper,
                       ResourceKey<Biome> biome,
                       Climate.Parameter temperature,
                       Climate.Parameter humidity,
                       Climate.Parameter continentalness,
                       Climate.Parameter erosion,
                       Climate.Parameter depth,
                       Climate.Parameter weirdness) {
        mapper.accept(Pair.of(
                Climate.parameters(temperature, humidity, continentalness, erosion, depth, weirdness, 0.0F),
                biome
        ));
    }
}