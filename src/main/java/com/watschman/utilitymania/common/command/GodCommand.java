package com.watschman.utilitymania.common.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.watschman.utilitymania.UtilityMania;
import com.watschman.utilitymania.common.command.placeholder.PlayerPlaceholder;
import com.watschman.utilitymania.common.config.UtilityManiaConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GodCommand extends BaseCommand {

    public static final String COMMAND_TAG = UtilityMania.MOD_ID + ".god";
    private static final String commandSolo = "god";
    private static final String commandWithPlayer = commandSolo + " #" + PlayerPlaceholder.PLACE_HOLDER_TEXT + "#";

    private static final String commandWithPlayerOn = commandWithPlayer + " on";
    private static final String commandWithPlayerOff = commandWithPlayer + " off";

    @Override
    protected List<String> getCommandPaths() {
        return Arrays.asList(commandSolo, commandWithPlayer, commandWithPlayerOn, commandWithPlayerOff);
    }

    @Override
    protected int executeCommand(String command, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        switch (command) {
            case commandSolo -> {
                return this.executeSelf(context.getSource());
            }
            case commandWithPlayer -> {
                return this.executeWithArgument(context.getSource(), context.getArgument(PlayerPlaceholder.ARGUMENT_NAME, EntitySelector.class));
            }
            case commandWithPlayerOn -> {
                return this.executeWithArgument(context.getSource(), context.getArgument(PlayerPlaceholder.ARGUMENT_NAME, EntitySelector.class), true);
            }
            case commandWithPlayerOff -> {
                return this.executeWithArgument(context.getSource(), context.getArgument(PlayerPlaceholder.ARGUMENT_NAME, EntitySelector.class), false);
            }
        }
        throw ICommand.ERROR_COMMAND_NOT_IMPLEMENTED.create();
    }

    private int executeSelf(CommandSourceStack commandSourceStack) throws CommandSyntaxException {
        return this.execute(commandSourceStack, new ServerPlayer[]{commandSourceStack.getPlayerOrException()});
    }

    public static void handlePlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player.getTags().contains(COMMAND_TAG)) {
            makePlayerGod(player);
        }
    }

    private static void makePlayerGod(Player player) {
        player.getAbilities().flying = true;
        player.getAbilities().mayfly = true;
        player.getAbilities().invulnerable = true;
        player.onUpdateAbilities();
    }

    private static void makePlayerMortal(Player player) {
        player.getAbilities().flying = false;
        player.getAbilities().mayfly = false;
        player.getAbilities().invulnerable = false;
        player.onUpdateAbilities();
    }

    private int execute(CommandSourceStack commandSourceStack, ServerPlayer[] players, boolean activateGod) {
        List<String> godPlayers = new ArrayList<>();
        List<String> noGodPlayers = new ArrayList<>();
        List<String> noChangedPlayers = new ArrayList<>();
        for (ServerPlayer player : players) {
            if (!player.gameMode.isSurvival()) {
                continue;
            }
            if (player.getTags().contains(COMMAND_TAG)) {
                if (!activateGod) {
                    noGodPlayers.add(player.getDisplayName().getString());
                    player.removeTag(COMMAND_TAG);
                    makePlayerMortal(player);
                } else {
                    noChangedPlayers.add(player.getDisplayName().getString());
                }
            } else {
                if (activateGod) {
                    player.addTag(COMMAND_TAG);
                    godPlayers.add(player.getDisplayName().getString());
                    makePlayerGod(player);
                } else {
                    noChangedPlayers.add(player.getDisplayName().getString());
                }
            }
        }

        if (godPlayers.size() == 0 && noGodPlayers.size() == 0) {
            commandSourceStack.sendSuccess(MutableComponent.create(new TranslatableContents("Players " + String.join(", ", noChangedPlayers) + " are already " + (activateGod ? "Gods" : "Mortals") + ".")), true);
        }
        if (godPlayers.size() != 0) {
            commandSourceStack.sendSuccess(MutableComponent.create(new TranslatableContents("Turned " + String.join(", ", godPlayers) + " to Gods.")), true);
        }
        if (noGodPlayers.size() != 0) {
            commandSourceStack.sendSuccess(MutableComponent.create(new TranslatableContents("Turned " + String.join(", ", noGodPlayers) + " to Mortals.")), true);
        }
        return 0;
    }

    private int execute(CommandSourceStack commandSourceStack, ServerPlayer[] players) {
        List<String> godPlayers = new ArrayList<>();
        List<String> noGodPlayers = new ArrayList<>();
        for (ServerPlayer player : players) {
            if (!player.gameMode.isSurvival()) {
                continue;
            }
            if (player.getTags().contains(COMMAND_TAG)) {
                noGodPlayers.add(player.getDisplayName().getString());
                player.removeTag(COMMAND_TAG);
                makePlayerMortal(player);

            } else {
                player.addTag(COMMAND_TAG);
                godPlayers.add(player.getDisplayName().getString());
                makePlayerGod(player);
            }
        }

        if (godPlayers.size() != 0) {
            commandSourceStack.sendSuccess(MutableComponent.create(new TranslatableContents("Turned " + String.join(", ", godPlayers) + " to Gods.")), true);
        }
        if (noGodPlayers.size() != 0) {
            commandSourceStack.sendSuccess(MutableComponent.create(new TranslatableContents("Turned " + String.join(", ", noGodPlayers) + " to Mortals.")), true);
        }


        return 0;
    }

    private int executeWithArgument(CommandSourceStack commandSourceStack, EntitySelector entitySelector) throws CommandSyntaxException {
        List<ServerPlayer> serverPlayers = entitySelector.findPlayers(commandSourceStack);
        if (serverPlayers.size() == 0) {
            throw CommandSourceStack.ERROR_NOT_PLAYER.create();
        }
        ServerPlayer[] serverPlayerArray = new ServerPlayer[serverPlayers.size()];
        serverPlayers.toArray(serverPlayerArray);
        return this.execute(commandSourceStack, serverPlayerArray);
    }

    private int executeWithArgument(CommandSourceStack commandSourceStack, EntitySelector entitySelector, boolean activateGod) throws CommandSyntaxException {
        List<ServerPlayer> serverPlayers = entitySelector.findPlayers(commandSourceStack);
        if (serverPlayers.size() == 0) {
            throw CommandSourceStack.ERROR_NOT_PLAYER.create();
        }
        ServerPlayer[] serverPlayerArray = new ServerPlayer[serverPlayers.size()];
        serverPlayers.toArray(serverPlayerArray);
        return this.execute(commandSourceStack, serverPlayerArray, activateGod);
    }

    @Override
    public boolean isActive() {
        return UtilityManiaConfig.CONFIGS.isGodCommandEnabled.getConfigValue().get();
    }

    @Override
    protected boolean requiresAdmin() {
        return true;
    }
}
