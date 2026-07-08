package net.kaupenjoe.tutorialmod.block.entity;

import net.kaupenjoe.tutorialmod.TutorialMod;
import net.kaupenjoe.tutorialmod.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TutorialMod.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EssenceTableBlockEntity>> ESSENCE_TABLE_BE =
            BLOCK_ENTITIES.register("essence_table_be", () -> BlockEntityType.Builder.of(
                    EssenceTableBlockEntity::new, ModBlocks.ESSENCE_TABLE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyTableBlockEntity>> ENERGY_TABLE_BE =
            BLOCK_ENTITIES.register("energy_table_be", () -> BlockEntityType.Builder.of(
                    EnergyTableBlockEntity::new, ModBlocks.ENERGY_TABLE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}