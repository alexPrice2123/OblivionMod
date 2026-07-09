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
        basicItem(ModItems.LIGHTNING_ENERGY.get());
    }
}