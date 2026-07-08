package net.kaupenjoe.tutorialmod.energy;

import net.minecraft.world.item.Item;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EnergyEssenceRegistry {
    private static final Map<Supplier<? extends Item>, Supplier<? extends Item>> MAPPINGS = new LinkedHashMap<>();

    /** Call this once per energy type you create, e.g. in ModEnergyMappings. */
    public static void register(Supplier<? extends Item> energyItem, Supplier<? extends Item> essenceItem) {
        MAPPINGS.put(energyItem, essenceItem);
    }

    public static boolean isValidEnergyItem(Item item) {
        return MAPPINGS.keySet().stream().anyMatch(supplier -> supplier.get() == item);
    }

    public static Item getEssenceFor(Item energyItem) {
        for (var entry : MAPPINGS.entrySet()) {
            if (entry.getKey().get() == energyItem) {
                return entry.getValue().get();
            }
        }
        return null;
    }
}