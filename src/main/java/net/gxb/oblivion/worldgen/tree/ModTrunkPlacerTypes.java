package net.gxb.oblivion.worldgen.tree;

import com.mojang.serialization.MapCodec;
import net.gxb.oblivion.Oblivion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTrunkPlacerTypes {
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER_TYPES =
            DeferredRegister.create(BuiltInRegistries.TRUNK_PLACER_TYPE.key(), Oblivion.MOD_ID);

    public static final DeferredHolder<TrunkPlacerType<?>, TrunkPlacerType<ThreeByThreeTrunkPlacer>> THREE_BY_THREE =
            TRUNK_PLACER_TYPES.register("three_by_three",
                    () -> new TrunkPlacerType<>(ThreeByThreeTrunkPlacer.CODEC));
}