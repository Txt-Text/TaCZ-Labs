//package com.txttext.taczlabs.event.shoot;
//
//import com.tacz.guns.api.item.IGun;
//import com.tacz.guns.client.input.ShootKey;
//import com.tacz.guns.util.InputExtraCheck;
//import com.txttext.taczlabs.config.fileconfig.FunctionConfig;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.player.LocalPlayer;
//import net.minecraft.world.item.Item;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.client.event.InputEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import org.lwjgl.glfw.GLFW;
//
//@OnlyIn(Dist.CLIENT)
//@Mod.EventBusSubscriber(value = Dist.CLIENT)
//public class ShootKeyHandler {
//    public static boolean isMouseDown = false;
//
//    @SubscribeEvent
//    public static void semiShoot(InputEvent.MouseButton.Post event){
//        if(!FunctionConfig.ENABLE_SPRINTING_SHOOT.get()) return;
//        if (InputExtraCheck.isInGame()) {
//            LocalPlayer player = Minecraft.getInstance().player;
//            if(player == null) return;
//            Item mainHand = player.getMainHandItem().getItem();
//            if(mainHand instanceof IGun && ShootKey.SHOOT_KEY.matchesMouse(event.getButton())){
//                isMouseDown = event.getAction() == GLFW.GLFW_PRESS;
//                //System.out.println("当前鼠标按下状态：" + isMouseDown);
//            }
//        }
//    }
//}
