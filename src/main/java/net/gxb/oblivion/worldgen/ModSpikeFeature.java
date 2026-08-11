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

public class ModSpikeFeature extends Feature<NoneFeatureConfiguration> {

    public ModSpikeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    // Weighted small/medium/large split so a chunk's spikes actually vary in scale
    // instead of all landing in one narrow height band.
    private static int rollHeight(RandomSource random) {
        float roll = random.nextFloat();
        if (roll < 0.5F) {
            return random.nextInt(5) + 5;    // small: 5-9
        } else if (roll < 0.9F) {
            return random.nextInt(8) + 10;   // medium: 10-17
        } else {
            return random.nextInt(8) + 18;   // large: 18-25
        }
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos pos = context.origin();
        RandomSource random = context.random();
        WorldGenLevel level = context.level();

        while (level.isEmptyBlock(pos) && pos.getY() > level.getMinBuildHeight() + 2) {
            pos = pos.below();
        }

        if (level.getBlockState(pos).isAir()) {
            return false;
        }

        boolean snowyGround = level.getBlockState(pos).is(Blocks.SNOW_BLOCK)
                || level.getBlockState(pos).is(Blocks.POWDER_SNOW)
                || level.getBlockState(pos).is(Blocks.SNOW);

        pos = pos.above(random.nextInt(4));
        int height = rollHeight(random);
        // Thinner relative to height than before — a stubby wide radius is
        // exactly what reads as a round mound instead of a rock spire.
        int baseRadius = Math.max(1, height / 4 + random.nextInt(2));

        float taperSharpness = 2.0F + random.nextFloat() * 1.6F;
        float bodyFraction = 0.12F + random.nextFloat() * 0.15F;
        float erosionFreq = 0.3F + random.nextFloat() * 0.4F;
        float erosionPhase = random.nextFloat() * (float) Math.PI * 2.0F;

        // Angular jaggedness — breaks the circular cross-section into a faceted,
        // ridged rock silhouette instead of a smooth round mound.
        float angFreq1 = 3.0F + random.nextFloat() * 3.0F;
        float angPhase1 = random.nextFloat() * (float) Math.PI * 2.0F;
        float angAmp1 = 0.18F + random.nextFloat() * 0.12F;
        float angFreq2 = 7.0F + random.nextFloat() * 5.0F;
        float angPhase2 = random.nextFloat() * (float) Math.PI * 2.0F;
        float angAmp2 = 0.1F + random.nextFloat() * 0.1F;

        for (int k = 0; k < height; ++k) {
            float t = (float) k / (float) height;

            float shapeFactor;
            if (t < bodyFraction) {
                shapeFactor = 1.0F;
            } else {
                float taperT = (t - bodyFraction) / (1.0F - bodyFraction);
                shapeFactor = (float) Math.pow(1.0 - taperT, taperSharpness);
            }

            float erosion = 1.0F + 0.07F * (float) Math.sin(k * erosionFreq + erosionPhase);

            float f = shapeFactor * erosion * (float) baseRadius;
            int l = Mth.ceil(f * 1.6F); // loop bound needs headroom for the angular bulges

            for (int i1 = -l; i1 <= l; ++i1) {
                float f1 = (float) Mth.abs(i1) - 0.25F;

                for (int j1 = -l; j1 <= l; ++j1) {
                    float f2 = (float) Mth.abs(j1) - 0.25F;
                    float roughness = ModSpikeUtils.angularRoughness(i1, j1, angFreq1, angPhase1, angAmp1, angFreq2, angPhase2, angAmp2);
                    float fJagged = f * roughness;
                    if ((i1 == 0 && j1 == 0 || !(f1 * f1 + f2 * f2 > fJagged * fJagged))
                            && (i1 != -l && i1 != l && j1 != -l && j1 != l || !(random.nextFloat() > 0.72F))) {

                        BlockPos upPos = pos.offset(i1, k, j1);
                        BlockState state = level.getBlockState(upPos);
                        if (ModSpikeUtils.isReplaceableGround(state)) {
                            this.setBlock(level, upPos, ModSpikeUtils.spikeBlockForY(random, upPos.getY()));
                        }
                    }
                }
            }
        }

        BlockPos tip = pos.above(height - 1);
        boolean aboveSnowLine = tip.getY() >= 150;
        if (snowyGround || aboveSnowLine) {
            if (level.getBlockState(tip.above()).isAir()) {
                this.setBlock(level, tip.above(), Blocks.SNOW.defaultBlockState());
            }
        }

        // Radial talus: flares out well beyond the spike's own base radius and
        // gradually thins toward the edge, so the spike reads as growing out of
        // the mountain instead of sitting on top of it with a hard vertical seam.
        int footRadius = baseRadius + 3 + random.nextInt(3);

        for (int l1 = -footRadius; l1 <= footRadius; ++l1) {
            for (int i2 = -footRadius; i2 <= footRadius; ++i2) {
                double dist = Math.sqrt((double) (l1 * l1 + i2 * i2));
                if (dist > footRadius) continue;

                float distFrac = (float) (dist / footRadius);
                float skipChance = 0.1F + distFrac * 0.6F;
                if (random.nextFloat() < skipChance) continue;

                int fillDepth = Math.max(1, Mth.floor((1.0F - distFrac) * 7.0F) + random.nextInt(2));

                BlockPos below = pos.offset(l1, -1, i2);
                int filled = 0;
                while (below.getY() > 50 && filled < fillDepth) {
                    BlockState state = level.getBlockState(below);
                    if (!ModSpikeUtils.isReplaceableGround(state)) {
                        break;
                    }
                    this.setBlock(level, below, ModSpikeUtils.spikeBlockForY(random, below.getY()));
                    below = below.below();
                    ++filled;
                }
            }
        }

        return true;
    }
}
