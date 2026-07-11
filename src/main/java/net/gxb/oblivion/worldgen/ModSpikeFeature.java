package net.gxb.oblivion.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.gxb.oblivion.block.ModBlocks;

public class ModSpikeFeature extends Feature<NoneFeatureConfiguration> {

    // swap this for your real spike block once registered, e.g. ModBlocks.SPIRIT_STONE.get()
    private static final BlockState SPIKE_BLOCK = Blocks.TUFF.defaultBlockState();

    public ModSpikeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos pos = context.origin();
        RandomSource random = context.random();
        WorldGenLevel level = context.level();

        while (level.isEmptyBlock(pos) && pos.getY() > level.getMinBuildHeight() + 2) {
            pos = pos.below();
        }

        // relaxed from vanilla's SNOW_BLOCK-only check — accept any solid, non-air ground
        if (level.getBlockState(pos).isAir()) {
            return false;
        }

        pos = pos.above(random.nextInt(4));
        int height = random.nextInt(4) + 7;
        int radius = height / 4 + random.nextInt(2);
        if (radius > 1 && random.nextInt(60) == 0) {
            pos = pos.above(10 + random.nextInt(30));
        }

        for (int k = 0; k < height; ++k) {
            float f = (1.0F - (float) k / (float) height) * (float) radius;
            int l = Mth.ceil(f);

            for (int i1 = -l; i1 <= l; ++i1) {
                float f1 = (float) Mth.abs(i1) - 0.25F;

                for (int j1 = -l; j1 <= l; ++j1) {
                    float f2 = (float) Mth.abs(j1) - 0.25F;
                    if ((i1 == 0 && j1 == 0 || !(f1 * f1 + f2 * f2 > f * f))
                            && (i1 != -l && i1 != l && j1 != -l && j1 != l || !(random.nextFloat() > 0.75F))) {

                        BlockState state = level.getBlockState(pos.offset(i1, k, j1));
                        if (state.isAir() || isDirt(state) || state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.STONE)) {
                            this.setBlock(level, pos.offset(i1, k, j1), SPIKE_BLOCK);
                        }

                        if (k != 0 && l > 1) {
                            state = level.getBlockState(pos.offset(i1, -k, j1));
                            if (state.isAir() || isDirt(state) || state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.STONE)) {
                                this.setBlock(level, pos.offset(i1, -k, j1), SPIKE_BLOCK);
                            }
                        }
                    }
                }
            }
        }

        int base = radius - 1;
        if (base < 0) base = 0;
        else if (base > 1) base = 1;

        for (int l1 = -base; l1 <= base; ++l1) {
            for (int i2 = -base; i2 <= base; ++i2) {
                BlockPos below = pos.offset(l1, -1, i2);
                int drop = 50;
                if (Math.abs(l1) == 1 && Math.abs(i2) == 1) {
                    drop = random.nextInt(5);
                }

                while (below.getY() > 50) {
                    BlockState state = level.getBlockState(below);
                    if (!state.isAir() && !isDirt(state) && !state.is(Blocks.SNOW_BLOCK)
                            && !state.is(Blocks.STONE) && state != SPIKE_BLOCK) {
                        break;
                    }
                    this.setBlock(level, below, SPIKE_BLOCK);
                    below = below.below();
                    --drop;
                    if (drop <= 0) {
                        below = below.below(random.nextInt(5) + 1);
                        drop = random.nextInt(5);
                    }
                }
            }
        }

        return true;
    }
}