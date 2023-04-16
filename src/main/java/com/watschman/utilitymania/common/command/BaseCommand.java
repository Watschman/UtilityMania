package com.watschman.utilitymania.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.watschman.utilitymania.common.command.placeholder.ICommandArgumentPlaceholder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseCommand implements ICommand {
    public BaseCommand() {
        COMMAND_LIST.add(this);
    }

    protected boolean requiresAdmin() {
        return false;
    }

    protected abstract List<String> getCommandPaths();

    @Override
    public final void registerCommand(CommandDispatcher<CommandSourceStack> commandDispatcher, Commands.CommandSelection selection) {
        for (String commandString : getCommandPaths()) {
            List<String> commandParts = new ArrayList<>(Arrays.asList(commandString.split(" ")));
            if (commandParts.size() == 0) {
                continue;
            }
            String startPart = commandParts.remove(0);
            if (!(this.resolveCommandPart(startPart) instanceof LiteralArgumentBuilder<?>)) {
                continue;
            }
            LiteralArgumentBuilder<CommandSourceStack> baseBuilder = Commands.literal(startPart);
            List<ArgumentBuilder<CommandSourceStack, ?>> builders = new ArrayList<>();

            for (String commandPart : commandParts) {
                builders.add(this.resolveCommandPart(commandPart));
            }
            ArgumentBuilder<CommandSourceStack, ?> lastBuilder = baseBuilder;
            if (builders.size() > 0) {
                lastBuilder = builders.get(builders.size() - 1);
            }
            if (this.requiresAdmin()) {
                lastBuilder.requires(commandSourceStack -> commandSourceStack.hasPermission(Commands.LEVEL_ADMINS));
            }
            lastBuilder.executes(context -> executeCommand(commandString, context));
            if (builders.size() > 0) {
                builders.remove(builders.size() - 1);
                builders.add(lastBuilder);
            }

            commandDispatcher.register(getBuilder(baseBuilder, builders));
        }
    }

    private LiteralArgumentBuilder<CommandSourceStack> getBuilder(LiteralArgumentBuilder<CommandSourceStack> builder, List<ArgumentBuilder<CommandSourceStack, ?>> builders) {
        if (builders.size() == 0) {
            return builder;
        }
        return builder.then(getBuilder(builders.remove(0), builders));
    }

    private ArgumentBuilder<CommandSourceStack, ?> getBuilder(ArgumentBuilder<CommandSourceStack, ?> builder, List<ArgumentBuilder<CommandSourceStack, ?>> builders) {
        if (builders.size() == 0) {
            return builder;
        }
        return builder.then(getBuilder(builders.remove(0), builders));
    }

    protected abstract int executeCommand(String command, CommandContext<CommandSourceStack> context) throws CommandSyntaxException;

    protected ArgumentBuilder<CommandSourceStack, ?> resolveCommandPart(String text) {
        for (ICommandArgumentPlaceholder placeholder : ICommandArgumentPlaceholder.PLACEHOLDERS) {
            int match = placeholder.match(text);
            if (match != -1) {
                return placeholder.argumentBuilder(match);
            }
        }
        return Commands.literal(text);
    }
}
