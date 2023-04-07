package com.watschman.utilitymania.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.watschman.utilitymania.common.config.UtilityManiaConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class GodCommand extends BaseCommand {
    private final String commandLiteral = "god";

    @Override
    public void registerCommand(CommandDispatcher<CommandSourceStack> commandDispatcher, Commands.CommandSelection selection) {
        commandDispatcher.register(
                Commands.literal(this.commandLiteral)
                        .requires(source -> source.hasPermission(Commands.LEVEL_ADMINS))
                        .executes(context -> executeSelf(context.getSource()))
                        .then(
                                player()
                                        .requires(source -> source.hasPermission(Commands.LEVEL_ADMINS))
                                        .executes(context -> executeWithArgument(context.getSource(), context.getArgument("player", EntitySelector.class)))
                        )
        );

    }

    protected int executeSelf(CommandSourceStack commandSourceStack) throws CommandSyntaxException {
        return this.execute(commandSourceStack, new ServerPlayer[]{commandSourceStack.getPlayerOrException()});
    }

    protected RequiredArgumentBuilder<CommandSourceStack, EntitySelector> player() {
        return Commands.argument("player", EntityArgument.players());
    }
    protected void makePlayerGod(ServerPlayer player) {
        player.getAbilities().flying = true;
        player.getAbilities().mayfly = true;
        player.getAbilities().invulnerable = true;
        player.onUpdateAbilities();
    }

    protected void makePlayerMortal(ServerPlayer player) {
        player.getAbilities().flying = false;
        player.getAbilities().mayfly = false;
        player.getAbilities().invulnerable = false;
        player.onUpdateAbilities();
    }

    protected int execute(CommandSourceStack commandSourceStack, ServerPlayer[] players) {
        List<String> godPlayers = new ArrayList<>();
        List<String> noGodPlayers = new ArrayList<>();
        for(ServerPlayer player : players) {
            if(!player.gameMode.isSurvival()) {
                continue;
            }
            if(player.getTags().contains(this.commandLiteral)) {
                noGodPlayers.add(player.getDisplayName().getString());
                player.removeTag(this.commandLiteral);
                this.makePlayerMortal(player);

            } else {
                player.addTag(this.commandLiteral);
                godPlayers.add(player.getDisplayName().getString());
                this.makePlayerGod(player);
            }
        }

        if(godPlayers.size() != 0) {
            commandSourceStack.sendSuccess(MutableComponent.create(new TranslatableContents("Turned " + String.join(", ", godPlayers) + " to Gods.")), true);
        }
        if(noGodPlayers.size() != 0) {
            commandSourceStack.sendSuccess(MutableComponent.create(new TranslatableContents("Turned " + String.join(", ", noGodPlayers) + " to Mortals.")), true);
        }




        return 0;
    }

    protected int executeWithArgument(CommandSourceStack commandSourceStack, EntitySelector entitySelector) throws CommandSyntaxException {
        List<ServerPlayer> serverPlayers = entitySelector.findPlayers(commandSourceStack);
        if(serverPlayers.size() == 0) {
            throw CommandSourceStack.ERROR_NOT_PLAYER.create();
        }
        ServerPlayer[] serverPlayerArray = new ServerPlayer[serverPlayers.size()];
        serverPlayers.toArray(serverPlayerArray);
        return this.execute(commandSourceStack, serverPlayerArray);
    }

    @Override
    public boolean isActive() {
        return UtilityManiaConfig.CONFIGS.isGodCommandEnabled.getConfigValue().get();
    }
}
