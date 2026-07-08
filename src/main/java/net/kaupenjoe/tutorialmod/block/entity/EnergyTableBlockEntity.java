package net.kaupenjoe.tutorialmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EnergyTableBlockEntity extends BlockEntity {
    private ItemStack energyItem = ItemStack.EMPTY;
    private boolean beingDrained = false;

    public EnergyTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ENERGY_TABLE_BE.get(), pos, state);
    }

    public ItemStack getEnergyItem() {
        return energyItem;
    }

    public void setEnergyItem(ItemStack stack) {
        this.energyItem = stack;
        setChanged();
    }

    public boolean hasEnergyItem() {
        return !energyItem.isEmpty();
    }

    public boolean isBeingDrained() {
        return beingDrained;
    }

    public void setBeingDrained(boolean drained) {
        this.beingDrained = drained;
    }

    /** Called by the Essence Table when the process finishes. Returns the item that was drained. */
    public ItemStack consumeEnergyItem() {
        ItemStack drained = energyItem.copyWithCount(1);
        energyItem.shrink(1);
        if (energyItem.isEmpty()) {
            energyItem = ItemStack.EMPTY;
        }
        beingDrained = false;
        setChanged();
        return drained;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("EnergyItem", energyItem.save(registries, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.energyItem = ItemStack.parseOptional(registries, tag.getCompound("EnergyItem"));
    }
}