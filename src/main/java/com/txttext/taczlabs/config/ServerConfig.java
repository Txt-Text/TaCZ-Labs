package com.txttext.taczlabs.config;

import com.txttext.taczlabs.config.fileconfig.FunctionConfig;
import net.minecraftforge.common.ForgeConfigSpec;

/*
    这里是初始化所有服务端配置的地方
*/
public class ServerConfig {
    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder server = new ForgeConfigSpec.Builder();

        return server.build();
    }
}
