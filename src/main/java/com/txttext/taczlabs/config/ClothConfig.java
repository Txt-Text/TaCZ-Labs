package com.txttext.taczlabs.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

import javax.annotation.Nullable;

public class ClothConfig/* extends Screen*/{
//    private final Screen parent;
//    protected ClothConfig(Component component, Screen parent) {
//        super(component);
//        this.parent = parent;
//    }

//    @Override
//    public void init(){
//        super.init();
//        ConfigBuilder builder = ConfigBuilder.create()
//                .setParentScreen(parent)
//                .setTitle(Component.translatable("config.taczlabs.title"))
//                .setSavingRunnable(() -> {
//                    ClientConfig.init().save();//保存配置
//        });
//        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
//
//        HudClothConfig.init(builder, entryBuilder);
//
//        this.addRenderableWidget(builder.build());
//    }
    public static Screen createConfigScreen(Screen parent){
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.taczlabs.title"))
                .setSavingRunnable(() -> {
                    ClientConfig.init().save();//保存配置
        });
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        HudClothConfig.init(builder, entryBuilder);

        return builder.build();
    }

    public static void register(){
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> createConfigScreen(parent)
                )
        );
    }

//    public static ConfigBuilder configBuilder(Screen parent){
//        ConfigBuilder builder = ConfigBuilder.create()
//                .setParentScreen(parent)
//                .setTitle(Component.translatable("config.taczlabs.title"))
//                .setSavingRunnable(() -> {
//                    //保存配置
//        });
//        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
//
//        HudClothConfig.init(builder, entryBuilder);
//
//        return builder;
//    }


//    public static void registerModsPage() {
//        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
//                new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> configBuilder(parent).getParentScreen()));
//    }

//    public static Screen getConfigScreen(@Nullable Screen parent) {
//        return ClothConfig.configBuilder().setParentScreen(parent).build();
//    }
}
