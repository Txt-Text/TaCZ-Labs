package com.txttext.taczlabs.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class GuiConfig {
    public static ForgeConfigSpec.BooleanValue ENABLE_TL_CROSSHAIR;
    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("gui");

        builder.comment("New Crosshair of TaCZ:Labs");//或许是输出到文件的#注释？
        ENABLE_TL_CROSSHAIR = builder.define("EnableTLCrosshair", true);

        builder.pop();
    }
}
