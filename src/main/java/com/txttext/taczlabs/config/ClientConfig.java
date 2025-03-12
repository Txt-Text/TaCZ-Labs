package com.txttext.taczlabs.config;

import net.minecraftforge.common.ForgeConfigSpec;
/*
    这里是初始化所有客户端配置的地方
*/
public class ClientConfig {
    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder client = new ForgeConfigSpec.Builder();
        HudConfig.init(client);//gui相关的config
        return client.build();
    }
}

