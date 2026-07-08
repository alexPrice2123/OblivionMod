package net.kaupenjoe.tutorialmod.event;

import net.kaupenjoe.tutorialmod.TutorialMod;
import net.kaupenjoe.tutorialmod.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

@EventBusSubscriber(modid = TutorialMod.MOD_ID)
public class ModAnvilEvent {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        // Check if both items are present
        boolean isCryingObsidian = left.is(Items.CRYING_OBSIDIAN) || right.is(Items.CRYING_OBSIDIAN);
        boolean isExtractionCore = left.is(ModItems.EXTRACTION_CORE.get()) || right.is(ModItems.EXTRACTION_CORE.get());

        if (isCryingObsidian && isExtractionCore) {
            // Enforce that the left item must only be a count of 1 to prevent losing a whole stack
            if (left.getCount() == 1) {
                event.setOutput(new ItemStack(ModItems.ESSENCE.get()));
                event.setCost(10);
                event.setMaterialCost(1);   // Consumes 1 from the right slot
            }
        }
    }
}