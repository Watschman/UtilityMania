package com.watschman.utilitymania.common.config;

import net.minecraftforge.common.ForgeConfigSpec;


public class UtilityManiaConfig {
    private static final ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
    public static Config CONFIG = new Config(configBuilder);
    public static final ForgeConfigSpec CONFIG_SPEC = configBuilder.build();

    public static class Config {

        public final ForgeConfigSpec.BooleanValue isGodModeEnabled;

        public Config(ForgeConfigSpec.Builder builder) {
            String section = "Commands";
            builder.push(section);
            isGodModeEnabled = builder.define("isGodModeEnabled", true);

        }
    }
}
