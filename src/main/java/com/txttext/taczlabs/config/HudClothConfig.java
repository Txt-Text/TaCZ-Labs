package com.txttext.taczlabs.config;

import com.txttext.taczlabs.hud.crosshair.SprintingCrosshair;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class HudClothConfig {
    public static void init(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory hud = builder.getOrCreateCategory(Component.translatable("config.taczlabs.hud"));

        /*是否启用TL准星*/
        hud.addEntry(entryBuilder.startBooleanToggle(
                Component.translatable("config.taczlabs.hud.enable_tl_crosshair"),
                        HudConfig.ENABLE_TL_CROSSHAIR.get()
                )
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.taczlabs.hud.enable_tl_crosshair.desc"))
                .setSaveConsumer(HudConfig.ENABLE_TL_CROSSHAIR::set)//Recommended: Called when user save the config
                .build()
        );

        /*疾跑时显示的准星样式*/
        hud.addEntry(entryBuilder.startEnumSelector(
                Component.translatable("config.taczlabs.hud.sprinting_crosshair"),
                        SprintingCrosshair.class,
                        HudConfig.CROSSHAIR_STATUS_DURING_SPRINTING.get()
                )
                .setDefaultValue(HudConfig.CROSSHAIR_STATUS_DURING_SPRINTING.getDefault())//获取HudConfig的默认值（TL_CROSSHAIR）
                .setEnumNameProvider(value ->
                        Component.translatable("enum.taczlabs." + value.name().toLowerCase()))
                .setTooltip(Component.translatable("config.taczlabs.hud.sprinting_crosshair.desc"))
                .setSaveConsumer(HudConfig.CROSSHAIR_STATUS_DURING_SPRINTING::set)
                .build()
        );
    }
}
