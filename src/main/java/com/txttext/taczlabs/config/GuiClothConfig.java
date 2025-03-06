package com.txttext.taczlabs.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class GuiClothConfig {
    public static void init(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory gui = builder.getOrCreateCategory(Component.translatable("config.taczlabs.gui"));
        /*是否启用TL新准星*/
        gui.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.taczlabs.gui.enable_tl_crosshair"), GuiConfig.ENABLE_TL_CROSSHAIR.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.taczlabs.gui.enable_tl_crosshair.desc"))
                .setSaveConsumer(GuiConfig.ENABLE_TL_CROSSHAIR::set).build()); //Recommended: Called when user save the config

    }
}
