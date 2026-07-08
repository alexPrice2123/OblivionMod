package net.kaupenjoe.tutorialmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kaupenjoe.tutorialmod.block.entity.EssenceTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class EssenceTableBlockEntityRenderer implements BlockEntityRenderer<EssenceTableBlockEntity> {
    private final ItemRenderer itemRenderer;

    public EssenceTableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(EssenceTableBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack stack = be.getEssenceItem();
        if (stack.isEmpty()) return;

        poseStack.pushPose();

        // Center above the block, floating just above the top face
        poseStack.translate(0.5, 1.1, 0.5);

        // Slow spin based on world time for a nice "magic item" feel
        float rotation = (be.getLevel().getGameTime() + partialTick) * 2.0f;
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rotation));

        poseStack.scale(0.5f, 0.5f, 0.5f);

        itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                packedLight,
                packedOverlay,
                poseStack,
                bufferSource,
                be.getLevel(),
                0
        );

        poseStack.popPose();
    }
}