package com.txttext.taczlabs.config;

import net.minecraftforge.common.ForgeConfigSpec;
/*
    这里是初始化所有配置类的地方
*/
public class Config {
    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        GuiConfig.init(builder);//gui相关的config
        return builder.build();
    }
}

