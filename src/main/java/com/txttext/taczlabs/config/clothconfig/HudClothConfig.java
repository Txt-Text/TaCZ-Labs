package com.txttext.taczlabs.config.clothconfig;

import com.txttext.taczlabs.config.fileconfig.HudConfig;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class HudClothConfig {
    @SuppressWarnings({"unchecked", "rawtypes"})
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

        //全局设置
        /*ARGB滑块*/
        hud.addEntry(entryBuilder.startIntSlider(Component.translatable("config.taczlabs.hud.crosshair_alpha"), HudConfig.A.get(), 0, 255)
                .setSaveConsumer(val -> {
                    HudConfig.A.set(val);
                    updateColorFromARGB();
                }).build());
        hud.addEntry(entryBuilder.startIntSlider(Component.translatable("config.taczlabs.hud.crosshair_red"), HudConfig.R.get(), 0, 255)
                .setSaveConsumer(val -> {
                    HudConfig.R.set(val);
                    updateColorFromARGB();
                }).build());
        hud.addEntry(entryBuilder.startIntSlider(Component.translatable("config.taczlabs.hud.crosshair_green"), HudConfig.G.get(), 0, 255)
                .setSaveConsumer(val -> {
                    HudConfig.G.set(val);
                    updateColorFromARGB();
                }).build());
        hud.addEntry(entryBuilder.startIntSlider(Component.translatable("config.taczlabs.hud.crosshair_blue"), HudConfig.B.get(), 0, 255)
                .setSaveConsumer(val -> {
                    HudConfig.B.set(val);
                    updateColorFromARGB();
                }).build());

        //准星类型（配置不同枪械使用的准星）
        //List<AbstractConfigListEntry<?>> colorOptions = new ArrayList<>();
        List<AbstractConfigListEntry<?>> crosshairType = new ArrayList<>();
        //手枪准星
        crosshairType.add(entryBuilder.startEnumSelector(
                                Component.translatable("config.taczlabs.hud.gun.crosshair.pistol"),
                                CrosshairType.class,
                                HudConfig.pistolCrosshair.get()
                        )
                        .setDefaultValue(HudConfig.pistolCrosshair.getDefault())//获取HudConfig的默认值（TL_CROSSHAIR）
                        .setEnumNameProvider(value -> Component.translatable("config.taczlabs.hud.crosshair." + value.name().toLowerCase()))
                        .setSaveConsumer(HudConfig.pistolCrosshair::set)
                        .build()
        );
        //冲锋枪准星
        crosshairType.add(entryBuilder.startEnumSelector(
                                Component.translatable("config.taczlabs.hud.gun.crosshair.smg"),
                                CrosshairType.class,
                                HudConfig.smgCrosshair.get()
                        )
                        .setDefaultValue(HudConfig.smgCrosshair.getDefault())//获取HudConfig的默认值（TL_CROSSHAIR）
                        .setEnumNameProvider(value -> Component.translatable("config.taczlabs.hud.crosshair." + value.name().toLowerCase()))
                        .setSaveConsumer(HudConfig.smgCrosshair::set)
                        .build()
        );
        //步枪准星
        crosshairType.add(entryBuilder.startEnumSelector(
                                Component.translatable("config.taczlabs.hud.gun.crosshair.rifle"),
                                CrosshairType.class,
                                HudConfig.rifleCrosshair.get()
                        )
                        .setDefaultValue(HudConfig.rifleCrosshair.getDefault())
                        .setEnumNameProvider(value -> Component.translatable("config.taczlabs.hud.crosshair." + value.name().toLowerCase()))
                        .setSaveConsumer(HudConfig.rifleCrosshair::set)
                        .build()
        );
        //机枪准星
        crosshairType.add(entryBuilder.startEnumSelector(
                                Component.translatable("config.taczlabs.hud.gun.crosshair.mg"),
                                CrosshairType.class,
                                HudConfig.mgCrosshair.get()
                        )
                        .setDefaultValue(HudConfig.mgCrosshair.getDefault())
                        .setEnumNameProvider(value -> Component.translatable("config.taczlabs.hud.crosshair." + value.name().toLowerCase()))
                        .setSaveConsumer(HudConfig.mgCrosshair::set)
                        .build()
        );
        //狙击枪准星
        crosshairType.add(entryBuilder.startEnumSelector(
                                Component.translatable("config.taczlabs.hud.gun.crosshair.sniper"),
                                CrosshairType.class,
                                HudConfig.sniperCrosshair.get()
                        )
                        .setDefaultValue(HudConfig.sniperCrosshair.getDefault())
                        .setEnumNameProvider(value -> Component.translatable("config.taczlabs.hud.crosshair." + value.name().toLowerCase()))
                        .setSaveConsumer(HudConfig.sniperCrosshair::set)
                        .build()
        );
        //霰弹枪准星
        crosshairType.add(entryBuilder.startEnumSelector(
                                Component.translatable("config.taczlabs.hud.gun.crosshair.shotgun"),
                                CrosshairType.class,
                                HudConfig.shotgunCrosshair.get()
                        )
                        .setDefaultValue(HudConfig.shotgunCrosshair.getDefault())
                        .setEnumNameProvider(value -> Component.translatable("config.taczlabs.hud.crosshair." + value.name().toLowerCase()))
                        .setSaveConsumer(HudConfig.shotgunCrosshair::set)
                        .build()
        );
        //重武器准星
        crosshairType.add(entryBuilder.startEnumSelector(
                                Component.translatable("config.taczlabs.hud.gun.crosshair.rpg"),
                                CrosshairType.class,
                                HudConfig.rpgCrosshair.get()
                        )
                        .setDefaultValue(HudConfig.rpgCrosshair.getDefault())
                        .setEnumNameProvider(value -> Component.translatable("config.taczlabs.hud.crosshair." + value.name().toLowerCase()))
                        .setSaveConsumer(HudConfig.rpgCrosshair::set)
                        .build()
        );
        //注册折叠选项
        hud.addEntry(entryBuilder.startSubCategory(Component.translatable("config.taczlabs.hud.crosshair.crosshair_type"),
                        (List<AbstractConfigListEntry>)(List<?>)crosshairType
                )
                .setTooltip(Component.translatable("config.taczlabs.hud.crosshair.crosshair_type.desc"))
                .setExpanded(true)//默认展开
                .build());
        //局部设置
        hud.addEntry(entryBuilder.startSubCategory(Component.translatable("config.taczlabs.hud.crosshair.crosshair")

        ).setExpanded(true).build());
        hud.addEntry(entryBuilder.startSubCategory(Component.translatable("config.taczlabs.hud.crosshair.right_angle")

        ).setExpanded(true).build());
        hud.addEntry(entryBuilder.startSubCategory(Component.translatable("config.taczlabs.hud.crosshair.rect")

        ).setExpanded(true).build());
    }

    //从 ARGB 更新 color 整体
    private static void updateColorFromARGB() {
        int argb = (HudConfig.A.get() & 0xFF) << 24 |
                (HudConfig.R.get() & 0xFF) << 16 |
                (HudConfig.G.get() & 0xFF) << 8  |
                (HudConfig.B.get() & 0xFF);
        HudConfig.color.set(argb);
        /*int a = HudConfig.A.get();
        int r = HudConfig.R.get();
        int g = HudConfig.G.get();
        int b = HudConfig.B.get();
        int color = (a << 24) | (r << 16) | (g << 8) | b;
        HudConfig.color.set(color);*/
    }
}

