package com.watschman.utilitymania.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public abstract class BaseConfigEntry<T> implements IConfigValue<T>, IConfigEntry {

    protected ForgeConfigSpec.ConfigValue<T> configValue;
    protected static class Sections {
        public static final String NO_SECTION = null;
        public static final String COMMANDS = "commands";
    }

    protected abstract String getLabel();

    public BaseConfigEntry() {
        UtilityManiaConfig.configList.add(this);
    }
    protected String getSection() {
        return Sections.NO_SECTION;
    }

    protected String getComment() {
        return null;
    }

    @Override
    public ForgeConfigSpec.ConfigValue<T> getConfigValue() {
        return this.configValue;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        String path = this.getLabel();

        if(this.getSection() != null) {
            path = this.getSection() + "." + path;
        }
        if(this.getComment() != null) {
            builder.comment(this.getComment());
        }
        if(this.requiresWorldRestart()) {
            builder.worldRestart();
        }
        this.configValue = builder.define(path, this.getDefault());
    }

    protected abstract T getDefault();

    protected boolean requiresWorldRestart() {
        return true;
    }
}
