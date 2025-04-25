package com.txttext.taczlabs.config.fileconfig;

import net.minecraftforge.common.ForgeConfigSpec;

public class FunctionConfig {
    public static ForgeConfigSpec.BooleanValue ENABLE_SPRINTING_SHOOT;//启用跑射

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("FUNCTION");
        ENABLE_SPRINTING_SHOOT = builder
                .comment("Trying shoot during sprinting will stop sprint and shoot instead of no effect.")
                .define("FIX Sprinting Shoot", true);
        builder.pop();
    }
}
