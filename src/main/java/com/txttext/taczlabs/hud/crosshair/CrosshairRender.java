package com.txttext.taczlabs.hud.crosshair;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.tacz.guns.resource.pojo.data.gun.InaccuracyType;
import com.txttext.taczlabs.config.fileconfig.HudConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.txttext.taczlabs.config.clothconfig.CrosshairType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import static com.txttext.taczlabs.config.fileconfig.HudConfig.*;

public class CrosshairRender {
    private static float lastSpread = 0f;//保存上一tick的 spread
    private static InaccuracyType lastStatus = InaccuracyType.STAND;
    private static float lastTargetSpread = 0f;
    private static float space = 5.0f;//距离屏幕中央的间距

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

    //决定要渲染的准星类型
    public static void renderCrosshairType(CrosshairType type, float x, float y, Map<InaccuracyType, Float> map, LocalPlayer player){
        InaccuracyType playerStatus = InaccuracyType.getInaccuracyType(player);//获取玩家状态
        //根据准星类型获取不同配置值
        float radius;//半径
        switch (type){
            case CROSSHAIR-> {//十字准星
                radius = crosshairRadius.get().floatValue();
            }
            case RECT-> {//方形准星
                radius = rectCrosshairRadius.get().floatValue();
            }
            case RIGHT_ANGLE-> {//直角准星
                radius = rightAngleCrosshairRadius.get().floatValue();
            }
            default->{//未知情况，正常情况不会触发
                radius = crosshairRadius.get().floatValue();
            }
        }
        //根据状态决定默认半径被影响的倍数（潜行、趴下时缩小准星）
        float status = switch (playerStatus){
            case SNEAK -> 0.7f;
            case LIE -> 0.5f;
            default -> 1f;
        };

        //根据设置决定扩散算法
        float spread = inaccuracySpread.get() ? getRealSpread(map, playerStatus) : getVisalSpread(player);
        float spreadAndRadius = spread + radius * status;//加上默认准星半径
        //更新保存的值
        //lastSpread = spread;

        switch (type){
            case CROSSHAIR-> drawCrosshair(x, y, spreadAndRadius);//绘制十字准星
            case RECT-> drawRectCrosshair(x, y, spreadAndRadius);//绘制方形准星
            case RIGHT_ANGLE-> drawRightAngleCrosshair(x, y, spreadAndRadius);//绘制直角准星
            case POINT-> drawPointCrosshair(x, y);//绘制点状准星
//            case TACZ->
            //未知情况，正常情况不会触发
            default-> drawCrosshair(x, y, spreadAndRadius);//绘制十字准星
        }
    }

    //虚拟的准星扩散，带来最好的视觉体验（但是不考虑扩散）
    private static float getVisalSpread(LocalPlayer player){
        /*获取玩家速度*/
        //PlayerMovementHelper.MovementInfo info = PlayerMovementHelper.getMovementInfo(player);
        //double speed = info.speedPerTick;//获取速度向量（三方向速度）
        Vec3 velocity = player.getDeltaMovement();
        float speed = (float) velocity.horizontalDistance();//只要XZ平面速度
        /*计算当前目标 spread（准星扩张大小）*/
        //根据玩家状态量化准星扩张（不同状态有不同的散布）
        //ShooterDataHolder data = new ShooterDataHolder();
//        taczlabs$space = HudConfig.crosshairRadius.get().floatValue();//获取配置值
//        if (player.isCrouching()){
//            taczlabs$space *= 0.5f;
//        }
        //因素乘起来的量化
        float t = Mth.clamp(speed, 0f, 1.0f);//阈值
        //非线性缩放（调参）
        float targetSpread = t * 30f;//加上量化
        //平滑过渡（Lerp插值）
        float alpha = 0.2f;//越小越平滑
        float spread = Mth.lerp(alpha, lastSpread, targetSpread);
        //更新保存的值
        lastSpread = spread;
        return spread;
    }

