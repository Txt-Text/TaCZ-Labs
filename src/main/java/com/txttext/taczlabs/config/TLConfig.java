package com.txttext.taczlabs.config;

import net.minecraftforge.common.ForgeConfigSpec;
/*
    这里是初始化所有配置类的地方
*/
public class TLConfig {
    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        TLCommonConfig.init(builder);//主要配置在这里，如果东西不多就写在一个类里了，要加分类的时候再改名
        return builder.build();
    }
}

