package com.txttext.taczlabs.config.clothconfig;

import com.txttext.taczlabs.config.fileconfig.FunctionConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class FunctionClothConfig {
    public static void init(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory function = builder.getOrCreateCategory(Component.translatable("config.taczlabs.function"));

        /*启用跑射*/
        function.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.taczlabs.func.enable_sprinting_shoot"),
                                FunctionConfig.ENABLE_SPRINTING_SHOOT.get()
                        )
                        .setDefaultValue(true)
                        .setTooltip(Component.translatable("config.taczlabs.func.enable_sprinting_shoot.desc"))
                        .setSaveConsumer(FunctionConfig.ENABLE_SPRINTING_SHOOT::set)//Recommended: Called when user save the config
                        .build()
        );
    }
}
