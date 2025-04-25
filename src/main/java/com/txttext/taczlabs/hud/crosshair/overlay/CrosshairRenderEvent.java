//package com.txttext.taczlabs.hud.crosshair.overlay;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.txttext.taczlabs.TaCZLabs;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.GameRenderer;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.gui.overlay.IGuiOverlay;
//import net.minecraftforge.fml.common.Mod;
//
//@SuppressWarnings("removal")
//@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TaCZLabs.MODID)
//public class CrosshairRenderEvent {
//    private static final ResourceLocation RIFLE_VERTICAL_CROSSHAIR = new ResourceLocation(TaCZLabs.MODID, "textures/crosshair/crosshair_rifle_vertical.png");
//    private static final ResourceLocation RIFLE_ACROSS_CROSSHAIR = new ResourceLocation(TaCZLabs.MODID, "textures/crosshair/crosshair_rifle_across.png");
//    private static final ResourceLocation RIFLE_MIDDLE_CROSSHAIR = new ResourceLocation(TaCZLabs.MODID, "textures/crosshair/crosshair_rifle_middle.png");
//    private static final long animTime = 500L;
//    private static long animTimestamp = -1L;
//
//    // 定义准星的渲染逻辑
//    public static final IGuiOverlay CROSSHAIR = new IGuiOverlay(
//            new ResourceLocation("your_mod_id", "crosshair"),
//            (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
//                Minecraft mc = Minecraft.getInstance();
//                if (mc.options.hideGui || mc.screen != null) return;
//
//                int centerX = screenWidth / 2;
//                int centerY = screenHeight / 2;
//
//                // 方式1：直接绘制图形（简单十字）
//                RenderSystem.setShader(GameRenderer::getPositionColorShader);
//                RenderSystem.enableBlend();
//                GuiUtils.drawCrosshair(guiGraphics, centerX, centerY, 0xFFFFFFFF);
//                RenderSystem.disableBlend();
//
//                // 方式2：使用纹理（需提供png文件）
//                // RenderSystem.setShaderTexture(0, CROSSHAIR_TEXTURE);
//                // guiGraphics.blit(CROSSHAIR_TEXTURE, centerX - 8, centerY - 8, 0, 0, 16, 16, 16, 16);
//            }
//    );
//
//    public static long getAnimTime() {
//        return animTime;
//    }
//
//    public static long getAnimTimestamp() {
//        return animTimestamp;
//    }
//}
