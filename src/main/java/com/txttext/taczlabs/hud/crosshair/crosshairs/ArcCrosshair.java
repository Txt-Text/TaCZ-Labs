package com.txttext.taczlabs.hud.crosshair.crosshairs;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.txttext.taczlabs.config.fileconfig.HudConfig;
import com.txttext.taczlabs.hud.crosshair.Line;

import java.util.List;

public class ArcCrosshair extends AbstractCrosshair{

    public ArcCrosshair() {
        super(HudConfig.arcCrosshairLength.get());
    }

    /// 绘制方括号准星 [ ]
    /// @param x 屏幕中心的宽
    /// @param y 屏幕中心的高
    /// @param spread 扩散值 + 基础半径
    @Override
    public void Render(float x, float y, float spread) {
        float lineLength = HudConfig.arcCrosshairLength.get(); // 括号垂直方向的总高度的一半
        float lineWidth = HudConfig.arcCrosshairWidth.get() / 2.0f; // 线条粗细
        float tickLength = lineLength / 2.0f; // 括号向内拐的那个小横杠的长度（可以自定义比例）

        // 基础偏移量（括号距离准星中心的 X 轴距离）
        float offset = spread + tickLength;

        List<Line> lines = List.of(
                // ================= 左括号 [ =================
                // 1. 左侧竖线 (Y轴向上下各延伸 lineWidth，包裹住整个高度，形成完整外沿)
                new Line(
                        x - offset - lineWidth, y - lineLength - lineWidth,
                        x - offset + lineWidth, y + lineLength + lineWidth
                ),
                // 2. 左上角向内的横线 (X轴从竖线内沿开始，防止重叠出现加深色块)
                new Line(
                        x - offset + lineWidth, y - lineLength - lineWidth,
                        x - spread, y - lineLength + lineWidth
                ),
                // 3. 左下角向内的横线 (同上)
                new Line(
                        x - offset + lineWidth, y + lineLength - lineWidth,
                        x - spread, y + lineLength + lineWidth
                ),

                // ================= 右括号 ] =================
                // 4. 右侧竖线 (贯通整个高度)
                new Line(
                        x + offset - lineWidth, y - lineLength - lineWidth,
                        x + offset + lineWidth, y + lineLength + lineWidth
                ),
                // 5. 右上角向内的横线 (接在右竖线的左内沿)
                new Line(
                        x + spread, y - lineLength - lineWidth,
                        x + offset - lineWidth, y - lineLength + lineWidth
                ),
                // 6. 右下角向内的横线
                new Line(
                        x + spread, y + lineLength - lineWidth,
                        x + offset - lineWidth, y + lineLength + lineWidth
                )
        );

        // 利用父类现成的方法，一次性画完阴影和主体
        drawLineWithShadow(lines);

        drawDot(x, y);
    }

    /// 绘制圆弧括号准星 ( )
    /// @param x 屏幕中心的宽
    /// @param y 屏幕中心的高
    /// @param spread 扩散值 + 基础半径
//    @Override
//    public void Render(float x, float y, float spread) {
//        int currentColor = HudConfig.color.get();
//        int shadowColor = (HudConfig.shadowAlpha.get() & 0xFF) << 24; // 获取纯阴影色
//
//        float radius = HudConfig.crosshairLength.get(); // 把设定的准星长度当成括号半径
//        float thickness = HudConfig.crosshairWidth.get(); // 圆弧的粗细
//        float shadowOff = HudConfig.shadowOffset.get() / 2.0f; // 阴影偏移量
//
//        // ===== 先画所有的阴影层 =====
//        // 左括号阴影
//        drawArc(x - spread + shadowOff, y + shadowOff, radius, thickness, 90f, 270f, 24, shadowColor);
//        // 右括号阴影
//        drawArc(x + spread + shadowOff, y + shadowOff, radius, thickness, -90f, 90f, 24, shadowColor);
//
//        // ===== 再画所有的主色层 =====
//        // 左括号主色 (180度朝左弯)
//        drawArc(x - spread, y, radius, thickness, 90f, 270f, 24, currentColor);
//        // 右括号主色 (0度朝右弯)
//        drawArc(x + spread, y, radius, thickness, -90f, 90f, 24, currentColor);
//
//        // ===== 最后画中心点 =====
//        drawDot(x, y);
//    }
}
