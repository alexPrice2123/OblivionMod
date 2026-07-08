package net.kaupenjoe.tutorialmod.energy;

import net.kaupenjoe.tutorialmod.item.ModItems;

public class ModEnergyMappings {
    public static void register() {
        // Add one line per energy type you create, e.g.:
        EnergyEssenceRegistry.register(ModItems.VOIDENERGY, ModItems.VOIDESSENCE);
        // EnergyEssenceRegistry.register(ModItems.FIRE_ENERGY, ModItems.ESSENCE_OF_FIRE);
    }
}