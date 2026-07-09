package net.gxb.oblivion.energy;

import net.gxb.oblivion.item.ModItems;

public class ModEnergyMappings {
    public static void init() {
        // Fusions
        EnergyFusionRegistry.register(ModItems.LIGHTNING_ENERGY, ModItems.VOID_ENERGY, ModItems.ASTRAL_ENERGY);
        EnergyFusionRegistry.register(ModItems.LIGHTNING_ENERGY, ModItems.PURE_ENERGY, ModItems.DIVINE_ENERGY);
        EnergyFusionRegistry.register(ModItems.LIGHTNING_ENERGY, ModItems.CORRUPT_ENERGY, ModItems.WRAITH_ENERGY);
        EnergyFusionRegistry.register(ModItems.PURE_ENERGY, ModItems.CORRUPT_ENERGY, ModItems.DUALITY_ENERGY);
        EnergyFusionRegistry.register(ModItems.PURE_ENERGY, ModItems.VOID_ENERGY, ModItems.ENTROPY_ENERGY);
        EnergyFusionRegistry.register(ModItems.CORRUPT_ENERGY, ModItems.VOID_ENERGY, ModItems.ABYSSAL_ENERGY);

        // Tier 1
        EnergyEssenceRegistry.register(ModItems.VOID_ENERGY, ModItems.VOID_ESSENCE, 1, 0.2f, 0.0f, 0.4f);
        EnergyEssenceRegistry.register(ModItems.LIGHTNING_ENERGY, ModItems.LIGHTNING_ESSENCE, 1, 0.49f, 0.98f, 1.0f);
        EnergyEssenceRegistry.register(ModItems.PURE_ENERGY, ModItems.PURE_ESSENCE, 1, 0.95f, 0.95f, 0.9f);
        EnergyEssenceRegistry.register(ModItems.CORRUPT_ENERGY, ModItems.CORRUPT_ESSENCE, 1, 0.8f, 0.0f, 0.0f);
        //Tier 2
        EnergyEssenceRegistry.register(ModItems.ASTRAL_ENERGY, ModItems.ASTRAL_ESSENCE, 2, 0.8f, 0.7f, 0.9f);
        EnergyEssenceRegistry.register(ModItems.DIVINE_ENERGY, ModItems.DIVINE_ESSENCE, 2, 0.9f, 0.9f, 0.3f);
        EnergyEssenceRegistry.register(ModItems.WRAITH_ENERGY, ModItems.WRAITH_ESSENCE, 2, 0.7f, 0.7f, 0.7f);
        EnergyEssenceRegistry.register(ModItems.ENTROPY_ENERGY, ModItems.ENTROPY_ESSENCE, 2, 0.5f, 0.9f, 0.5f);
        EnergyEssenceRegistry.register(ModItems.DUALITY_ENERGY, ModItems.DUALITY_ESSENCE, 2, 0.9f, 0.4f, 0.4f);
        EnergyEssenceRegistry.register(ModItems.ABYSSAL_ENERGY, ModItems.ABYSSAL_ESSENCE, 2, 0.1f, 0.0f, 0.2f);

    }
}