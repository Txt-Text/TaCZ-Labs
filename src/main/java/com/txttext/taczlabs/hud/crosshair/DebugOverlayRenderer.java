//package com.txttext.taczlabs.hud.crosshair;
//
//import com.tacz.guns.resource.pojo.data.gun.InaccuracyType;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.Font;
//import net.minecraft.client.player.LocalPlayer;
//import net.minecraft.world.entity.player.Player;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.RenderGuiOverlayEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
////调试类，在左上角显示玩家状态
//@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
//public class DebugOverlayRenderer {
//
//    @SubscribeEvent
//    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
//        Minecraft mc = Minecraft.getInstance();
//        if (mc.player == null || mc.options.renderDebug) {
//            return;//玩家不存在或者开了F3就不显示
//        }
//
//        Font font = mc.font;
//
//        //显示的文本
//        String statusText = "玩家状态: " + getPlayerStatus();
//
//        //在左上角绘制白色文字
//        event.getGuiGraphics().drawString(font, statusText, 10, 10, 0xFFFFFFFF, false);
//    }
//
//    private static String getPlayerStatus() {
//        /*Local*/
//        Player player = Minecraft.getInstance().player;
//        InaccuracyType playerStatus = InaccuracyType.getInaccuracyType(player);//获取玩家状态
//        return String.format(playerStatus.name());
//    }
//}
//
