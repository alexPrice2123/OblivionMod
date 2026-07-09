package net.gxb.oblivion.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
        syncToClient();
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

    /** Called by the Essence Table when the process finishes. */
    public void consumeEnergyItem() {
        // 1. Actually clear the item stack
        this.energyItem = ItemStack.EMPTY;

        // 2. Save the change to the server
        this.setChanged();

        // 3. Force sync to the client so the item visually disappears instantly
        if (this.level != null && !this.level.isClientSide()) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    private void syncToClient() {
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        loadAdditional(tag, registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // Automatically handles the incoming network packet on the client side
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            loadAdditional(tag, registries);
            if (this.level != null && this.level.isClientSide()) {
                // Forces the client chunk and your custom renderer to redraw the block completely empty
                this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!energyItem.isEmpty()) {
            tag.put("EnergyItem", energyItem.save(registries, new CompoundTag()));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("EnergyItem")) {
            this.energyItem = ItemStack.parseOptional(registries, tag.getCompound("EnergyItem"));
        } else {
            // CRITICAL: If the NBT data does not have an energy item, clear it out explicitly.
            this.energyItem = ItemStack.EMPTY;
        }
    }
}