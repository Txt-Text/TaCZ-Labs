package com.txttext.taczlabs.hud.crosshair;

import com.tacz.guns.client.resource.index.ClientGunIndex;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import com.tacz.guns.resource.pojo.data.gun.InaccuracyType;
import com.txttext.taczlabs.event.shoot.PlayerFireHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;

import java.util.Map;

import static com.tacz.guns.resource.pojo.data.gun.InaccuracyType.*;
import static com.txttext.taczlabs.config.fileconfig.HudConfig.*;
import static com.txttext.taczlabs.hud.crosshair.CrosshairRender.*;

public class Crosshair {
    private static float lastSpread = 0f;//保存上一tick的 spread
    public static GunData gunData = null;//枪械数据

    //决定要渲染的准星类型
    public static void renderCrosshair(GuiGraphics graphics, CrosshairType type, float x, float y, ClientGunIndex gunIndex, LocalPlayer player){
        //计算扩散
        float spread = getSpread(type, gunIndex, player, inaccuracySpread.get());
        switch (type){
            case CROSSHAIR-> drawCrosshair(x, y, spread);//绘制十字准星
            case RECT-> drawRectCrosshair(x, y, spread);//绘制方形准星
            case RIGHT_ANGLE-> drawRightAngleCrosshair(x, y, spread);//绘制直角准星
            case ARC -> drawArcCrosshair(graphics, x, y, spread);
            case POINT-> drawDot(x, y);//绘制点状准星
            default-> drawCrosshair(x, y, spread);//未知情况，正常情况不会触发
        }
    }

    //虚拟的准星扩散，带来最好的视觉体验（但是不考虑扩散）
//    private static float getVisalSpread(LocalPlayer player){
//        /*获取玩家速度*/
//        //PlayerMovementHelper.MovementInfo info = PlayerMovementHelper.getMovementInfo(player);
//        //double speed = info.speedPerTick;//获取速度向量（三方向速度）
//        Vec3 velocity = player.getDeltaMovement();
//        float speed = (float) velocity.horizontalDistance();//只要XZ平面速度
//        /*计算当前目标 spread（准星扩张大小）*/
//        //根据玩家状态量化准星扩张（不同状态有不同的散布）
//        //ShooterDataHolder data = new ShooterDataHolder();
//        //因素乘起来的量化
//        float t = Mth.clamp(speed, 0f, 1.0f);//阈值
//        //非线性缩放（调参）
//        float targetSpread = t * 30f;//加上量化
//        //平滑过渡（Lerp插值）
//        float alpha = 0.2f;//越小越平滑
//        float spread = Mth.lerp(alpha, lastSpread, targetSpread);
//        //更新保存的值
//        lastSpread = spread;
//        return spread;
//    }

