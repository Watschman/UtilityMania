package com.watschman.utilitymania.common.config;

import com.watschman.utilitymania.common.config.commands.DoesMinifyRequireAdminPrivilegesConfigEntry;
import com.watschman.utilitymania.common.config.commands.IsGodCommandEnabledConfigEntry;
import com.watschman.utilitymania.common.config.commands.IsMinifyCommandEnabledConfigEntry;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;


public class UtilityManiaConfig {
    public static List<IConfigEntry> configList = new ArrayList<>();

    public static ConfigEntries CONFIGS = new ConfigEntries();

    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        for (IConfigEntry configEntry : configList) {
            configEntry.buildConfig(configBuilder);
        }
        return configBuilder.build();
    }

    public static class ConfigEntries {
        public IsGodCommandEnabledConfigEntry isGodCommandEnabled;
        public IsMinifyCommandEnabledConfigEntry isMinifyCommandEnabled;

        public DoesMinifyRequireAdminPrivilegesConfigEntry doesMinifyRequireAdminPrivileges;

        public ConfigEntries() {
            this.isGodCommandEnabled = new IsGodCommandEnabledConfigEntry();
            this.isMinifyCommandEnabled = new IsMinifyCommandEnabledConfigEntry();
            this.doesMinifyRequireAdminPrivileges = new DoesMinifyRequireAdminPrivilegesConfigEntry();
        }
    }
}
