package net.gxb.oblivion.worldgen;

import net.gxb.oblivion.block.ModBlocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Shared logic between ModSpikeFeature (big peaks) and ModRidgeFillerFeature
 * (small common filler bumps) so both features use the exact same elevation
 * gradient and ground-replacement rules and can't drift apart.
 */
public class ModSpikeUtils {

    // Mirrors the Spirit Peaks surface rule bands from Oblivion.java so spikes
    // blend with the elevation-based stone layers instead of being solid tuff.
    // The jitter window is wide (+/-4) so each band fades into the next over a
    // real gradient instead of a hard line or a barely-visible dither.
    public static BlockState spikeBlockForY(RandomSource random, int y) {
        int jitter = random.nextInt(9) - 4; // -4..+4 block gradient zone
        int effectiveY = y + jitter;
        if (effectiveY >= 146) return ModBlocks.SPIRIT_STONE.get().defaultBlockState();
        if (effectiveY >= 135) return Blocks.DEEPSLATE.defaultBlockState();
        if (effectiveY >= 105) return Blocks.TUFF.defaultBlockState();
        return Blocks.STONE.defaultBlockState();
    }

    public static boolean isReplaceableGround(BlockState state) {
        return state.isAir() || state.is(BlockTags.DIRT) || state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.STONE)
                || state.is(Blocks.TUFF) || state.is(Blocks.DEEPSLATE) || state.is(ModBlocks.SPIRIT_STONE.get());
    }

    /**
     * Turns a pure circular cross-section into a jagged, angular one by adding
     * two overlapping angular sine waves around the radius. A perfect circle at
     * every height layer is what makes a tapered mound read as a smooth round
     * "turd" shape instead of a weathered rock formation — real rock spires
     * have ridges, facets, and notches around their circumference, not a clean
     * radius. The same freq/phase per spike keeps the ridges running vertically
     * (like rock strata) instead of changing randomly layer to layer.
     */
    public static float angularRoughness(int i, int j, float freq1, float phase1, float amp1,
                                          float freq2, float phase2, float amp2) {
        if (i == 0 && j == 0) return 1.0F;
        double angle = Math.atan2(j, i);
        float wave1 = amp1 * (float) Math.sin(freq1 * angle + phase1);
        float wave2 = amp2 * (float) Math.sin(freq2 * angle + phase2);
        return Mth.clamp(1.0F + wave1 + wave2, 0.25F, 1.6F);
    }
}
