package net.kaupenjoe.tutorialmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.kaupenjoe.tutorialmod.block.entity.EnergyTableBlockEntity;
import net.kaupenjoe.tutorialmod.energy.EnergyEssenceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class EnergyTableBlock extends BaseEntityBlock {
    public static final MapCodec<EnergyTableBlock> CODEC = simpleCodec(EnergyTableBlock::new);

    public EnergyTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) return ItemInteractionResult.SUCCESS;

        if (level.getBlockEntity(pos) instanceof EnergyTableBlockEntity be) {
            if (!be.hasEnergyItem() && !stack.isEmpty()
                    && EnergyEssenceRegistry.isValidEnergyItem(stack.getItem())) {
                be.setEnergyItem(stack.copyWithCount(1));
                stack.shrink(1);
                return ItemInteractionResult.CONSUME;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (level.getBlockEntity(pos) instanceof EnergyTableBlockEntity be) {
            if (be.hasEnergyItem() && !be.isBeingDrained()) {
                ItemStack out = be.getEnergyItem();
                be.setEnergyItem(ItemStack.EMPTY);
                player.getInventory().placeItemBackInInventory(out);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergyTableBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}