package com.txttext.taczlabs.hud.crosshair.crosshairs;

import com.txttext.taczlabs.config.fileconfig.HudConfig;
import com.txttext.taczlabs.hud.crosshair.Line;

import java.util.List;

import static com.txttext.taczlabs.config.fileconfig.HudConfig.rectCrosshairLength;
import static com.txttext.taczlabs.config.fileconfig.HudConfig.rectCrosshairWidth;

public class RectCrosshair extends AbstractCrosshair {

    public RectCrosshair() {
        super(HudConfig.rectCrosshairLength.get()/* + HudConfig.rectCrosshairWidth.get()*/);
    }

    /// 绘制方形准心
    /// @param x 屏幕中心的宽
    /// @param y 屏幕中心的高
    /// @param spread 扩散值 + 基础半径。将基础半径放到外面是因为需要一起计算过渡效果
    @Override
    public void Render(float x, float y, float spread) {
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
        drawDot(x, y);//绘制点状准星
    }
}
