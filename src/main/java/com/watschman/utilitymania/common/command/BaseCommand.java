package com.watschman.utilitymania.common.command;

public abstract class BaseCommand implements ICommand {
    public BaseCommand() {
        UtilityManiaCommands.commandList.add(this);
    }
}
