package com.txttext.taczlabs.hud.crosshair.crosshairs;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.txttext.taczlabs.config.fileconfig.HudConfig;
import com.txttext.taczlabs.hud.crosshair.DotStyle;
import com.txttext.taczlabs.hud.crosshair.Line;
import net.minecraft.client.renderer.GameRenderer;
//import org.joml.Matrix4f;
import java.util.List;
import static com.txttext.taczlabs.config.fileconfig.HudConfig.*;

public abstract class AbstractCrosshair {
    private float minRadius = 1.0f;

    public AbstractCrosshair(float minRadius) {
        this.minRadius = minRadius;
    }

    public AbstractCrosshair() {}

    public abstract void Render(float x, float y, float spread);

    public float getMinRadius() {
        return minRadius;
    }

    /**
     * 利用 OpenGL 绘制方形
     * @param x1 左上角的横坐标
     * @param y1 左上角的纵坐标
     * @param x2 右下角的横坐标
     * @param y2 右下角的纵坐标
     * @param color 十六进制 ARGB，例如 0xE6FFFFFF。使用 ARGB 而不是 RGBA 顺序是因为适应 MC 渲染 API 中的颜色，大多也是 ARGB 排列
     * @apiNote 别忘了 OpenGL 的坐标系是反着来的，需颠倒加减
     * */
    public static void drawRect(float x1, float y1, float x2, float y2, int color) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        List<Float> argb = Argb(color);
        float a = argb.get(0);
        float r = argb.get(1);
        float g = argb.get(2);
        float b = argb.get(3);

        //自动排序坐标
        float left = Math.min(x1, x2);
        float right = Math.max(x1, x2);
        float top = Math.min(y1, y2);
        float bottom = Math.max(y1, y2);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(left, bottom, 0).color(r, g, b, a).endVertex();
        buffer.vertex(right, bottom, 0).color(r, g, b, a).endVertex();
        buffer.vertex(right, top, 0).color(r, g, b, a).endVertex();
        buffer.vertex(left, top, 0).color(r, g, b, a).endVertex();
        tesselator.end();
    }


    /**
     * 绘制具有厚度的圆弧（使用 QUADS 确保 100% 渲染可见，且可控制线宽）
     * * @param cx         圆心 X
     * @param cy         圆心 Y
     * @param radius     外圆半径
     * @param thickness  线条厚度 (向内延伸)
     * @param startAngle 起始角度
     * @param endAngle   结束角度
     * @param segments   分段数 (越大越圆滑，通常 16-32 即可)
     * @param color      ARGB 颜色
     */
    public static void drawArc(float cx, float cy, float radius, float thickness, float startAngle, float endAngle, int segments, int color) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // 确保使用支持顶点颜色插值的着色器
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        List<Float> argb = Argb(color);
        float a = argb.get(0);
        float r = argb.get(1);
        float g = argb.get(2);
        float b = argb.get(3);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        // 羽化宽度，通常 0.5 到 1.0 像素就能达到完美的抗锯齿效果
        float feather = 1.0f;

        float mainOuter = radius;
        float mainInner = radius - thickness;
        float outFringe = mainOuter + feather;
        float inFringe = mainInner - feather;

        for (int i = 0; i < segments; i++) {
            float angle1 = startAngle + (endAngle - startAngle) * i / segments;
            float angle2 = startAngle + (endAngle - startAngle) * (i + 1) / segments;

            double rad1 = Math.toRadians(angle1);
            double rad2 = Math.toRadians(angle2);

            float cos1 = (float) Math.cos(rad1);
            float sin1 = (float) Math.sin(rad1);
            float cos2 = (float) Math.cos(rad2);
            float sin2 = (float) Math.sin(rad2);

            // === 计算四个圈的坐标 ===
            // 角度1的四个点 (外羽化层、外实体层、内实体层、内羽化层)
            float x1OutF = cx + cos1 * outFringe; float y1OutF = cy + sin1 * outFringe;
            float x1MainO = cx + cos1 * mainOuter; float y1MainO = cy + sin1 * mainOuter;
            float x1MainI = cx + cos1 * mainInner; float y1MainI = cy + sin1 * mainInner;
            float x1InF = cx + cos1 * inFringe; float y1InF = cy + sin1 * inFringe;

            // 角度2的四个点
            float x2OutF = cx + cos2 * outFringe; float y2OutF = cy + sin2 * outFringe;
            float x2MainO = cx + cos2 * mainOuter; float y2MainO = cy + sin2 * mainOuter;
            float x2MainI = cx + cos2 * mainInner; float y2MainI = cy + sin2 * mainInner;
            float x2InF = cx + cos2 * inFringe; float y2InF = cy + sin2 * inFringe;

            // === 1. 绘制外层羽化 (Alpha: 0 -> a) ===
            buffer.vertex(x1OutF, y1OutF, 0).color(r, g, b, 0f).endVertex();
            buffer.vertex(x1MainO, y1MainO, 0).color(r, g, b, a).endVertex();
            buffer.vertex(x2MainO, y2MainO, 0).color(r, g, b, a).endVertex();
            buffer.vertex(x2OutF, y2OutF, 0).color(r, g, b, 0f).endVertex();

            // === 2. 绘制核心实体 (Alpha: a -> a) ===
            buffer.vertex(x1MainO, y1MainO, 0).color(r, g, b, a).endVertex();
            buffer.vertex(x1MainI, y1MainI, 0).color(r, g, b, a).endVertex();
            buffer.vertex(x2MainI, y2MainI, 0).color(r, g, b, a).endVertex();
            buffer.vertex(x2MainO, y2MainO, 0).color(r, g, b, a).endVertex();

            // === 3. 绘制内层羽化 (Alpha: a -> 0) ===
            buffer.vertex(x1MainI, y1MainI, 0).color(r, g, b, a).endVertex();
            buffer.vertex(x1InF, y1InF, 0).color(r, g, b, 0f).endVertex();
            buffer.vertex(x2InF, y2InF, 0).color(r, g, b, 0f).endVertex();
            buffer.vertex(x2MainI, y2MainI, 0).color(r, g, b, a).endVertex();
        }

        tesselator.end();
    }

    /**
     * 绘制具有阴影的矩形线条（用于准星）
     * @param lines 准星各部分的 xy 坐标，每个两组
     * @param shadowColor 阴影颜色
     * */
    //统一绘制阴影与准星，弃用依次绘制，防止后渲染的阴影遮挡准星
    //将坐标系封装到 List 结构中，省去冗长的显式传递
    public static void drawLineWithShadow(List<Line> lines, int shadowColor){
        //绘制所有阴影
        for (Line l : lines) {
            drawRect(//沟槽的OpenGL单位好像是0.5像素，传入0.5才是偏移1像素
                    l.x1() + shadowOffset.get() / 2.0f, l.y1() + shadowOffset.get() / 2.0f,
                    l.x2() + shadowOffset.get() / 2.0f, l.y2() + shadowOffset.get() / 2.0f,
                    shadowColor
            );
        }
        //绘制所有准星主体
        for (Line l : lines) {
            drawRect(l.x1(), l.y1(), l.x2(), l.y2(), color.get());
        }
    }
    public static void drawLineWithShadow(List<Line> lines){
        drawLineWithShadow(lines, (shadowAlpha.get() & 0xFF) << 24);//将阴影值转成0xAA000000格式
    }

