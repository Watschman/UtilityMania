package com.watschman.utilitymania.common.config.commands;

public class DoesMinifyRequireAdminPrivilegesConfigEntry extends CommandConfigEntry<Boolean> {
    @Override
    protected String getLabel() {
        return "MinifyRequiresAdmin";
    }

    @Override
    protected Boolean getDefault() {
        return false;
    }
}
