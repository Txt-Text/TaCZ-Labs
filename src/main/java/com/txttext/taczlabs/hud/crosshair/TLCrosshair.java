package com.txttext.taczlabs.hud.crosshair;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tacz.guns.api.item.IGun;
import com.txttext.taczlabs.TaCZLabs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class TLCrosshair implements IGuiOverlay {
    public static final TLCrosshair INSTANCE = new TLCrosshair();
    //public final ResourceLocation HUDLoc = new ResourceLocation(TaCZLabs.MODID, "textures/hud/crosshair/hud.png");

    public TLCrosshair(){

    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight){
        Minecraft minecraft = Minecraft.getInstance();
        //检查玩家主手是否为tacz的枪，如果不是直接返回
        if (minecraft.player != null && !(minecraft.player.getMainHandItem().getItem() instanceof IGun)) {
            return;
        }
        guiGraphics.setColor(1,1,1,1);
        //在屏幕中央上方绘制 HUD 纹理，宽高为 32x32 像素
        //参数是含义，x坐标，y坐标，u，v坐标，宽高，uv宽高。
        //guiGraphics.blit(HUDLoc,screenWidth/2-16,screenHeight/2-64,0,0,32,32,32,32);
    }
    //提供一个静态方法用于获取 ExampleHud 的单例对象
    public static TLCrosshair getInstance() {
        return INSTANCE;
    }

//        System.out.println("Rendering crosshair...");
//        if(minecraft.options.hideGui || !minecraft.options.getCameraType().isFirstPerson()) {
//            return;
//        }
//
//        PoseStack poseStack = guiGraphics.pose();
//        /*获取屏幕中心点*/
//        int centerX = screenWidth / 2;
//        int centerY = screenHeight / 2;
//
//        guiGraphics.fill(centerX - 5, centerY - 5, centerX + 5, centerY + 5, 0x66FF0000);
//
//        //动态参数（示例：旋转角度）
//        float time = (minecraft.player.tickCount + partialTick) * 0.05f; // 0-1循环
//        float angle = (time % 1.0f) * 360.0f;// 完整旋转
//
//        //绘制动态准星
//        poseStack.pushPose();
//        poseStack.translate(centerX, centerY, 0);
//        poseStack.mulPose(Axis.ZP.rotationDegrees(angle));
//        //绘制十字线（自定义形状）
//        int size = 8;
//        int thickness = 2;
//        guiGraphics.fill(-size, -thickness/2, size, thickness/2, 0xFFFF0000); //水平红线
//        guiGraphics.fill(-thickness/2, -size, thickness/2, size, 0xFF00FF00); // 垂直绿线
//
//        poseStack.popPose();
}