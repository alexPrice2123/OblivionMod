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

    public ModRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {

        // Ancient Forest — main biome. Widened continentalness/erosion span = bigger contiguous patches.
        // Narrow-ish weirdness/temperature window vs. a full vanilla-replace = rarer than plain Forest.
        mapper.accept(Pair.of(
                Climate.parameters(
                        Climate.Parameter.span(0.25F, 0.5F),    // temperature
                        Climate.Parameter.span(0.4F, 0.9F),     // humidity
                        Climate.Parameter.span(-0.05F, 0.12F),  // continentalness — narrower span = smaller patches
                        Climate.Parameter.span(-0.15F, 0.15F),  // erosion — narrower span = smaller patches
                        Climate.Parameter.point(0.0F),     // depth
                        Climate.Parameter.span(-0.05F, 0.05F),  // weirdness
                        0.0F
                ),
                ModBiomes.ANCIENT_FOREST
        ));

        // Spirit Peaks — sub-biome. Same broad temperature/humidity zone as Ancient Forest,
        // but only at high "weirdness" (mountainous peaks/valleys terrain) and low erosion (jagged terrain).
        mapper.accept(Pair.of(
                Climate.parameters(
                        Climate.Parameter.span(0.25F, 0.5F),     // same temp zone as forest
                        Climate.Parameter.span(0.2F, 0.6F),        //humidity
                        Climate.Parameter.span(0.4F, 1.0F),     // continentalness — narrower span = smaller patches
                        Climate.Parameter.span(-1.0F, -0.7F),    // erosion — narrower span = smaller patches
                        Climate.Parameter.point(0.0F),          // depth
                        Climate.Parameter.span(0.7F, 1.0F),     // weirdness
                        0.0F
                ),
                ModBiomes.SPIRIT_PEAKS
        ));
    }
}