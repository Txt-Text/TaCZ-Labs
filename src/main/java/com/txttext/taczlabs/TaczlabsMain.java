package com.txttext.taczlabs;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// 这里的值应与 META-INF/mods.toml 文件中的条目相匹配
@Mod(TaczlabsMain.MODID)
public class TaczlabsMain {
    public static final String MODID = "taczlabs";
    private static final Logger LOGGER = LogUtils.getLogger();// 直接引用 slf4j 日志记录器
    /*// 创建一个延迟注册器（Deferred Register），用于保存所有将在 “taczlabs ”命名空间下注册的方块
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);//物品
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);//创造模式标签页
    // 依据命名空间和路径，注册一个 id 为 taczlabs:example_block 的方块
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().alwaysEat().nutrition(1).saturationMod(2f).build())));

    // 注册一个id为 taczlabs:example_tab 的创造模式标签页，置于“战斗”标签页之后。
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder().withTabsBefore(CreativeModeTabs.COMBAT).icon(() -> EXAMPLE_ITEM.get().getDefaultInstance()).displayItems((parameters, output) -> {
        output.accept(EXAMPLE_ITEM.get()); // 将example_item添加到标签页。对于自己的标签页，这种方法优于事件
    }).build());*/
    @SuppressWarnings("all")
    public TaczlabsMain(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);//为模组加载注册 commonSetup 方法
        MinecraftForge.EVENT_BUS.register(this);//注册server与其他要监听的游戏事件
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
        //一些常见的设置
        LOGGER.info("HELLO FROM COMMON SETUP");//Minecraft Development你补药在日志里乱拉屎啊（恼）
        //LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
//        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
//        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
//        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    //在构造的方块标签页中添加示例方块物品
//    private void addCreative(BuildCreativeModeTabContentsEvent event) {
//        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) event.accept(EXAMPLE_BLOCK_ITEM);
//    }

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
            // 在客户端启动时写点东西
            //LOGGER.info("HELLO FROM CLIENT SETUP");
            //LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
