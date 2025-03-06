package com.txttext.taczlabs.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class ClothConfig {
    public static ConfigBuilder configBuilder(){
        ConfigBuilder builder = ConfigBuilder.create()
                //.setParentScreen(parent)
                .setTitle(Component.translatable("config.taczlabs.title"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        GuiClothConfig.init(builder, entryBuilder);

        builder.setSavingRunnable(() -> {
            //保存配置
        });

        return builder;
    }

    //public static void
    /*
    * public static void registerModsPage() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> getConfigScreen(parent)));
    }

    public static Screen getConfigScreen(@Nullable Screen parent) {
        return MenuIntegration.getConfigBuilder().setParentScreen(parent).build();
    }
    * */
}
