package com.watschman.utilitymania.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.ArrayList;
import java.util.List;

public class UtilityManiaCommands {
    public static List<ICommand> commandList = new ArrayList<>();

    public static CommandEntries COMMANDS = new CommandEntries();

    public void registerCommands(
            CommandDispatcher<CommandSourceStack> commandDispatcher,
            Commands.CommandSelection selection
    ) {
        for (ICommand command : commandList) {
            if (!command.isActive()) {
                continue;
            }
            command.registerCommand(commandDispatcher, selection);
        }
    }

    public static class CommandEntries {

        public GodCommand godCommand;

        public CommandEntries() {
            godCommand = new GodCommand();
        }
    }
}
