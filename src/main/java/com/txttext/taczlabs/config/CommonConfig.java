package com.txttext.taczlabs.config;

import net.minecraftforge.common.ForgeConfigSpec;

/*
    这里是初始化所有COMMON配置的地方
*/
public class CommonConfig {
    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder common = new ForgeConfigSpec.Builder();

        return common.build();
    }
}
