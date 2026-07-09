package net.gxb.oblivion.worldgen.biome;

import net.gxb.oblivion.Oblivion;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class ModBiomes {
    public static final ResourceKey<Biome> ANCIENT_FOREST = registerKey("ancient_forest");

    private static ResourceKey<Biome> registerKey(String name) {
        return ResourceKey.create(Registries.BIOME,
                ResourceLocation.fromNamespaceAndPath(Oblivion.MOD_ID, name));
    }
}