    //基于真实扩散值的准星扩散，由于TACZ影响散射的条件过少，没有考虑速度等数值，这可能导致准星比较僵硬
    private static float getRealSpread(Map<InaccuracyType, Float> map, InaccuracyType playerStatus) {
        // 获取枪械的站立扩散值作为基准
        float stand = map.getOrDefault(InaccuracyType.STAND, 1.0f);
        float move = map.getOrDefault(InaccuracyType.MOVE, stand);// 默认使用stand值避免空值
        float sneak = map.getOrDefault(InaccuracyType.SNEAK, stand);
        float lie = map.getOrDefault(InaccuracyType.LIE, stand);

//jisuan sdasda
        // 当前状态的实际扩散值
        float raw = switch (playerStatus) {
            case STAND -> stand;
            case MOVE -> move;
//            case SNEAK -> sneak;
//            case LIE -> lie;
            default -> stand;
        };

// 计算 SNEAK 和 LIE 相对站立的比例
        float sneakFactor = sneak / stand;
        float lieFactor = lie / stand;

        // 根据玩家状态选择适当的扩散因子
        float spreadFactor;
        switch (playerStatus) {
//            case STAND -> spreadFactor = 1f;  // 站立状态
//            case MOVE -> spreadFactor = 1f;   // 移动状态
            case SNEAK -> space *= sneakFactor; // 蹲下状态
            case LIE -> space *= lieFactor; // 趴下状态
            default -> space = 5f;//1f
        }

        // 以 STAND 为基准做归一化
        float base = Math.max(stand, 0.001f);// 防止除 0 错误
        // 当前状态相对于站立的比例偏移（负数变为 0，最多不超过1）
        float normalized = Mth.clamp((raw - base) / base, 0f, 1f);
        // 非线性缩放（根据喜好调整倍率）
        float targetSpread = normalized * 30f;

        boolean statusChanged = playerStatus != lastStatus;
        if (statusChanged) {
            lastTargetSpread = targetSpread;
            lastStatus = playerStatus;
        }
// 平滑靠近当前 targetSpread，不频繁重置 lastTargetSpread
        float tickDelta = Mth.clamp(Minecraft.getInstance().getFrameTime(), 0.001f, 0.05f);//安全保护，极低帧率或者暂停菜单的场景可能让tickDelta很奇怪
        float smoothing = 1.5f;
        float lerpAlpha = 1 - (float) Math.exp(-smoothing * tickDelta);
        float spread = Mth.lerp(lerpAlpha, lastSpread, targetSpread); // 注意是 targetSpread
        // 每一帧平滑靠近 lastTargetSpread，而不是每一帧重新计算
        // 插值平滑
        //float alpha = 0.1f; // 越小越平滑
        //float spread = Mth.lerp(alpha, lastSpread, targetSpread);
        // 平滑靠近 lastTargetSpread，考虑暂停恢复问题
        lastSpread = spread;
        return spread;

        //虚拟扩散方案 启用视觉扩散模式
        // 非线性视觉增强（视觉上差异更明显）
        /*float visualBoost = (float) Math.pow(inaccuracy, 0.7); // 0.7 是调参值，可调大调小
        float amplified = Mth.clamp(visualBoost * 40f, 1f, 40f); // 扩展最大视觉半径上限

        // 平滑动画插值
        float alpha = 0.25f; // 平滑程度（越小越慢）
        float spread = Mth.lerp(alpha, lastSpread, amplified);*/
        //end虚拟扩散方案

//        //前面tacz已经进行了瞄准检查，现在不需要再检查了
//// 阈值计算和非线性缩放
//        float targetSpread = inaccuracy * 30f; // 以归一化值为基础
//
//        // 平滑过渡（Lerp插值）
//        float alpha = (playerStatus == InaccuracyType.MOVE) ? 0.2f : 0.1f; // 根据跑步状态调整平滑因子
//        float spread = Mth.lerp(alpha, lastSpread, targetSpread);
//
//        /*//因素乘起来的量化
//        float t = Mth.clamp(inaccuracy, 0f, 1.0f);//阈值
//        //非线性缩放（调参）
//        float targetSpread = t * 30f;//加上量化
//        *//*
//        * 一个建议优化（可选）
//        目前 t 是速度的线性映射，如果你想让玩家稍微一动准星就开始放大，而不是完全线性递增，可以试试：
//        float t = Mth.clamp(speed * speed, 0f, 1.0f); // 二次曲线，微动也扩散
//        或者更灵活一点：
//        float t = Mth.clamp((float) Math.pow(speed, 1.5), 0f, 1.0f); // 曲线自由调
//        * *//*
//        //平滑过渡（Lerp插值）
//        float alpha = 0.1f;//越小越平滑
//        float spread = Mth.lerp(alpha, lastSpread, targetSpread);*/
//        return spread;
    }

    /*绘制各种准星*/

    //绘制十字准心
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

    //绘制直角准心（手枪准心）
    public static void drawRightAngleCrosshair(float x, float y, float spread) {
        float lineLength1 = 6.0f;//两边横线部分的线长
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

    //绘制方形准心
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
