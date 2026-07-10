package net.gxb.oblivion;

import net.gxb.oblivion.block.ModBlocks;
import net.gxb.oblivion.block.entity.ModBlockEntities;
import net.gxb.oblivion.energy.ModEnergyMappings;
import net.gxb.oblivion.item.ModCreativeModeTabs;
import net.gxb.oblivion.item.ModItems;
import net.gxb.oblivion.worldgen.ModRegion;
import net.gxb.oblivion.worldgen.biome.ModBiomes;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import net.gxb.oblivion.worldgen.tree.ModTrunkPlacerTypes;

@Mod(Oblivion.MOD_ID)
public class Oblivion {
    public static final String MOD_ID = "oblivionmodgxb";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Oblivion(IEventBus modEventBus, ModContainer modContainer) {
        // Register Mod Bus listeners directly
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::registerRenderers);

        // Register game bus listeners
        NeoForge.EVENT_BUS.register(this);

        // Registry registrations
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModTrunkPlacerTypes.TRUNK_PLACER_TYPES.register(modEventBus); // <-- add it here

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModEnergyMappings.init();
            Regions.register(new ModRegion(
                    ResourceLocation.fromNamespaceAndPath(Oblivion.MOD_ID, "overworld"), 10));

            SurfaceRuleManager.addSurfaceRules(
                    SurfaceRuleManager.RuleCategory.OVERWORLD,
                    Oblivion.MOD_ID,
                    SurfaceRules.ifTrue(
                            SurfaceRules.isBiome(ModBiomes.SPIRIT_PEAKS),
                            SurfaceRules.state(Blocks.TUFF.defaultBlockState())
                    )
            );
        });
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        // Client initialization logic goes here
    }

    private void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.ESSENCE_TABLE_BE.get(), EssenceTableBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ENERGY_TABLE_BE.get(), EnergyTableBlockEntityRenderer::new);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}