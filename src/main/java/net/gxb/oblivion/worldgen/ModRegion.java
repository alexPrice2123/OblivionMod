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

        // Import ALL of vanilla's real, correctly-sized biome map with zero replacements.
        // This gives Ancient Forest genuine neighbors, so it can no longer balloon into
        // unclaimed space via fallback — it only wins where it's the closest match.
        this.addModifiedVanillaOverworldBiomes(mapper, builder -> {
            // intentionally empty — no replaceBiome calls, just importing vanilla as-is
        });

        // Ancient Forest — widened for a bigger footprint
        mapper.accept(Pair.of(
                Climate.parameters(
                        Climate.Parameter.span(0.15F, 0.9F),   // temperature — slightly wider
                        Climate.Parameter.span(0.3F, 1.0F),    // humidity — slightly wider
                        Climate.Parameter.span(0.0F, 0.9F),    // continentalness — WIDER, still land-only
                        Climate.Parameter.span(0.0F, 1.0F),   // erosion — WIDER
                        Climate.Parameter.point(0.0F),
                        Climate.Parameter.span(-0.3F, 0.9F),   // weirdness — wider, less rare
                        0.0F
                ),
                ModBiomes.ANCIENT_FOREST
        ));

// Spirit Peaks — kept narrow/rare, unaffected by Ancient Forest's growth
        mapper.accept(Pair.of(
                Climate.parameters(
                        Climate.Parameter.span(-0.1F, 0.15F),   // temperature — slightly wider
                        Climate.Parameter.span(0.3F, 1.0F),    // humidity — slightly wider
                        Climate.Parameter.span(0.8F, 0.9F),    // continentalness — WIDER, still land-only
                        Climate.Parameter.span(-1.0F, 1.0F),   // erosion — WIDER
                        Climate.Parameter.point(0.0F),
                        Climate.Parameter.span(-0.2F, 0.2F),   // weirdness — wider, less rare
                        0.0F
                ),
                ModBiomes.SPIRIT_PEAKS
        ));
    }
}