package com.txttext.taczlabs.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class ClothConfig {
    public static ConfigBuilder configBuilder(){
        ConfigBuilder builder = ConfigBuilder.create()
                //.setParentScreen(parent)
                .setTitle(Component.translatable("title.taczlabs.config"));
        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("title.taczlabs.config"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        boolean enableTLCrosshair = true;
        general.addEntry(entryBuilder.startStrField(Component.translatable("option.taczlabs.enableTLCrosshair"), String.valueOf(enableTLCrosshair))
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.tip.enable_tl_crosshair"))
                .setSaveConsumer() // Recommended: Called when user save the config
                .build());

        builder.setSavingRunnable(() -> {
            //保存配置
        });

        return builder;
    }
}
