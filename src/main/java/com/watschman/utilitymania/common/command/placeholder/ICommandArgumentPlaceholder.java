package com.watschman.utilitymania.common.command.placeholder;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

import java.util.ArrayList;
import java.util.List;

public interface ICommandArgumentPlaceholder {
    static String placeHolderWithDelimiter(String placeHolderText) {
        return "#" + placeHolderText + "#";
    }

    static String placeHolderWithDelimiter(String placeHolderText, int position) {
        if (position < 0) {
            return placeHolderWithDelimiter(placeHolderText);
        }
        return "#" + placeHolderText + "_" + position + "#";
    }

    List<ICommandArgumentPlaceholder> PLACEHOLDERS = new ArrayList<>();

    ArgumentBuilder<CommandSourceStack, ?> argumentBuilder(int argumentPosition);

    int match(String text);
}
