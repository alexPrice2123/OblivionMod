package net.gxb.oblivion.worldgen.tree;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ThreeByThreeTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<ThreeByThreeTrunkPlacer> CODEC =
            RecordCodecBuilder.mapCodec(instance -> trunkPlacerParts(instance).apply(instance, ThreeByThreeTrunkPlacer::new));

    public ThreeByThreeTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.THREE_BY_THREE.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                                            RandomSource random, int freeTreeHeight, BlockPos pos,
                                                            TreeConfiguration config) {
        List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<>();

        for (int y = 0; y < freeTreeHeight; y++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos logPos = pos.offset(dx, y, dz);
                    this.placeLog(level, blockSetter, random, logPos, config);
                }
            }
        }

        attachments.add(new FoliagePlacer.FoliageAttachment(pos.above(freeTreeHeight), 1, false));
        return attachments;
    }
}