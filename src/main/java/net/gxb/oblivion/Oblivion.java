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
import terrablender.api.RegionType;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.gxb.oblivion.worldgen.ModFeatures;

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
        NeoForge.EVENT_BUS.addListener(this::onBlockInteraction);

        // Registry registrations
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModTrunkPlacerTypes.TRUNK_PLACER_TYPES.register(modEventBus);
        ModFeatures.FEATURES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModEnergyMappings.init();
            Regions.register(new ModRegion(
                    ResourceLocation.fromNamespaceAndPath(Oblivion.MOD_ID, "overworld"),
                    RegionType.OVERWORLD,
                    1));

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

    private void onBlockInteraction(final UseItemOnBlockEvent event) {
        var context = event.getUseOnContext();
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var state = level.getBlockState(pos);

        // Check if the player right-clicked your custom log
        if (state.is(ModBlocks.SPIRIT_LOG.get())) {
            var player = context.getPlayer();
            var itemStack = context.getItemInHand();

            // Check if they are holding an item that can perform axe-stripping actions
            if (itemStack.canPerformAction(net.neoforged.neoforge.common.ItemAbilities.AXE_STRIP)) {
                // Play the vanilla stripping sound effect
                level.playSound(player, pos, net.minecraft.sounds.SoundEvents.AXE_STRIP,
                        net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);

                if (!level.isClientSide) {
                    // Replace the block, keeping its original orientation (axis)
                    level.setBlock(pos, ModBlocks.STRIPPED_SPIRIT_LOG.get().defaultBlockState()
                            .setValue(net.minecraft.world.level.block.RotatedPillarBlock.AXIS,
                                    state.getValue(net.minecraft.world.level.block.RotatedPillarBlock.AXIS)), 11);

                    // Damage the axe by 1 durability point
                    if (player != null) {
                        itemStack.hurtAndBreak(1, player, net.minecraft.world.entity.LivingEntity.getSlotForHand(context.getHand()));
                    }
                }
                event.cancelWithResult(net.minecraft.world.ItemInteractionResult.SUCCESS);
            }
        }
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