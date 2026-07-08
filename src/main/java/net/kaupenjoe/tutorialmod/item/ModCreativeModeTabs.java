package net.kaupenjoe.tutorialmod.item;

import net.kaupenjoe.tutorialmod.TutorialMod;
import net.kaupenjoe.tutorialmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TutorialMod.MOD_ID);

    public static final Supplier<CreativeModeTab> OBLIVION_TAB = CREATIVE_MODE_TAB.register("oblivion_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.ESSENCE.get()))
                    .title(Component.translatable("creativetab.tutorialmod.oblivion_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.EXTRACTION_CORE.get());
                        output.accept(ModItems.ESSENCE.get());
                        output.accept(ModItems.ESSENCE_CORE.get());

                        // Add every Energy / Essence item pair here as you create them, e.g.:
                        output.accept(ModItems.VOID_ENERGY.get());
                        output.accept(ModItems.VOID_ESSENCE.get());
                        output.accept(ModItems.LIGHTNING_ENERGY.get());
                        output.accept(ModItems.LIGHTNING_ESSENCE.get());
                        output.accept(ModItems.ASTRAL_ENERGY.get());
                        output.accept(ModItems.ASTRAL_ESSENCE.get());

                        output.accept(ModBlocks.ENERGY_TABLE.get().asItem());
                        output.accept(ModBlocks.ESSENCE_TABLE.get().asItem());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}