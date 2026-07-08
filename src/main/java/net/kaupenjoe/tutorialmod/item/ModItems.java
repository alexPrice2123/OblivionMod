package net.kaupenjoe.tutorialmod.item;

import net.kaupenjoe.tutorialmod.TutorialMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TutorialMod.MOD_ID);

    public static final DeferredItem<Item> EXTRACTION_CORE = ITEMS.register("extraction_core",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ESSENCE = ITEMS.register("essence",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ESSENCE_CORE = ITEMS.register("essence_core",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> VOID_ESSENCE = ITEMS.register("void_essence",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> VOID_ENERGY = ITEMS.register("void_energy",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> LIGHTNING_ESSENCE = ITEMS.register("lightning_essence",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LIGHTNING_ENERGY = ITEMS.register("lightning_energy",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ASTRAL_ESSENCE = ITEMS.register("astral_essence",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ASTRAL_ENERGY = ITEMS.register("astral_energy",
            () -> new Item(new Item.Properties()));




    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