    //准星扩散值计算
    private static float getSpread(CrosshairType type, ClientGunIndex gunIndex, LocalPlayer player, boolean inaccuracy) {
        //使用散射映射表获取枪械扩散值
        gunData = gunIndex.getGunData();
        GunSpread gunSpread = getGunSpread(gunData.getInaccuracy());
        //以站立为基准归一化各状态扩散，基准为1
        float base = Math.max(gunSpread.stand(), 0.001f);//防止除0
        //float factorStand = 1.0f;//基准
        float factorMove = gunSpread.move() / base;
        float factorSneak = gunSpread.sneak() / base;
        float factorLie = gunSpread.lie() / base;
        //根据状态（潜行、趴下）决定是否缩小准星默认半径（混合影响关闭则按照下面的归一化比例来）
        if(inaccuracy){//开启了严格按照扩散值
            factorSneak = 0.7f;
            factorLie = 0.5f;
        }
        //获取玩家状态
        InaccuracyType playerStatus = InaccuracyType.getInaccuracyType(player);
        float status = switch (playerStatus){
                    case SNEAK -> factorSneak;
                    case LIE -> factorLie;
                    default -> 1f;
                };
//        float status = inaccuracySpread.get() ?
//            switch (playerStatus){//开启了严格按照扩散值
//                case SNEAK -> factorSneak;
//                case LIE -> factorLie;
//                default -> 1f;
//            }
//            :
//            switch (playerStatus){
//                case SNEAK -> 0.7f;
//                case LIE -> 0.5f;
//                default -> 1f;
//            };

        //速度阈值保护。确认混合影响配置关闭。限制在 [0,1]
        //获取玩家速度（XZ平面速度）
        float speed = (float) player.getDeltaMovement().horizontalDistance();
        float speedFactor = !inaccuracy ? Mth.clamp(speed, 0f, 1f) * 20 : 0;

        //获取移动/站立时的实际扩散值
        //如果想要速度较小的时候不扩散阈值可以高点
        //不*2变化就太小了
        //需要准星扩散值不为0
        float raw = /*maxSpread.get() != 0 &&*/ speed > 0.01f ? factorMove*2 : 1f;//tacz的状态极其不可靠因此自己判断

        /*
        //归一化扩散（以站立为基准）
        float base = Math.max(stand, 0.001f);//防止除0
        //当前状态相对于站立的比例偏移（负数变为 0，最多不超过1）
        float normalized = Mth.clamp((raw - base) / base, 0f, 1f);
        //结合扩散和速度影响，计算目标准星扩散
        float targetSpread = (normalized + speedFactor) * crosshairSpread.get();//从配置获取扩散倍率
        * */

        //获取准星默认半径
        float radius = getRadius(type);
        //结合扩散和速度影响，计算目标准星扩散
        //baseSpread（基础扩散） = 默认准星半径 * 由潜行和趴下影响的倍率
        float baseSpread = radius * status * raw;//基础扩散
        //(radius * status) + speedFactor * crosshairSpread.get() + raw//如果采用加raw的算法，获取raw的时候应该获取move和stand
        //targetSpread = 基础扩散 + 速度影响 + 开火抖动
        float fireSpread = PlayerFireHandler.getFireSpread();
        float targetSpread = Math.min(baseSpread + speedFactor, radius + maxSpread.get()) + fireSpread;//限制在最大扩散范围内（不限制开火扩散），加上radius是需要不受默认半径影响

        //每帧自然衰减 fireSpread，防止一直扩张
        PlayerFireHandler.setFireSpread(Mth.lerp(0.15f, fireSpread, 0f));
        //平滑靠近当前 targetSpread，不频繁重置 lastTargetSpread
        float tickDelta = Mth.clamp(Minecraft.getInstance().getFrameTime(), 0.001f, 0.05f);//安全保护，极低帧率或者暂停菜单的场景可能让tickDelta很奇怪
        float smoothing = 3f;
        float lerpAlpha = 1 - (float) Math.exp(-smoothing * tickDelta);
        float spread = Mth.lerp(lerpAlpha, lastSpread, targetSpread);
        lastSpread = spread;
        return spread;
//        一个建议优化（可选）
//        目前 t 是速度的线性映射，如果你想让玩家稍微一动准星就开始放大，而不是完全线性递增，可以试试：
//        float t = Mth.clamp(speed * speed, 0f, 1.0f); // 二次曲线，微动也扩散
//        或者更灵活一点：
//        float t = Mth.clamp((float) Math.pow(speed, 1.5), 0f, 1.0f); // 曲线自由调
    }

//    public static float getRealSpread(){
//
//    }
//
//    public static float getVisalSpread(){
//
//    }

    //根据准星类型获取配置中的默认半径
    public static float getRadius(CrosshairType type){
        return switch (type) {
            case CROSSHAIR -> (float) crosshairRadius.get();//十字准星
            case RECT -> (float) rectCrosshairRadius.get();//方形准星
            case RIGHT_ANGLE -> (float) rightAngleCrosshairRadius.get();//直角准星
            default -> (float) crosshairRadius.get();//点状准星和未知情况
        };
    }

    //获取枪械扩散值
    public static GunSpread getGunSpread(Map<InaccuracyType, Float> map){
        float stand = map.getOrDefault(STAND, 1.0f);
        return new GunSpread(
                stand,
                map.getOrDefault(MOVE, stand),//默认使用stand避免空值//move
                map.getOrDefault(SNEAK, stand),//sneak
                map.getOrDefault(LIE, stand)//lie
        );
    }

    //归一化扩散值
    public static void normalizeSpread(){

    }
}
