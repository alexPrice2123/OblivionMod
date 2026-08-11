package net.gxb.oblivion.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Small, common rocky bumps that fill the gaps between the big ModSpikeFeature
 * peaks. On their own they're too small to read as "spikes", but placed densely
 * across a chunk they break up flat/smooth ground so the whole biome silhouette
 * reads as continuously jagged instead of tall spikes surrounded by flat gaps.
 */
public class ModRidgeFillerFeature extends Feature<NoneFeatureConfiguration> {

    public ModRidgeFillerFeature(Codec<NoneFeatureConfiguration> codec) {
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

        if (level.getBlockState(pos).isAir()) {
            return false;
        }

        int height = random.nextInt(6) + 4; // 4-9 tall — a rocky bump, not a peak
        int baseRadius = Math.max(1, height / 3);

        float taperSharpness = 1.6F + random.nextFloat() * 1.4F;

        float angFreq1 = 3.0F + random.nextFloat() * 3.0F;
        float angPhase1 = random.nextFloat() * (float) Math.PI * 2.0F;
        float angAmp1 = 0.2F + random.nextFloat() * 0.15F;
        float angFreq2 = 7.0F + random.nextFloat() * 5.0F;
        float angPhase2 = random.nextFloat() * (float) Math.PI * 2.0F;
        float angAmp2 = 0.12F + random.nextFloat() * 0.1F;

        for (int k = 0; k < height; ++k) {
            float t = (float) k / (float) height;
            float f = (float) Math.pow(1.0 - t, taperSharpness) * (float) baseRadius;
            int l = Mth.ceil(f * 1.6F);

            for (int i1 = -l; i1 <= l; ++i1) {
                float f1 = (float) Mth.abs(i1) - 0.25F;

                for (int j1 = -l; j1 <= l; ++j1) {
                    float f2 = (float) Mth.abs(j1) - 0.25F;
                    float roughness = ModSpikeUtils.angularRoughness(i1, j1, angFreq1, angPhase1, angAmp1, angFreq2, angPhase2, angAmp2);
                    float fJagged = f * roughness;
                    if (i1 == 0 && j1 == 0 || !(f1 * f1 + f2 * f2 > fJagged * fJagged)) {
                        BlockPos upPos = pos.offset(i1, k, j1);
                        BlockState state = level.getBlockState(upPos);
                        if (ModSpikeUtils.isReplaceableGround(state)) {
                            this.setBlock(level, upPos, ModSpikeUtils.spikeBlockForY(random, upPos.getY()));
                        }
                    }
                }
            }
        }

        return true;
    }
}
