package net.gxb.oblivion.item;

import net.gxb.oblivion.Oblivion;
import net.gxb.oblivion.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Oblivion.MOD_ID);

    public static final Supplier<CreativeModeTab> OBLIVION_TAB = CREATIVE_MODE_TAB.register("oblivion_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.ESSENCE.get()))
                    .title(Component.translatable("creativetab.oblivionmodgxb.oblivion_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.EXTRACTION_CORE.get());
                        output.accept(ModItems.ESSENCE.get());
                        output.accept(ModItems.ESSENCE_CORE.get());

                        output.accept(ModItems.VOID_ENERGY.get());
                        output.accept(ModItems.VOID_ESSENCE.get());
                        output.accept(ModItems.LIGHTNING_ENERGY.get());
                        output.accept(ModItems.LIGHTNING_ESSENCE.get());
                        output.accept(ModItems.PURE_ENERGY.get());
                        output.accept(ModItems.PURE_ESSENCE.get());
                        output.accept(ModItems.CORRUPT_ENERGY.get());
                        output.accept(ModItems.CORRUPT_ESSENCE.get());

                        output.accept(ModItems.ASTRAL_ENERGY.get());
                        output.accept(ModItems.ASTRAL_ESSENCE.get());
                        output.accept(ModItems.DIVINE_ENERGY.get());
                        output.accept(ModItems.DIVINE_ESSENCE.get());
                        output.accept(ModItems.WRAITH_ENERGY.get());
                        output.accept(ModItems.WRAITH_ESSENCE.get());
                        output.accept(ModItems.ENTROPY_ENERGY.get());
                        output.accept(ModItems.ENTROPY_ESSENCE.get());
                        output.accept(ModItems.DUALITY_ENERGY.get());
                        output.accept(ModItems.DUALITY_ESSENCE.get());
                        output.accept(ModItems.ABYSSAL_ENERGY.get());
                        output.accept(ModItems.ABYSSAL_ESSENCE.get());

                        output.accept(ModBlocks.ENERGY_TABLE.get().asItem());
                        output.accept(ModBlocks.ESSENCE_TABLE.get().asItem());
                        output.accept(ModBlocks.SPIRIT_LOG.get().asItem());
                        output.accept(ModBlocks.SPIRIT_LEAVES.get().asItem());
                        output.accept(ModBlocks.STRIPPED_SPIRIT_LOG.get().asItem());
                        output.accept(ModBlocks.SPIRIT_PLANKS.get().asItem());

                        // New stone-family blocks
                        output.accept(ModBlocks.SPIRIT_STONE.get().asItem());
                        output.accept(ModBlocks.CURSED_STONE.get().asItem());
                        output.accept(ModBlocks.COBBLED_SPIRIT_STONE.get().asItem());
                        output.accept(ModBlocks.POLISHED_SPIRIT_STONE.get().asItem());
                        output.accept(ModBlocks.POLISHED_CURSED_STONE.get().asItem());
                        output.accept(ModBlocks.POLISHED_SPIRIT_BRICKS.get().asItem());
                        output.accept(ModBlocks.POLISHED_CURSED_BRICKS.get().asItem());
                        output.accept(ModBlocks.CRACKED_POLISHED_SPIRIT_BRICKS.get().asItem());
                        output.accept(ModBlocks.CRACKED_POLISHED_CURSED_BRICKS.get().asItem());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}