package net.gxb.oblivion.item;

import net.gxb.oblivion.Oblivion;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Oblivion.MOD_ID);

    public static final DeferredItem<Item> EXTRACTION_CORE = ITEMS.register("extraction_core",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ESSENCE = ITEMS.register("essence",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ESSENCE_CORE = ITEMS.register("essence_core",
            () -> new Item(new Item.Properties()));
    //TIER 1 ESSENCE/ENERGY
    public static final DeferredItem<Item> VOID_ESSENCE = ITEMS.register("void_essence",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> VOID_ENERGY = ITEMS.register("void_energy",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PURE_ESSENCE = ITEMS.register("pure_essence",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PURE_ENERGY = ITEMS.register("pure_energy",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CORRUPT_ESSENCE = ITEMS.register("corrupt_essence",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CORRUPT_ENERGY = ITEMS.register("corrupt_energy",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LIGHTNING_ESSENCE = ITEMS.register("lightning_essence",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LIGHTNING_ENERGY = ITEMS.register("lightning_energy",
            () -> new Item(new Item.Properties()));

    //TIER 2 ESSENCE/ENERGY
    public static final DeferredItem<Item> ASTRAL_ESSENCE = ITEMS.register("astral_essence",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ASTRAL_ENERGY = ITEMS.register("astral_energy",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DIVINE_ESSENCE = ITEMS.register("divine_essence",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DIVINE_ENERGY = ITEMS.register("divine_energy",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WRAITH_ESSENCE = ITEMS.register("wraith_essence",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WRAITH_ENERGY = ITEMS.register("wraith_energy",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DUALITY_ESSENCE = ITEMS.register("duality_essence",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DUALITY_ENERGY = ITEMS.register("duality_energy",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ENTROPY_ESSENCE = ITEMS.register("entropy_essence",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ENTROPY_ENERGY = ITEMS.register("entropy_energy",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ABYSSAL_ESSENCE = ITEMS.register("abyssal_essence",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ABYSSAL_ENERGY = ITEMS.register("abyssal_energy",
            () -> new Item(new Item.Properties()));




    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
