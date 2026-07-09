package net.gxb.oblivion;

import net.gxb.oblivion.block.ModBlocks;
import net.gxb.oblivion.block.entity.ModBlockEntities;
import net.gxb.oblivion.energy.ModEnergyMappings;
import net.gxb.oblivion.item.ModCreativeModeTabs;
import net.gxb.oblivion.item.ModItems;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.gxb.oblivion.client.renderer.EssenceTableBlockEntityRenderer;
import net.gxb.oblivion.client.renderer.EnergyTableBlockEntityRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(Oblivion.MOD_ID)
public class Oblivion {
    public static final String MOD_ID = "oblivionmobgxb";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Oblivion(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        // REMOVED ModEnergyMappings.register() from here because it was too early!

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModEnergyMappings.init(); // Use init() instead of register()
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.ESSENCE_TABLE_BE.get(), EssenceTableBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.ENERGY_TABLE_BE.get(), EnergyTableBlockEntityRenderer::new);
        }
    }
}