package com.watschman.utilitymania.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.watschman.utilitymania.UtilityMania;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public interface ICommand {
    List<ICommand> COMMAND_LIST = new ArrayList<>();

    SimpleCommandExceptionType ERROR_COMMAND_NOT_IMPLEMENTED = new SimpleCommandExceptionType(Component.translatable(UtilityMania.MOD_ID + ".commands.error.not_implemented"));

    void registerCommand(
            CommandDispatcher<CommandSourceStack> commandDispatcher,
            Commands.CommandSelection selection
    );

    boolean isActive();
}
