package com.txttext.taczlabs.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class GuiConfig {
    public static ForgeConfigSpec.BooleanValue ENABLE_TL_CROSSHAIR;
    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("gui");

        builder.comment("New Crosshair of TaCZ:Labs");//搞清楚这是干什么的之后替换成路径
        ENABLE_TL_CROSSHAIR = builder.define("EnableTLCrosshair", true);

        builder.pop();
    }
}
