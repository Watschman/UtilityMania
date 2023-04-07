package com.watschman.utilitymania.common.command;

import com.watschman.utilitymania.UtilityMania;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class UtilityManiaCommandHandler {
    private final UtilityManiaCommands utilityManiaCommands;

    public UtilityManiaCommandHandler() {
        this.utilityManiaCommands = new UtilityManiaCommands();
    }
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        UtilityMania.LOGGER.info("Registering Commands");
        utilityManiaCommands.registerCommands(event.getDispatcher(), event.getCommandSelection());
    }
}
