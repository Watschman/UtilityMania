package com.watschman.utilitymania.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public interface IConfigValue<T> {
    ForgeConfigSpec.ConfigValue<T> getConfigValue();
}
