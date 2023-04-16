package com.watschman.utilitymania.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.watschman.utilitymania.common.command.placeholder.PlayerPlaceholder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class UtilityManiaCommands {

    public static CommandEntries COMMANDS = new CommandEntries();

    public void registerCommands(
            CommandDispatcher<CommandSourceStack> commandDispatcher,
            Commands.CommandSelection selection
    ) {
        for (ICommand command : ICommand.COMMAND_LIST) {
            if (!command.isActive()) {
                continue;
            }
            command.registerCommand(commandDispatcher, selection);
        }
    }

    public void initArgumentPlaceHolders() {
        new PlayerPlaceholder();
    }

    public static class CommandEntries {

        public GodCommand godCommand;
        public MinifyCommand minifyCommand;

        public CommandEntries() {
            godCommand = new GodCommand();
            minifyCommand = new MinifyCommand();
        }
    }
}
