package com.txttext.taczlabs.hud.crosshair;

import com.txttext.taczlabs.TaCZLabs;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(/*modid = "taczlabs", */bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HudEventHandler {
    //在 RegisterGuiOverlaysEvent 事件中注册自定义的 HUD 覆盖
    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        // 使用 event.registerAboveAll 方法将 ExampleHud 的实例注册为最高优先级的 HUD 覆盖
        // 第一个参数是一个 ResourceLocation 对象，用于指定 HUD 的唯一标识符
        // 第二个参数是 ExampleHud 的单例对象，通过 ExampleHud.getInstance() 获取
        //event.registerAboveAll(new ResourceLocation(TaCZLabs.MODID,"textures/hud/crosshair/hud.png"), TLCrosshair.getInstance());
    }
    /*@SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        // 使用 event.registerAboveAll 方法将 ExampleHud 的实例注册为最高优先级的 HUD 覆盖
        // 第一个参数是一个 ResourceLocation 对象，用于指定 HUD 的唯一标识符
        // 第二个参数是 ExampleHud 的单例对象，通过 ExampleHud.getInstance() 获取
        event.registerAboveAll(new ResourceLocation(ExampleMod.MODID,"example_hud"), ExampleHud.getInstance());*/
//    public void registerCrosshair(RegisterGuiOverlaysEvent event){
//
//        event.registerAbove(
//                VanillaGuiOverlay.CROSSHAIR.id(),
//                "taczlabs",
//                TLCrosshair.INSTANCE);
//        System.out.println("自定义准星已注册");
////        Minecraft minecraft = Minecraft.getInstance();
////        Window window = minecraft.getWindow();
////        int screenCenterX = (window.getGuiScaledWidth()) / 2;//（屏幕宽）/2
////        int screenCenterY = (window.getGuiScaledHeight()) / 2;//（屏幕高）/2
////        GuiGraphics guiGraphics = event.getGuiGraphics();
////        int size = 10;
////        int thickness = 2;
////
////        long time = System.currentTimeMillis() % 1000;
////        double scaleFactor = 0.5f + 0.2f * Math.sin(time * 0.005); // 缩放波动
////        size = (int) (10 * scaleFactor);
////
////        if(minecraft.player != null){
////            if (minecraft.player.xxa != 0 || minecraft.player.zza != 0) { // 检测移动输入
////                double spread = Math.abs(minecraft.player.getDeltaMovement().length()) * 10;
////                size += (int) spread;
////            }
////        }
////        //水平线
////        guiGraphics.fill(
////                screenCenterY - thickness / 2,
////                screenCenterX - size,
////                screenCenterY + thickness / 2,
////                screenCenterX + size,
////                0xFFFFFFFF
////        );
////
////        //垂直线
////        guiGraphics.fill(
////                screenCenterY - thickness / 2,
////                screenCenterX - size,
////                screenCenterY + thickness / 2,
////                screenCenterX + size,
////                0xFFFFFFFF
////        );
//    }
}
