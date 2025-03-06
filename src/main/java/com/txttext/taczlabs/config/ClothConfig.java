package com.txttext.taczlabs.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class ClothConfig {
    public static ConfigBuilder configBuilder(){
        ConfigBuilder builder = ConfigBuilder.create()
                //.setParentScreen(parent)
                .setTitle(Component.translatable("taczlabs.config.title"));
        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("taczlabs.config.title"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("taczlabs.config.option.enableTLCrosshair"), GuiConfig.ENABLE_TL_CROSSHAIR.get())
                .setDefaultValue(true)
                .setTooltip(Component.translatable("taczlabs.config.tip.enable_tl_crosshair"))
                .setSaveConsumer(GuiConfig.ENABLE_TL_CROSSHAIR::set) // Recommended: Called when user save the config
                .build());

        builder.setSavingRunnable(() -> {
            //保存配置
        });

        return builder;
    }
}
