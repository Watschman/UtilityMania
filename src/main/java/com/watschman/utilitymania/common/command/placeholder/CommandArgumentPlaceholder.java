package com.watschman.utilitymania.common.command.placeholder;

public abstract class CommandArgumentPlaceholder implements ICommandArgumentPlaceholder {
    public CommandArgumentPlaceholder() {
        PLACEHOLDERS.add(this);
    }

    protected abstract String placeHolderText();

    @Override
    public int match(String text) {
        if (text.equals(ICommandArgumentPlaceholder.placeHolderWithDelimiter(placeHolderText()))) {
            return 0;
        }
        if (text.matches("#" + placeHolderText() + "_[0-9]+" + "#")) {
            String numberString = text.split("#")[1].replace(placeHolderText() + "_", "");
            return Integer.parseInt(numberString);
        }
        return -1;
    }
}
