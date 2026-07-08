package net.kaupenjoe.tutorialmod.datagen;

import net.kaupenjoe.tutorialmod.TutorialMod;
import net.kaupenjoe.tutorialmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.ModelProvider;
import net.minecraft.data.models.model.ModelTemplates;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, TutorialMod.MOD_ID);
    }

    @Override
    protected  void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels){
        itemModels.generateFlatItems(ModItems.EXTRACTION_CORE).get(), ModelTemplates.FLAT_ITEM;
    }
}
