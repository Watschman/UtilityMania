package com.watschman.utilitymania.common.command.placeholder;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

public class PlayerPlaceholder extends CommandArgumentPlaceholder {

    public static final String PLACE_HOLDER_TEXT = "PLAYER";

    public static final String ARGUMENT_NAME = "player";

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> argumentBuilder(int argumentPosition) {
        return Commands.argument(ARGUMENT_NAME + (argumentPosition <= 0 ? "" : argumentPosition), EntityArgument.players());

    }

    @Override
    protected String placeHolderText() {
        return PLACE_HOLDER_TEXT;
    }
}
