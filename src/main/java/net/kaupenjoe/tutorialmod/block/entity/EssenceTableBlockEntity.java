package net.kaupenjoe.tutorialmod.block.entity;

import net.kaupenjoe.tutorialmod.energy.EnergyEssenceRegistry;
import net.kaupenjoe.tutorialmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EssenceTableBlockEntity extends BlockEntity {
    private static final int SEARCH_RADIUS = 2;
    private static final int PROCESS_TIME = 100; // 5 seconds at 20 ticks/sec

    private ItemStack essenceItem = ItemStack.EMPTY;
    private BlockPos linkedEnergyTablePos = null;
    private int processTicks = 0;
    private boolean processing = false;

    public EssenceTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ESSENCE_TABLE_BE.get(), pos, state);
    }

    public ItemStack getEssenceItem() {
        return essenceItem;
    }

    public boolean hasEssenceItem() {
        return !essenceItem.isEmpty();
    }

    public boolean isProcessing() {
        return processing;
    }

    /** Only accepts the Raw Essence item. Returns false (and consumes nothing) otherwise. */
    public boolean tryInsertEssence(ItemStack stack) {
        if (hasEssenceItem() || processing) return false;
        if (!stack.is(ModItems.ESSENCE.get())) return false;   // was RAW_ESSENCE

        this.essenceItem = stack.copyWithCount(1);
        setChanged();
        return true;
    }

    public ItemStack tryExtractEssence() {
        if (processing || essenceItem.isEmpty()) return ItemStack.EMPTY;
        ItemStack out = essenceItem;
        essenceItem = ItemStack.EMPTY;
        setChanged();
        return out;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EssenceTableBlockEntity be) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        if (!be.hasEssenceItem()) {
            be.processing = false;
            be.processTicks = 0;
            return;
        }

        if (!be.processing) {
            BlockPos target = be.findValidEnergyTable(level, pos);
            if (target != null) {
                be.linkedEnergyTablePos = target;
                be.processing = true;
                be.processTicks = 0;

                if (level.getBlockEntity(target) instanceof EnergyTableBlockEntity energyBe) {
                    energyBe.setBeingDrained(true);
                }
            }
            return;
        }

        if (be.linkedEnergyTablePos == null ||
                !(level.getBlockEntity(be.linkedEnergyTablePos) instanceof EnergyTableBlockEntity energyBe) ||
                !energyBe.hasEnergyItem()) {
            be.processing = false;
            be.processTicks = 0;
            be.linkedEnergyTablePos = null;
            return;
        }

        be.processTicks++;
        be.spawnLinkParticles(serverLevel, pos, be.linkedEnergyTablePos);

        if (be.processTicks >= PROCESS_TIME) {
            ItemStack drainedEnergy = energyBe.consumeEnergyItem();
            be.finishProcess(drainedEnergy);
        }
    }

    private void finishProcess(ItemStack energySource) {
        Item resultEssence = EnergyEssenceRegistry.getEssenceFor(energySource.getItem());

        if (resultEssence != null) {
            essenceItem = new ItemStack(resultEssence);
        }
        // If somehow no mapping exists, the Raw Essence just stays as-is (safety fallback).

        processing = false;
        processTicks = 0;
        linkedEnergyTablePos = null;
        setChanged();
    }

    private BlockPos findValidEnergyTable(Level level, BlockPos origin) {
        for (BlockPos check : BlockPos.betweenClosed(
                origin.offset(-SEARCH_RADIUS, -SEARCH_RADIUS, -SEARCH_RADIUS),
                origin.offset(SEARCH_RADIUS, SEARCH_RADIUS, SEARCH_RADIUS))) {
            if (check.equals(origin)) continue;
            if (level.getBlockEntity(check) instanceof EnergyTableBlockEntity energyBe) {
                if (energyBe.hasEnergyItem() && !energyBe.isBeingDrained()
                        && EnergyEssenceRegistry.isValidEnergyItem(energyBe.getEnergyItem().getItem())) {
                    return check.immutable();
                }
            }
        }
        return null;
    }

    private void spawnLinkParticles(ServerLevel level, BlockPos from, BlockPos to) {
        double x1 = to.getX() + 0.5, y1 = to.getY() + 1.0, z1 = to.getZ() + 0.5;
        double x2 = from.getX() + 0.5, y2 = from.getY() + 1.0, z2 = from.getZ() + 0.5;

        double dx = x2 - x1, dy = y2 - y1, dz = z2 - z1;

        level.sendParticles(ParticleTypes.WITCH,
                x1 + dx * 0.5, y1 + dy * 0.5, z1 + dz * 0.5,
                1, dx * 0.1, dy * 0.1, dz * 0.1, 0.0);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("EssenceItem", essenceItem.save(registries, new CompoundTag()));
        tag.putBoolean("Processing", processing);
        tag.putInt("ProcessTicks", processTicks);
        if (linkedEnergyTablePos != null) {
            tag.putLong("LinkedPos", linkedEnergyTablePos.asLong());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.essenceItem = ItemStack.parseOptional(registries, tag.getCompound("EssenceItem"));
        this.processing = tag.getBoolean("Processing");
        this.processTicks = tag.getInt("ProcessTicks");
        if (tag.contains("LinkedPos")) {
            this.linkedEnergyTablePos = BlockPos.of(tag.getLong("LinkedPos"));
        }
    }
}