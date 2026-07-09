package net.gxb.oblivion.datagen;

import net.gxb.oblivion.Oblivion;
import net.gxb.oblivion.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModModelProvider extends ItemModelProvider {

    public ModModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Oblivion.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.EXTRACTION_CORE.get());
        basicItem(ModItems.ESSENCE_CORE.get());
        basicItem(ModItems.ESSENCE.get());

        basicItem(ModItems.LIGHTNING_ENERGY.get());
        basicItem(ModItems.LIGHTNING_ESSENCE.get());
        basicItem(ModItems.VOID_ENERGY.get());
        basicItem(ModItems.VOID_ESSENCE.get());
        basicItem(ModItems.PURE_ENERGY.get());
        basicItem(ModItems.PURE_ESSENCE.get());
        basicItem(ModItems.CORRUPT_ENERGY.get());
        basicItem(ModItems.CORRUPT_ESSENCE.get());

        basicItem(ModItems.ABYSSAL_ENERGY.get());
        basicItem(ModItems.ABYSSAL_ESSENCE.get());
        basicItem(ModItems.DIVINE_ENERGY.get());
        basicItem(ModItems.DIVINE_ESSENCE.get());
        basicItem(ModItems.DUALITY_ENERGY.get());
        basicItem(ModItems.DUALITY_ESSENCE.get());
        basicItem(ModItems.ENTROPY_ENERGY.get());
        basicItem(ModItems.ENTROPY_ESSENCE.get());
        basicItem(ModItems.WRAITH_ENERGY.get());
        basicItem(ModItems.WRAITH_ESSENCE.get());
        basicItem(ModItems.ASTRAL_ENERGY.get());
        basicItem(ModItems.ASTRAL_ESSENCE.get());
    }
}