package com.watschman.utilitymania.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public interface ICommand {
    void registerCommand(
            CommandDispatcher<CommandSourceStack> commandDispatcher,
            Commands.CommandSelection selection
    );

    boolean isActive();
}
