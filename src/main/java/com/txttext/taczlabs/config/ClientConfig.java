package com.txttext.taczlabs.config;

import com.txttext.taczlabs.config.fileconfig.FunctionConfig;
import com.txttext.taczlabs.config.fileconfig.HudConfig;
import net.minecraftforge.common.ForgeConfigSpec;
/*
    这里是初始化所有客户端配置的地方
*/
public class ClientConfig {
    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder client = new ForgeConfigSpec.Builder();
        HudConfig.init(client);//gui相关的config
        FunctionConfig.init(client);//功能性相关的config
        return client.build();
    }
}

