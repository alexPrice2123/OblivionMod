package net.kaupenjoe.tutorialmod.energy;

import net.minecraft.world.item.Item;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EnergyEssenceRegistry {
    private static final Map<Supplier<? extends Item>, Supplier<? extends Item>> ESSENCE_MAP = new LinkedHashMap<>();
    private static final Map<Supplier<? extends Item>, Integer> TIER_MAP = new LinkedHashMap<>();
    private static final Map<Supplier<? extends Item>, float[]> COLOR_MAP = new LinkedHashMap<>();

    /** Simple registration: tier defaults to 1, color defaults to white. */
    public static void register(Supplier<? extends Item> energyItem, Supplier<? extends Item> essenceItem) {
        register(energyItem, essenceItem, 1, 1.0f, 1.0f, 1.0f);
    }

    public static void register(Supplier<? extends Item> energyItem, Supplier<? extends Item> essenceItem,
                                int tier, float r, float g, float b) {
        ESSENCE_MAP.put(energyItem, essenceItem);
        TIER_MAP.put(energyItem, tier);
        COLOR_MAP.put(energyItem, new float[]{r, g, b});
    }

    public static boolean isValidEnergyItem(Item item) {
        return ESSENCE_MAP.keySet().stream().anyMatch(supplier -> supplier.get() == item);
    }

    public static Item getEssenceFor(Item energyItem) {
        if (energyItem == null) return null;
        for (var entry : ESSENCE_MAP.entrySet()) {
            // Evaluate the supplier key to get the raw item instance
            if (entry.getKey().get() == energyItem) {
                return entry.getValue().get();
            }
        }
        return null;
    }

    public static int getTier(Item energyItem) {
        for (var entry : TIER_MAP.entrySet()) {
            if (entry.getKey().get() == energyItem) {
                return entry.getValue();
            }
        }
        return 1;
    }

    public static float[] getColor(Item energyItem) {
        for (var entry : COLOR_MAP.entrySet()) {
            if (entry.getKey().get() == energyItem) {
                return entry.getValue();
            }
        }
        return new float[]{1.0f, 1.0f, 1.0f};
    }
}