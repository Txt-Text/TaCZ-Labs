package com.txttext.taczlabs.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class GuiConfig {
    public static ForgeConfigSpec.BooleanValue ENABLE_TL_CROSSHAIR;
    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("gui");

        builder.comment("The default fire sound range (block)");
        ENABLE_TL_CROSSHAIR = builder.define("EnableTLCrosshair", true);


        builder.pop();
    }
}
