package com.txttext.taczlabs.hud.crosshair.crosshairs;

import com.txttext.taczlabs.hud.crosshair.Line;

import java.util.List;

import static com.txttext.taczlabs.config.fileconfig.HudConfig.crosshairLength;
import static com.txttext.taczlabs.config.fileconfig.HudConfig.crosshairWidth;

public class Crosshair extends AbstractCrosshair{

    /// 绘制十字准星
    /// @param x 屏幕中心的宽
    /// @param y 屏幕中心的高
    /// @param spread 扩散值 + 基础半径。将基础半径放到外面是因为需要一起计算过渡效果
    @Override
    public void Render(float x, float y, float spread) {
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
        drawDot(x, y);//绘制点状准星
    }
}
