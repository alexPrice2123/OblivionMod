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
                        // Add every item you want to appear here
                        output.accept(ModItems.EXTRACTION_CORE.get());
                        output.accept(ModItems.ESSENCE.get());

                        // Add block items too (example)
                        // output.accept(ModBlocks.YOUR_BLOCK.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}