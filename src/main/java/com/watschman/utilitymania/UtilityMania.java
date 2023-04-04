package com.watschman.utilitymania;

import com.mojang.logging.LogUtils;
import com.watschman.utilitymania.common.CommonSetupEventSubscriber;
import com.watschman.utilitymania.common.config.UtilityManiaConfig;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

@Mod(UtilityMania.MOD_ID)
public class UtilityMania {
    public static final String MOD_ID = "utilitymania";
    public static final String MOD_Name = "Utility Mania";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public UtilityMania() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UtilityManiaConfig.CONFIG_SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().register(new CommonSetupEventSubscriber());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Hello World Server Starting");
    }


}
