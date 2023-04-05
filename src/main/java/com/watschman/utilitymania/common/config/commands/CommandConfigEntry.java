package com.watschman.utilitymania.common.config.commands;

import com.watschman.utilitymania.common.config.BaseConfigEntry;

public abstract class CommandConfigEntry<T> extends BaseConfigEntry<T> {

    @Override
    protected String getSection() {
        return Sections.COMMANDS;
    }
}
