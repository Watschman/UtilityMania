package com.watschman.utilitymania.common.config.commands;

public class IsGodCommandEnabledConfigEntry extends CommandConfigEntry<Boolean> {
    @Override
    protected String getLabel() {
        return "EnableGodCommand";
    }
    @Override
    protected Boolean getDefault() {
        return Boolean.TRUE;
    }
}
