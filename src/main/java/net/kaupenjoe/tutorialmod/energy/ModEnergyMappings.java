package net.kaupenjoe.tutorialmod.energy;

import net.kaupenjoe.tutorialmod.item.ModItems;

public class ModEnergyMappings {
    public static void init() {
        // Register your fusions
        EnergyFusionRegistry.register(ModItems.VOID_ENERGY, ModItems.VOID_ENERGY, ModItems.VOID_ENERGY);

        // 2. The Essence table checks this map: Void Energy transforms your item into Void Essence!
        EnergyEssenceRegistry.register(ModItems.VOID_ENERGY, ModItems.VOID_ESSENCE, 1, 0.2f, 0.0f, 0.4f);
        // --- Add each new Tier-1 energy the same way ---
        // EnergyEssenceRegistry.register(ModItems.LIGHTNING_ENERGY, ModItems.ESSENCE_OF_LIGHTNING, 1, 0.0f, 0.9f, 0.9f);
        // EnergyEssenceRegistry.register(ModItems.PURE_ENERGY,      ModItems.ESSENCE_OF_PURE,      1, 1.0f, 1.0f, 1.0f);
        // EnergyEssenceRegistry.register(ModItems.CORRUPT_ENERGY,   ModItems.ESSENCE_OF_CORRUPT,   1, 0.8f, 0.0f, 0.0f);

        // --- Tier-1 fusions -> Tier-2 result (from your chart) ---
        // EnergyFusionRegistry.register(ModItems.LIGHTNING_ENERGY, ModItems.PURE_ENERGY,    ModItems.DIVINE_ENERGY);
        // EnergyFusionRegistry.register(ModItems.LIGHTNING_ENERGY, ModItems.CORRUPT_ENERGY, ModItems.WRAITH_ENERGY);
        // EnergyFusionRegistry.register(ModItems.LIGHTNING_ENERGY, ModItems.VOID_ENERGY,    ModItems.ASTRAL_ENERGY);
        // EnergyFusionRegistry.register(ModItems.PURE_ENERGY,      ModItems.CORRUPT_ENERGY, ModItems.DUALITY_ENERGY);
        // EnergyFusionRegistry.register(ModItems.PURE_ENERGY,      ModItems.VOID_ENERGY,    ModItems.ENTROPY_ENERGY);
        // EnergyFusionRegistry.register(ModItems.CORRUPT_ENERGY,   ModItems.VOID_ENERGY,    ModItems.ABYSSAL_ENERGY);

        // --- Tier-2 fusions -> Tier-3, once those Tier-2 energies exist, follow the same pattern:
        // EnergyEssenceRegistry.register(ModItems.DIVINE_ENERGY, ModItems.ESSENCE_OF_DIVINE, 2, r, g, b);
        // EnergyFusionRegistry.register(ModItems.DIVINE_ENERGY, ModItems.WRAITH_ENERGY, ModItems.ANGEL_ENERGY);
        // ...and so on for the rest of your Tier-2 table.
    }
}