//    public static void drawArcCrosshair(GuiGraphics graphics, float x, float y, float spread){
//        int color = 0xFFFFFFFF; // 白色
//        PoseStack poseStack = graphics.pose();
//
//        // 开启混合并设置函数，确保带有 Alpha 通道的颜色能正常半透明显示
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//
//        // 关闭深度测试，确保准星永远渲染在最上层，不被别的 UI 遮挡
//        RenderSystem.disableDepthTest();
//
//        // 中心点
//        drawDot(x, y); // 你的点绘制方法也需要注意 Shader 问题
//
//        // 左半圆括号：从180°到360°（注意你的角度逻辑：标准数学坐标中 180->360 是下半圆，如果想画左括号，可能需要 90->270）
//        drawArc(poseStack, x - 6 - spread, y, 5, 90f, 270f, 32, color); // 建议加上 spread 动态扩散值
//
//        // 右半圆括号：从0°到180°（右括号则是 -90->90 或 270->450）
//        drawArc(poseStack, x + 6 + spread, y, 5, -90f, 90f, 32, color);
//
//        // 绘制完毕后恢复深度测试，以免影响后续其他模组的 UI 渲染
//        RenderSystem.enableDepthTest();
//    }
//    public static void drawRectCrosshair(float x, float y, float spread) {
//        drawRectCrosshair(x, y, 6.0f, 1.0f, spread);
//    }

    public static void drawDot(float x, float y){
        if(HudConfig.dotStyle.get() == DotStyle.RECT) {
            drawDotRect(x, y);
        }
        else{
            drawDotCircle(x, y);
        }
    }

    /// 绘制点状准星
    /// @param x 屏幕中心的宽
    /// @param y 屏幕中心的高
    /// @apiNote 固定绘制屏幕中心 2x2 的像素点，里面的数值不能动
    ///（暂时不打算做调节准星大小）
    public static void drawDotRect(float x, float y){
        List<Line> lines = List.of(new Line(x - 0.5F, y - 0.5F, x + 0.5F, y + 0.5F));
        drawLineWithShadow(lines);
    }

    /// 绘制小十字点状准星
    /// @param x 屏幕中心的宽
    /// @param y 屏幕中心的高
    /// @apiNote 固定绘制屏幕中心 2x4 的交叉像素点，里面的数值不能动
    public static void drawDotCircle(float x, float y){
        List<Line> lines = List.of(
                new Line(x - 0.5F, y - 1.0F, x + 0.5F, y + 1.0F),
                new Line(x - 1.0F, y - 0.5F, x + 1.0F, y + 0.5F)
        );
        drawLineWithShadow(lines);
    }

    public static List<Float> Argb(int color){
        return List.of(
                (color >> 24 & 255) / 255.0f,
                (color >> 16 & 255) / 255.0f,
                (color >> 8 & 255) / 255.0f,
                (color & 255) / 255.0f
        );
    }
}
