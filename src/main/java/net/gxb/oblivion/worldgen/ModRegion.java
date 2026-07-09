package net.gxb.oblivion.worldgen;

import net.gxb.oblivion.worldgen.biome.ModBiomes;
import net.minecraft.resources.ResourceLocation;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.ParameterUtils.*;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.util.function.Consumer;

public class ModRegion extends Region {

    public ModRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // Example: place ancient_forest in a temperate, humid climate band
        this.addModifiedVanillaOverworldBiomes(mapper, builder -> {
            builder.replaceBiome(Biomes.FOREST, ModBiomes.ANCIENT_FOREST);
        });
    }
}