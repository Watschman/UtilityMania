package com.watschman.utilitymania.common.command;

import com.watschman.utilitymania.UtilityMania;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
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
        GodCommand.handlePlayerLogin(event);
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        MinifyCommand.handleLivingAttack(event);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        MinifyCommand.handlePlayerRespawn(event);
    }

    @SubscribeEvent
    public void onSizeEvent(EntityEvent.Size event) {
        MinifyCommand.handleSizeEvent(event);
    }
}
