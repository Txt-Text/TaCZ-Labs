package com.txttext.taczlabs.hud.crosshair.crosshairs;

import com.txttext.taczlabs.config.fileconfig.HudConfig;
import com.txttext.taczlabs.hud.crosshair.Line;

import java.util.List;

public class BracketCrosshair extends AbstractCrosshair {

    public BracketCrosshair() {
        super( HudConfig.crosshairLength.get());
    }

    /// 绘制方括号准星 [ ]
    /// @param x 屏幕中心的宽
    /// @param y 屏幕中心的高
    /// @param spread 扩散值 + 基础半径
    @Override
    public void Render(float x, float y, float spread) {
        float lineLength = HudConfig.crosshairLength.get(); // 括号垂直方向的总高度的一半
        float lineWidth = HudConfig.crosshairWidth.get() / 2.0f; // 线条粗细
        float tickLength = lineLength / 2.0f; // 括号向内拐的那个小横杠的长度（可以自定义比例）

        // 基础偏移量（括号距离准星中心的 X 轴距离）
        float offset = spread + tickLength;

        List<Line> lines = List.of(
                // ================= 左括号 [ =================
                // 1. 左侧竖线
                new Line(
                        x - offset - lineWidth, y - lineLength,
                        x - offset + lineWidth, y + lineLength
                ),
                // 2. 左上角向内的横线
                new Line(
                        x - offset, y - lineLength - lineWidth,
                        x - spread, y - lineLength + lineWidth
                ),
                // 3. 左下角向内的横线
                new Line(
                        x - offset, y + lineLength - lineWidth,
                        x - spread, y + lineLength + lineWidth
                ),

                // ================= 右括号 ] =================
                // 4. 右侧竖线
                new Line(
                        x + offset - lineWidth, y - lineLength,
                        x + offset + lineWidth, y + lineLength
                ),
                // 5. 右上角向内的横线
                new Line(
                        x + spread, y - lineLength - lineWidth,
                        x + offset, y - lineLength + lineWidth
                ),
                // 6. 右下角向内的横线
                new Line(
                        x + spread, y + lineLength - lineWidth,
                        x + offset, y + lineLength + lineWidth
                )
        );

        // 利用父类现成的方法，一次性画完阴影和主体
        drawLineWithShadow(lines);

        drawDot(x, y);
    }
}