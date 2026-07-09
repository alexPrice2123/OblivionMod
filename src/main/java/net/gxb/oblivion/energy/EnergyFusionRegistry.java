package net.gxb.oblivion.energy;

import net.minecraft.world.item.Item;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EnergyFusionRegistry {

    // The key now safely holds the Suppliers to prevent startup crashes
    private record FusionKey(Supplier<? extends Item> a, Supplier<? extends Item> b) {}

    private static final Map<FusionKey, Supplier<? extends Item>> FUSIONS = new HashMap<>();

    /** Order doesn't matter — Lightning+Pure is the same fusion as Pure+Lightning. */
    public static void register(Supplier<? extends Item> energyA, Supplier<? extends Item> energyB,
                                Supplier<? extends Item> result) {
        FUSIONS.put(new FusionKey(energyA, energyB), result);
    }

    /** * Scans the map at runtime, unpacking the suppliers safely to compare
     * them against the raw items sitting on the tables.
     */
    public static Item getFusionResult(Item energyA, Item energyB) {
        for (Map.Entry<FusionKey, Supplier<? extends Item>> entry : FUSIONS.entrySet()) {
            // Safely unpack the items from their suppliers now that the game is running
            Item keyA = entry.getKey().a().get();
            Item keyB = entry.getKey().b().get();

            // Check both combinations (A+B or B+A)
            if ((keyA == energyA && keyB == energyB) || (keyA == energyB && keyB == energyA)) {
                return entry.getValue().get();
            }
        }
        return null; // No matching oblivionmodgxb found
    }
}