package com.txttext.taczlabs.hud.crosshair;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;

import java.util.List;
import static com.txttext.taczlabs.config.fileconfig.HudConfig.*;

public class CrosshairRender {
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

        float a = (color >> 24 & 255) / 255f;
        float r = (color >> 16 & 255) / 255f;
        float g = (color >> 8 & 255) / 255f;
        float b = (color & 255) / 255f;

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

    /*绘制各种准星*/
    /// 绘制十字准星
    /// @param x 屏幕中心的宽
    /// @param y 屏幕中心的高
    /// @param spread 扩散值 + 基础半径。将基础半径放到外面是因为需要一起计算过渡效果
    public static void drawCrosshair(float x, float y, float spread) {
        float lineLength = crosshairLength.get();
        float lineWidth = crosshairWidth.get() / 2.0f;// /2是因为opengl单位是0.5像素

        List<Line> lines = List.of(
                //横线：上 & 下
                new Line(
                        x - lineWidth,
                        y + spread,
                        x + lineWidth,
                        y + spread + lineLength
                ),
                new Line(
                        x - lineWidth,
                        y - spread - lineLength,
                        x + lineWidth,
                        y - spread
                ),
                //竖线：左 & 右
                new Line(
                        x - lineLength - spread,
                        y - lineWidth,
                        x - spread,
                        y + lineWidth
                ),
                new Line(
                        x + spread,
                        y - lineWidth,
                        x + lineLength + spread,
                        y + lineWidth
                )
        );

        drawLineWithShadow(lines);
        drawPointCrosshair(x, y);//绘制点状准星
    }
//    public static void drawCrosshair(float x, float y, float spread) {
//        drawCrosshair(x, y, 6.0f, 0.5f, spread);
//    }

    /// 绘制直角准心（手枪准心）
    /// @param x 屏幕中心的宽
    /// @param y 屏幕中心的高
    /// @param spread 扩散值 + 基础半径。将基础半径放到外面是因为需要一起计算过渡效果
    public static void drawRightAngleCrosshair(float x, float y, float spread) {
        float lineLength1 = 5.0f;//两边横线部分的线长
        float lineLength2 = 4.0f;//两边竖线部分的线长
        float lineLength3 = 0.5f;//正中的线长（不算两边的线宽，只有被y轴劈开的一半）
//        float lineWidth = 0.5f;//线宽度
        float lineWidth = rightAngleCrosshairWidth.get() / 2.0f;

        List<Line> lines = List.of(
                // 第一条线：右侧水平
                new Line(
                        x + spread + lineLength1,
                        y,
                        x + spread,
                        y - lineWidth
                ),
                // 第二条线：右侧垂直
                new Line(
                        x + spread + lineWidth,
                        y + lineLength2,
                        x + spread,
                        y
                ),
                // 第三条线：中右竖线
                // 加一是两边的准星部分降低顶部y轴一格，目的是直角准星瞄准的地方是上面的缺口
                new Line(
                        x + lineLength3 + lineWidth,
                        y + lineLength2,
                        x + lineLength3,
                        y + 1
                ),
                // 第四条线：中水平
                new Line(
                        x + lineLength3,
                        y + lineWidth + 1,//中间基准准星
                        x - lineLength3,
                        y + 1
                ),
                // 第五条线：中左竖线
                new Line(
                        x - lineLength3,
                        y + lineLength2,
                        x - lineLength3 - lineWidth,
                        y + 1
                ),
                // 第六条线：左侧垂直
                new Line(
                        x - spread,
                        y + lineLength2,
                        x - spread - lineWidth,
                        y
                ),
                // 第七条线：左侧水平
                new Line(
                        x - spread,
                        y,
                        x - spread - lineLength1,
                        y - lineWidth
                )
        );

        //渲染准星
        drawLineWithShadow(lines);
    }

    /// 绘制方形准心
    /// @param x 屏幕中心的宽
    /// @param y 屏幕中心的高
    /// @param spread 扩散值 + 基础半径。将基础半径放到外面是因为需要一起计算过渡效果
    public static void drawRectCrosshair(float x, float y, float spread) {
        float lineLength = rectCrosshairLength.get();
        float lineWidth = rectCrosshairWidth.get();
        List<Line> lines = List.of(
                //横线：上 & 下
                new Line(
                        x - lineLength,
                        y - spread,
                        x + lineLength,
                        y - spread + lineWidth),
                new Line(
                        x - lineLength,
                        y + spread - lineWidth,
                        x + lineLength,
                        y + spread),
                //竖线：左 & 右
                new Line(
                        x - spread,
                        y - lineLength,
                        x - spread + lineWidth,
                        y + lineLength),
                new Line(
                        x + spread - lineWidth,
                        y - lineLength,
                        x + spread,
                        y + lineLength)
        );
        drawLineWithShadow(lines);
        drawPointCrosshair(x, y);//绘制点状准星
    }
//    public static void drawRectCrosshair(float x, float y, float spread) {
//        drawRectCrosshair(x, y, 6.0f, 1.0f, spread);
//    }

    /// 绘制点状准星
    /// @param x 屏幕中心的宽
    /// @param y 屏幕中心的高
    /// @apiNote 固定绘制屏幕中心 2x2 的像素点，里面的数值不能动
    ///（暂时不打算做调节准星大小）
    public static void drawPointCrosshair(float x, float y){
        List<Line> lines = List.of(new Line(x - 0.5F, y - 0.5F, x + 0.5F, y + 0.5F));
        drawLineWithShadow(lines);
    }

    /// 绘制小十字点状准星
    /// @param x 屏幕中心的宽
    /// @param y 屏幕中心的高
    /// @apiNote 固定绘制屏幕中心 2x4 的交叉像素点，里面的数值不能动
    public static void drawPointCrosshair2(float x, float y){
        List<Line> lines = List.of(
                new Line(x - 0.5F, y - 1.0F, x + 0.5F, y + 1.0F),
                new Line(x - 1.0F, y - 0.5F, x + 1.0F, y + 0.5F)
        );
        drawLineWithShadow(lines);
    }
}
