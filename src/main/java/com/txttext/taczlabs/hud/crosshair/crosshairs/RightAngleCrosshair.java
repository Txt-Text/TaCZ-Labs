package com.txttext.taczlabs.hud.crosshair.crosshairs;

import com.txttext.taczlabs.hud.crosshair.Line;

import java.util.List;

import static com.txttext.taczlabs.config.fileconfig.HudConfig.rightAngleCrosshairWidth;

public class RightAngleCrosshair extends AbstractCrosshair {

    /// 绘制直角准心（手枪准心）
    /// @param x 屏幕中心的宽
    /// @param y 屏幕中心的高
    /// @param spread 扩散值 + 基础半径。将基础半径放到外面是因为需要一起计算过渡效果
    @Override
    public void Render(float x, float y, float spread) {
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
}
