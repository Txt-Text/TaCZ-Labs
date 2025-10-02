package com.txttext.taczlabs;

import com.txttext.taczlabs.config.ClientConfig;
import com.txttext.taczlabs.config.clothconfig.ClothConfig;
import com.txttext.taczlabs.config.ServerConfig;
import com.txttext.taczlabs.config.CommonConfig;
import com.txttext.taczlabs.event.shoot.PlayerFireHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// 这里的值应与 META-INF/mods.toml 文件中的条目相匹配
@Mod(TaCZLabs.MODID)
public class TaCZLabs {
    public static final String MODID = "taczlabs";

    @SuppressWarnings("all")//弃用警告你够了，用新写法旧版forge会崩溃
    public TaCZLabs(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);//为模组加载注册 commonSetup 方法
        MinecraftForge.EVENT_BUS.register(this);//注册server与其他要监听的游戏事件
        //MinecraftForge.EVENT_BUS.register(new CrosshairRegister());

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.init());
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.init());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.init());
        ClothConfig.register();
//        PlayerFireHandler.register();//注册监听的开火事件
    }
/*    public TaczlabsMain(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);//为模组加载注册 commonSetup 方法
        MinecraftForge.EVENT_BUS.register(this);//注册server与其他要监听的游戏事件
//        BLOCKS.register(modEventBus);//将延迟寄存器(Deferred Register)注册到模组事件总线上，以便方块得到注册
//        ITEMS.register(modEventBus);//物品
//        CREATIVE_MODE_TABS.register(modEventBus);//创造模式标签页
//        modEventBus.addListener(this::addCreative);//将物品注册到创造模式标签页

        //注册ForgeConfigSpec，以便 Forge 能创建并加载配置文件
//        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
//        context.registerConfig(ModConfig.Type.COMMON, TLConfig.init());//注册配置文件
    }*/

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    //可以使用 SubscribeEvent，让事件总线发现要调用的方法
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // 在服务端启动时写点东西
        //LOGGER.info("HELLO from server starting");
    }

    //可以使用 EventBusSubscriber 自动注册类中注解为 @SubscribeEvent 的所有静态方法
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            PlayerFireHandler.register();//注册监听的开火事件
            //LOGGER.info("HELLO FROM CLIENT SETUP");
            //LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
