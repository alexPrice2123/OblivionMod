package net.kaupenjoe.tutorialmod.block.entity;

import net.kaupenjoe.tutorialmod.energy.EnergyEssenceRegistry;
import net.kaupenjoe.tutorialmod.energy.EnergyFusionRegistry;
import net.kaupenjoe.tutorialmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class EssenceTableBlockEntity extends BlockEntity {
    private static final int MAX_SEARCH_RADIUS = 5;
    private static final int PROCESS_TIME = 100; // 5 seconds at 20 ticks/sec

    private ItemStack essenceItem = ItemStack.EMPTY;
    private boolean processing = false;
    private int processTicks = 0;

    private BlockPos linkedPosA = null;
    private BlockPos linkedPosB = null; // null when using a single energy source
    private Item resultEnergyItem = null;

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

    public boolean tryInsertEssence(ItemStack stack) {
        if (hasEssenceItem() || processing) return false;
        if (!stack.is(ModItems.ESSENCE.get())) return false;

        this.essenceItem = stack.copyWithCount(1);
        syncToClient();
        return true;
    }

    public ItemStack tryExtractEssence() {
        if (processing || essenceItem.isEmpty()) return ItemStack.EMPTY;
        ItemStack out = essenceItem;
        essenceItem = ItemStack.EMPTY;
        syncToClient();
        return out;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EssenceTableBlockEntity be) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        if (!be.hasEssenceItem()) {
            be.resetProcessState(level);
            return;
        }

        if (!be.processing) {
            be.tryStartProcess(serverLevel, pos);
            return;
        }

        be.continueProcess(serverLevel, pos);
    }

    // ---------------------------------------------------------------
    // Detection & start
    // ---------------------------------------------------------------

    private record EnergySource(BlockPos pos, Item item, int tier, int distance) {}

    private void tryStartProcess(ServerLevel level, BlockPos origin) {
        List<EnergySource> sources = findEnergySources(level, origin);

        if (sources.isEmpty()) {
            return; // still waiting, not a failure
        }

        if (sources.size() == 1) {
            beginSingleProcess(level, sources.get(0));
            return;
        }

        if (sources.size() == 2) {
            EnergySource a = sources.get(0);
            EnergySource b = sources.get(1);

            if (a.tier() != b.tier()) {
                failProcess(level, origin);
                return;
            }

            int requiredDistance = a.tier() + 1; // tier N needs an N-block gap
            if (a.distance() != requiredDistance || b.distance() != requiredDistance) {
                failProcess(level, origin);
                return;
            }

            Item result = EnergyFusionRegistry.getFusionResult(a.item(), b.item());
            if (result == null) {
                failProcess(level, origin);
                return;
            }

            beginFusionProcess(level, a, b, result);
            return;
        }

        // More than 2 sources detected -> invalid setup
        failProcess(level, origin);
    }

    private List<EnergySource> findEnergySources(Level level, BlockPos origin) {
        List<EnergySource> found = new ArrayList<>();

        for (BlockPos check : BlockPos.betweenClosed(
                origin.offset(-MAX_SEARCH_RADIUS, -MAX_SEARCH_RADIUS, -MAX_SEARCH_RADIUS),
                origin.offset(MAX_SEARCH_RADIUS, MAX_SEARCH_RADIUS, MAX_SEARCH_RADIUS))) {
            if (check.equals(origin)) continue;

            if (level.getBlockEntity(check) instanceof EnergyTableBlockEntity energyBe) {
                if (!energyBe.hasEnergyItem() || energyBe.isBeingDrained()) continue;

                Item item = energyBe.getEnergyItem().getItem();
                if (!EnergyEssenceRegistry.isValidEnergyItem(item)) continue;

                int distance = Math.max(
                        Math.max(Math.abs(check.getX() - origin.getX()), Math.abs(check.getY() - origin.getY())),
                        Math.abs(check.getZ() - origin.getZ()));

                found.add(new EnergySource(check.immutable(), item, EnergyEssenceRegistry.getTier(item), distance));
            }
        }

        return found;
    }

    private void beginSingleProcess(ServerLevel level, EnergySource src) {
        if (level.getBlockEntity(src.pos()) instanceof EnergyTableBlockEntity energyBe) {
            energyBe.setBeingDrained(true);
        }

        this.linkedPosA = src.pos();
        this.linkedPosB = null;
        this.resultEnergyItem = src.item();
        this.processing = true;
        this.processTicks = 0;
        syncToClient();
    }

    private void beginFusionProcess(ServerLevel level, EnergySource a, EnergySource b, Item result) {
        if (level.getBlockEntity(a.pos()) instanceof EnergyTableBlockEntity beA) beA.setBeingDrained(true);
        if (level.getBlockEntity(b.pos()) instanceof EnergyTableBlockEntity beB) beB.setBeingDrained(true);

        this.linkedPosA = a.pos();
        this.linkedPosB = b.pos();
        this.resultEnergyItem = result;
        this.processing = true;
        this.processTicks = 0;
        syncToClient();
    }

    // ---------------------------------------------------------------
    // Ticking the process
    // ---------------------------------------------------------------

    private void continueProcess(ServerLevel level, BlockPos origin) {
        EnergyTableBlockEntity beA = getEnergyTableAt(level, linkedPosA);
        EnergyTableBlockEntity beB = linkedPosB != null ? getEnergyTableAt(level, linkedPosB) : null;

        boolean sourceAValid = beA != null && beA.hasEnergyItem();
        boolean sourceBValid = linkedPosB == null || (beB != null && beB.hasEnergyItem());

        if (!sourceAValid || !sourceBValid) {
            resetProcessState(level); // a source disappeared mid-process, abort quietly
            return;
        }

        processTicks++;

        spawnBeamParticles(level, origin, linkedPosA, beA.getEnergyItem().getItem());
        if (linkedPosB != null) {
            spawnBeamParticles(level, origin, linkedPosB, beB.getEnergyItem().getItem());
        }

        if (processTicks >= PROCESS_TIME) {
            finishProcess(level, beA, beB);
        }
    }

    private void finishProcess(ServerLevel level, EnergyTableBlockEntity beA, EnergyTableBlockEntity beB) {
        // 1. Consume the energy items from the tables and reset them
        if (beA != null) {
            beA.consumeEnergyItem();
            beA.setBeingDrained(false);
            beA.setChanged();
            level.sendBlockUpdated(beA.getBlockPos(), beA.getBlockState(), beA.getBlockState(), 3);
        }

        if (beB != null) {
            beB.consumeEnergyItem();
            beB.setBeingDrained(false);
            beB.setChanged();
            level.sendBlockUpdated(beB.getBlockPos(), beB.getBlockState(), beB.getBlockState(), 3);
        }

        // 2. Determine the resulting essence item
        if (this.resultEnergyItem != null) {
            Item essenceResult = EnergyEssenceRegistry.getEssenceFor(this.resultEnergyItem);

            if (essenceResult != null) {
                // Temporarily set the item slot so the eject method can find it
                this.essenceItem = new ItemStack(essenceResult);

                // 3. Spawn success effects
                spawnSuccessParticles(level, getBlockPos(), this.resultEnergyItem);

                // 4. Kick the item out into the world
                ejectEssence(level, getBlockPos());
            } else {
                // DIAGNOSTIC LOG: This prints to your console if the essence registration is missing!
                System.out.println("[TutorialMod Error] Process finished, but EnergyEssenceRegistry has no essence registered for item: "
                        + this.resultEnergyItem.toString());
            }
        } else {
            System.out.println("[TutorialMod Error] Process finished, but resultEnergyItem was null!");
        }

        // 5. Reset this essence table's process state
        resetProcessState(level);
    }

    private void failProcess(ServerLevel level, BlockPos origin) {
        spawnFailParticles(level, origin);
        ejectEssence(level, origin);
        resetProcessState(level);
    }

    private void resetProcessState(Level level) {
        if (linkedPosA != null && level.getBlockEntity(linkedPosA) instanceof EnergyTableBlockEntity be) {
            be.setBeingDrained(false);
        }
        if (linkedPosB != null && level.getBlockEntity(linkedPosB) instanceof EnergyTableBlockEntity be) {
            be.setBeingDrained(false);
        }
        this.linkedPosA = null;
        this.linkedPosB = null;
        this.resultEnergyItem = null;
        this.processing = false;
        this.processTicks = 0;
        syncToClient();
    }

    private EnergyTableBlockEntity getEnergyTableAt(Level level, BlockPos pos) {
        if (pos == null) return null;
        return level.getBlockEntity(pos) instanceof EnergyTableBlockEntity be ? be : null;
    }

    private void ejectEssence(ServerLevel level, BlockPos origin) {
        if (essenceItem.isEmpty()) return;

        ItemEntity itemEntity = new ItemEntity(level,
                origin.getX() + 0.5, origin.getY() + 1.1, origin.getZ() + 0.5,
                essenceItem);
        itemEntity.setDeltaMovement(
                (level.random.nextDouble() - 0.5) * 0.2,
                0.3,
                (level.random.nextDouble() - 0.5) * 0.2);
        level.addFreshEntity(itemEntity);

        essenceItem = ItemStack.EMPTY;
    }

    // ---------------------------------------------------------------
    // Particles
    // ---------------------------------------------------------------

    private void spawnBeamParticles(ServerLevel level, BlockPos from, BlockPos to, Item sourceEnergy) {
        float[] color = EnergyEssenceRegistry.getColor(sourceEnergy);

        double x1 = to.getX() + 0.5, y1 = to.getY() + 1.0, z1 = to.getZ() + 0.5;
        double x2 = from.getX() + 0.5, y2 = from.getY() + 1.0, z2 = from.getZ() + 0.5;

        double dx = x2 - x1, dy = y2 - y1, dz = z2 - z1;

        DustParticleOptions dust = new DustParticleOptions(new Vector3f(color[0], color[1], color[2]), 1.2f);

        level.sendParticles(dust,
                x1 + dx * 0.5, y1 + dy * 0.5, z1 + dz * 0.5,
                1, dx * 0.15, dy * 0.15, dz * 0.15, 0.0);
    }

    private void spawnFailParticles(ServerLevel level, BlockPos origin) {
        double x = origin.getX() + 0.5, y = origin.getY() + 1.1, z = origin.getZ() + 0.5;
        DustParticleOptions redDust = new DustParticleOptions(new Vector3f(1.0f, 0.0f, 0.0f), 1.5f);
        level.sendParticles(redDust, x, y, z, 12, 0.2, 0.2, 0.2, 0.02);
    }

    private void spawnSuccessParticles(ServerLevel level, BlockPos origin, Item sourceEnergy) {
        double x = origin.getX() + 0.5, y = origin.getY() + 1.1, z = origin.getZ() + 0.5;

        // Dynamically grab the RGB color array assigned to this energy item type
        float[] color = EnergyEssenceRegistry.getColor(sourceEnergy);

        DustParticleOptions coloredDust = new DustParticleOptions(new Vector3f(color[0], color[1], color[2]), 1.5f);

        // Burst out 12 particles in a small cloud right above the essence table
        level.sendParticles(coloredDust, x, y, z, 12, 0.2, 0.2, 0.2, 0.02);
    }

    // ---------------------------------------------------------------
    // Sync / NBT
    // ---------------------------------------------------------------

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

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!essenceItem.isEmpty()) {
            tag.put("EssenceItem", essenceItem.save(registries, new CompoundTag()));
        }
        tag.putBoolean("Processing", processing);
        tag.putInt("ProcessTicks", processTicks);
        if (linkedPosA != null) tag.putLong("LinkedPosA", linkedPosA.asLong());
        if (linkedPosB != null) tag.putLong("LinkedPosB", linkedPosB.asLong());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.essenceItem = tag.contains("EssenceItem")
                ? ItemStack.parseOptional(registries, tag.getCompound("EssenceItem"))
                : ItemStack.EMPTY;
        this.processing = tag.getBoolean("Processing");
        this.processTicks = tag.getInt("ProcessTicks");
        this.linkedPosA = tag.contains("LinkedPosA") ? BlockPos.of(tag.getLong("LinkedPosA")) : null;
        this.linkedPosB = tag.contains("LinkedPosB") ? BlockPos.of(tag.getLong("LinkedPosB")) : null;
        // resultEnergyItem is intentionally not persisted; if the world reloads mid-process,
        // it just re-derives on the next successful finish (harmless edge case).
    }
}