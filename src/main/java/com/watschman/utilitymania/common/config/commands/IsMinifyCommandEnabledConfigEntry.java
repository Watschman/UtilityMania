package com.watschman.utilitymania.common.config.commands;

public class IsMinifyCommandEnabledConfigEntry extends CommandConfigEntry<Boolean> {
    @Override
    protected String getLabel() {
        return "EnableMinifyCommand";
    }

    @Override
    protected Boolean getDefault() {
        return true;
    }
}
