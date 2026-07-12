package net.gxb.oblivion.worldgen.tree;

import com.mojang.serialization.Codec;
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

public class TaperingTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<TaperingTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            trunkPlacerParts(instance)
                    .and(Codec.INT.fieldOf("start_size").forGetter(placer -> placer.startSize))
                    .apply(instance, TaperingTrunkPlacer::new)
    );

    private final int startSize;

    public TaperingTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, int startSize) {
        super(baseHeight, heightRandA, heightRandB);
        this.startSize = startSize;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.TAPERING_TRUNK.isBound()
                ? ModTrunkPlacerTypes.TAPERING_TRUNK.get()
                : TrunkPlacerType.DARK_OAK_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                                            RandomSource random, int freeTreeHeight, BlockPos pos,
                                                            TreeConfiguration config) {
        setDirtAt(level, blockSetter, random, pos, config);

        List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<>();

        // Constant-width column for the full height, no tapering
        for (int y = 0; y < freeTreeHeight; y++) {
            if (startSize >= 3) {
                // solid 3x3
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        this.placeLog(level, blockSetter, random, pos.offset(dx, y, dz), config);
                    }
                }
            } else {
                // solid NxN (e.g. 2x2 for startSize=2)
                for (int dx = 0; dx < startSize; dx++) {
                    for (int dz = 0; dz < startSize; dz++) {
                        this.placeLog(level, blockSetter, random, pos.offset(dx, y, dz), config);
                    }
                }
            }
        }

        attachments.add(new FoliagePlacer.FoliageAttachment(pos.above(freeTreeHeight), 1, false));
        return attachments;
    }
}