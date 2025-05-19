package com.txttext.taczlabs;

import com.txttext.taczlabs.config.ClientConfig;
import com.txttext.taczlabs.config.clothconfig.ClothConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;

// 这里的值应与 META-INF/mods.toml 文件中的条目相匹配
@Mod(TaCZLabs.MODID)
public class TaCZLabs {
    public static final String MODID = "taczlabs";
    //private static final Logger LOGGER = LogUtils.getLogger();// 直接引用 slf4j 日志记录器
    //大部分都是 Minecraft Dev 插件生成的，不要在意这些细节
    //@SuppressWarnings("all")//弃用警告你够了，用新写法旧版forge会崩溃
    public TaCZLabs(){
        //IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        //MinecraftForge.EVENT_BUS.register(this);//注册server与其他要监听的游戏事件
        //MinecraftForge.EVENT_BUS.register(new CrosshairRegister());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.init());
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClothConfig.register();
        }
    }
/*注册ForgeConfigSpec，以便 Forge 能创建并加载配置文件
//        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
//        context.registerConfig(ModConfig.Type.COMMON, TLConfig.init());//注册配置文件
    }*/

    //可以使用 EventBusSubscriber 自动注册类中注解为 @SubscribeEvent 的所有静态方法
//    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//    public static class ClientModEvents {
//        @SubscribeEvent
//        public static void onClientSetup(FMLClientSetupEvent event) {
//            PlayerFireHandler.register();//注册监听的开火事件
//            //LOGGER.info("HELLO FROM CLIENT SETUP");
//            //LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
//        }
//    }
}
