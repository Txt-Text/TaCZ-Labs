package com.txttext.taczlabs.hud.crosshair;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;

import java.util.List;

import com.txttext.taczlabs.config.clothconfig.CrosshairType;
import static com.txttext.taczlabs.config.fileconfig.HudConfig.color;
import static com.txttext.taczlabs.config.fileconfig.HudConfig.shadowOffset;

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
            drawRect(
                    l.x1() + shadowOffset.get().floatValue(), l.y1() + shadowOffset.get().floatValue(),
                    l.x2() + shadowOffset.get().floatValue(), l.y2() + shadowOffset.get().floatValue(),
                    shadowColor
            );
        }
        //绘制所有准星主体
        for (Line l : lines) {
            drawRect(l.x1(), l.y1(), l.x2(), l.y2(), color.get());
        }
    }

    public static void drawLineWithShadow(List<Line> lines){
        drawLineWithShadow(lines, 0x80000000);//半透明黑色阴影
    }

    /*计算Spread值*/
    public static void computeSpread(){

    }

    //决定要渲染的准星类型
    public static void renderCrosshairType(CrosshairType type, float x, float y, float spread){
        switch (type){
            case CROSSHAIR-> {
                drawCrosshair(x, y, spread);//绘制十字准星
            }
            case RECT-> {
                drawRectCrosshair(x, y, spread);//绘制方形准星
            }
            case RIGHT_ANGLE-> {
                drawRightAngleCrosshair(x, y, spread);//绘制直角准星
            }
            case POINT-> {
                drawPointCrosshair(x, y);//绘制点状准星
            }
            case TACZ->{
            }
            default->{//未知情况，正常情况不会触发
                drawCrosshair(x, y, spread);//绘制十字准星
            }
        }
    }

    /*各种准星*/
    //绘制点状准星
    public static void drawPointCrosshair(float x, float y){
        List<Line> lines = List.of(new Line(x - 0.5F, y - 0.5F, x + 0.5F, y + 0.5F));
        drawLineWithShadow(lines);
    }

    //绘制十字准心
    public static void drawCrosshair(float x, float y, float spread) {
        float lineWidth = 0.5f;
        float lineLength = 6.0f;
        float space = 5.0f;//距离屏幕中央的间距

        List<Line> lines = List.of(
                //横线：上 & 下
                new Line(
                        x - lineWidth,
                        y + spread + space,
                        x + lineWidth,
                        y + spread + lineLength + space
                ),
                new Line(
                        x - lineWidth,
                        y - spread - lineLength - space,
                        x + lineWidth,
                        y - spread - space
                ),
                //竖线：左 & 右
                new Line(
                        x - lineLength - spread - space,
                        y - lineWidth,
                        x - spread - space,
                        y + lineWidth
                ),
                new Line(
                        x + spread + space,
                        y - lineWidth,
                        x + lineLength + spread + space,
                        y + lineWidth
                )
        );

        drawLineWithShadow(lines);
        drawPointCrosshair(x, y);//绘制点状准星
    }

    //绘制直角准心（手枪准心）
    public static void drawRightAngleCrosshair(float x, float y, float spread) {
        float lineLength1 = 6.0f;//两边横线部分的线长
        float lineLength2 = 4.0f;//两边竖线部分的线长
        float lineLength3 = 0.5f;//正中的线长（不算两边的线宽，只有被y轴劈开的一半）
        float lineWidth1 = 0.5f;//线宽度
        float space = 5.0f;//距离屏幕中央的间距

        List<Line> lines = List.of(
                // 第一条线：右侧水平
                new Line(//减一是两边的准星部分抬高y轴一格
                        x + spread + lineLength1 + space,
                        y - 1,
                        x + spread + space,
                        y - lineWidth1 - 1
                ),
                // 第二条线：右侧垂直
                new Line(
                        x + spread + lineWidth1 + space,
                        y + lineLength2 - 1,
                        x + spread + space,
                        y - 1
                ),
                // 第三条线：中右竖线
                new Line(
                        x + lineLength3 + lineWidth1,
                        y + lineLength2 - 1,
                        x + lineLength3,
                        y
                ),
                // 第四条线：中水平
                new Line(
                        x + lineLength3,
                        y + lineWidth1,
                        x - lineLength3,
                        y
                ),
                // 第五条线：中左竖线
                new Line(
                        x - lineLength3,
                        y + lineLength2 - 1,
                        x - lineLength3 - lineWidth1,
                        y
                ),
                // 第六条线：左侧垂直
                new Line(
                        x - spread - space,
                        y + lineLength2 - 1,
                        x - spread - lineWidth1 - space,
                        y - 1
                ),
                // 第七条线：左侧水平
                new Line(
                        x - spread - space,
                        y - 1,
                        x - spread - lineLength1 - space,
                        y - lineWidth1 - 1
                )
        );

        //渲染准星
        drawLineWithShadow(lines);
    }

    //绘制方形准心
    public static void drawRectCrosshair(float x, float y, float spread) {
        float lineWidth = 1.0f;
        float lineLength = 6.0f;
        List<Line> lines = List.of(
                //横线：上 & 下
                new Line(x - lineLength, y - 10 - spread, x + lineLength, y - 10 - spread + lineWidth),
                new Line(x - lineLength, y + 10 + spread - lineWidth, x + lineLength, y + 10 + spread),
                //竖线：左 & 右
                new Line(x - 10 - spread, y - lineLength, x - 10 - spread + lineWidth, y + lineLength),
                new Line(x + 10 + spread - lineWidth, y - lineLength, x + 10 + spread, y + lineLength)
        );
        drawLineWithShadow(lines);
        drawPointCrosshair(x, y);//绘制点状准星
    }
}
