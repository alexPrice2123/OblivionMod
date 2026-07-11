package net.gxb.oblivion.datagen;

import net.gxb.oblivion.Oblivion;
import net.gxb.oblivion.worldgen.biome.ModBiomesGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.gxb.oblivion.worldgen.tree.ModTreeConfiguredFeatures;
import net.gxb.oblivion.worldgen.tree.ModTreePlacedFeatures;
import net.gxb.oblivion.worldgen.ModSpikeConfiguredFeatures;

import java.util.Set;

@EventBusSubscriber(modid = Oblivion.MOD_ID)
public class OblivionDataGenerators {

    public static final RegistrySetBuilder WORLDGEN_BUILDER = new RegistrySetBuilder()
            .add(Registries.BIOME, ModBiomesGenerator::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, ModTreeConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModTreePlacedFeatures::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, ModSpikeConfiguredFeatures::bootstrapConfigured)
            .add(Registries.PLACED_FEATURE, ModSpikeConfiguredFeatures::bootstrapPlaced);

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeServer(),
                new DatapackBuiltinEntriesProvider(
                        generator.getPackOutput(),
                        event.getLookupProvider(),
                        WORLDGEN_BUILDER,
                        Set.of(Oblivion.MOD_ID)));
    }
}