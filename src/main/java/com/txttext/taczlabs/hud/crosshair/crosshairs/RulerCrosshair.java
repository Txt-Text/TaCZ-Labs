package com.txttext.taczlabs.hud.crosshair.crosshairs;

import com.txttext.taczlabs.config.fileconfig.HudConfig;
import com.txttext.taczlabs.hud.crosshair.Line;

import java.util.List;

public class RulerCrosshair extends AbstractCrosshair {

    public RulerCrosshair() {
        super();
    }

//    public RulerCrosshair(float minRadius) {
//        super(minRadius);
//    }

    /// 绘制重武器标尺准星
    /// @param x 屏幕中心的宽
    /// @param y 屏幕中心的高
    /// @param spread 扩散值 + 基础半径
    @Override
    public void Render(float x, float y, float spread) {
        float lineLength = 10f;
        float lineWidth = 1f / 2.0f;

        // 标尺准星的特征是下方的垂直主干特别长，这里将其设置为普通准星的 2.5 倍
        float rulerLength = lineLength * 2.5f;

        spread = 2.0f;

        // 三个刻度线的 Y 轴相对下坠坐标 (距离中心越来越远)
        float tick1Y = y + spread + lineLength * 0.6f;
        float tick2Y = y + spread + lineLength * 1.3f;
        float tick3Y = y + spread + lineLength * 2.0f;

        // 三个刻度线的宽度（通常下方的刻度线会逐渐变窄，用来模拟距离透视或风偏范围）
        float w1 = lineLength * 0.6f;
        float w2 = lineLength * 0.4f;
        float w3 = lineLength * 0.2f;

        List<Line> lines = List.of(
                // ================= 1. 左右引导横线 =================
                // 左侧横线
                new Line(
                        x - spread - lineLength, y - lineWidth,
                        x - spread, y + lineWidth
                ),
                // 右侧横线
                new Line(
                        x + spread, y - lineWidth,
                        x + spread + lineLength, y + lineWidth
                ),

                // ================= 2. 下方标尺主干与刻度 (无缝拼接防重叠) =================

                // 第一段垂直主干 (从 spread 起始点 到 刻度1的上沿)
                new Line(
                        x - lineWidth, y + spread,
                        x + lineWidth, tick1Y - lineWidth
                ),
                // 刻度 1 (最长，代表近距离下坠)
                new Line(
                        x - w1, tick1Y - lineWidth,
                        x + w1, tick1Y + lineWidth
                ),

                // 第二段垂直主干 (从 刻度1的下沿 到 刻度2的上沿)
                new Line(
                        x - lineWidth, tick1Y + lineWidth,
                        x + lineWidth, tick2Y - lineWidth
                ),
                // 刻度 2 (中等)
                new Line(
                        x - w2, tick2Y - lineWidth,
                        x + w2, tick2Y + lineWidth
                ),

                // 第三段垂直主干 (从 刻度2的下沿 到 刻度3的上沿)
                new Line(
                        x - lineWidth, tick2Y + lineWidth,
                        x + lineWidth, tick3Y - lineWidth
                ),
                // 刻度 3 (最短，代表远距离下坠)
                new Line(
                        x - w3, tick3Y - lineWidth,
                        x + w3, tick3Y + lineWidth
                ),

                // 收尾的主干尾巴 (从 刻度3的下沿 到 标尺最底端)
                new Line(
                        x - lineWidth, tick3Y + lineWidth,
                        x + lineWidth, y + spread + rulerLength
                )
        );

        // 利用父类一次性画完所有阴影和高亮主体
        drawLineWithShadow(lines);

        // 重武器准星通常保留一个极其精确的中心点，用于第一发点射
        drawDot(x, y);
    }
}
