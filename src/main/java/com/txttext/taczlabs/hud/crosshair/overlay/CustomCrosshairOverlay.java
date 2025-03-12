package com.txttext.taczlabs.hud.crosshair.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class CustomCrosshairOverlay implements IGuiOverlay {
    public static final CustomCrosshairOverlay INSTANCE = new CustomCrosshairOverlay();

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight){
        Minecraft minecraft = Minecraft.getInstance();
        System.out.println("Rendering crosshair...");
        if(minecraft.options.hideGui || !minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }

        PoseStack poseStack = guiGraphics.pose();
        /*获取屏幕中心点*/
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        guiGraphics.fill(centerX - 5, centerY - 5, centerX + 5, centerY + 5, 0x66FF0000);

        //动态参数（示例：旋转角度）
        float time = (minecraft.player.tickCount + partialTick) * 0.05f; // 0-1循环
        float angle = (time % 1.0f) * 360.0f;// 完整旋转

        //绘制动态准星
        poseStack.pushPose();
        poseStack.translate(centerX, centerY, 0);
        poseStack.mulPose(Axis.ZP.rotationDegrees(angle));
        //绘制十字线（自定义形状）
        int size = 8;
        int thickness = 2;
        guiGraphics.fill(-size, -thickness/2, size, thickness/2, 0xFFFF0000); //水平红线
        guiGraphics.fill(-thickness/2, -size, thickness/2, size, 0xFF00FF00); // 垂直绿线

        poseStack.popPose();
    }


}
