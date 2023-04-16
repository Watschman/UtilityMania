package com.watschman.utilitymania.common.command;

import com.watschman.utilitymania.UtilityMania;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class UtilityManiaCommandHandler {
    private final UtilityManiaCommands utilityManiaCommands;

    public UtilityManiaCommandHandler() {
        this.utilityManiaCommands = new UtilityManiaCommands();
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        UtilityMania.LOGGER.info("Registering Commands");
        utilityManiaCommands.initArgumentPlaceHolders();
        utilityManiaCommands.registerCommands(event.getDispatcher(), event.getCommandSelection());
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player.getTags().contains(GodCommand.COMMAND_TAG)) {
            player.getAbilities().flying = true;
            player.getAbilities().mayfly = true;
            player.getAbilities().invulnerable = true;
            player.onUpdateAbilities();
        }
    }
}
