package net.kaupenjoe.tutorialmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kaupenjoe.tutorialmod.block.entity.EnergyTableBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class EnergyTableBlockEntityRenderer implements BlockEntityRenderer<EnergyTableBlockEntity> {
    private final ItemRenderer itemRenderer;

    public EnergyTableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(EnergyTableBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack stack = be.getEnergyItem();
        if (stack.isEmpty()) return;

        poseStack.pushPose();
        poseStack.translate(0.5, 1.35, 0.5);

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