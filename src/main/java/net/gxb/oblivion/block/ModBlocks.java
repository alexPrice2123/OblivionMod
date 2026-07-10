package net.gxb.oblivion.block;

import net.gxb.oblivion.Oblivion;
import net.gxb.oblivion.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.gxb.oblivion.block.custom.EssenceTableBlock;
import net.gxb.oblivion.block.custom.EnergyTableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.LeavesBlock;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Oblivion.MOD_ID);

    public static final DeferredBlock<Block> ESSENCE_TABLE = registerBlock("essence_table",
            () -> new EssenceTableBlock(BlockBehaviour.Properties.of()
                    .strength(3.5f)
                    .noOcclusion()));

    public static final DeferredBlock<Block> ENERGY_TABLE = registerBlock("energy_table",
            () -> new EnergyTableBlock(BlockBehaviour.Properties.of()
                    .strength(3.5f)
                    .noOcclusion()));
    public static final DeferredBlock<Block> SPIRIT_LOG = registerBlock("spirit_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_LOG)));
    public static final DeferredBlock<Block> SPIRIT_LEAVES = registerBlock("spirit_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_LEAVES)));
    public static final DeferredBlock<Block> STRIPPED_SPIRIT_LOG = registerBlock("stripped_spirit_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_DARK_OAK_LOG)));
    public static final DeferredBlock<Block> SPIRIT_PLANKS = registerBlock("spirit_planks",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS)));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
