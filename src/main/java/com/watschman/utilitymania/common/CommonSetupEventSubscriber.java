package com.watschman.utilitymania.common;

import com.watschman.utilitymania.UtilityMania;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = UtilityMania.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetupEventSubscriber {
    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        UtilityMania.LOGGER.info("Common Setup");
    }
}
