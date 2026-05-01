package com.txttext.taczlabs.hud.crosshair;

import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.client.resource.index.ClientGunIndex;
import com.tacz.guns.resource.modifier.AttachmentCacheProperty;
import com.tacz.guns.resource.modifier.custom.InaccuracyModifier;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import com.tacz.guns.resource.pojo.data.gun.InaccuracyType;
//import com.txttext.taczlabs.event.shoot.PlayerFireHandler;
import com.txttext.taczlabs.hud.crosshair.crosshairs.*;
import com.txttext.taczlabs.util.CrosshairShootManager;
import com.txttext.taczlabs.util.DeltaTime;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;

import java.util.Map;

import static com.tacz.guns.resource.pojo.data.gun.InaccuracyType.*;
import static com.txttext.taczlabs.config.fileconfig.HudConfig.*;

public class CrosshairRender {
    //静态常量
    private static final float lieGunSpread = 0.5f;
    private static final float sneakGunSpread = 0.7f;
    //数据
    //public static GunData GUN_DATA;//枪械数据
    private static float lastSpread = 0f;//保存上一tick的 spread
    private static final DeltaTime deltaTime = new DeltaTime();
    //private static long lastTime = System.nanoTime();
    //定义一些用于视觉调整的常量，避免写死在代码里
    //private static final float VISUAL_SPREAD_MULTIPLIER = 1.5f; //整体动态变化放大倍数
    //private static final float SHOOT_PENALTY_SCALE = 3.0f;      //开火后坐力在准星上的放大倍数
    // 视觉效果常量，可根据你的喜好微调
    private static final float VISUAL_SCALE = 2.0f; // 姿态变化时的视觉放大倍数
    private static CrosshairType lastCrosshairType;
    private static AbstractCrosshair crosshair;

    public enum SpreadType{
        REAL,//按照真实枪械散射
        VIRTUAL,//按照虚拟散射
        SPEED//按照速度散射
    }

    //决定要渲染的准星类型
//    public static void renderCrosshair(GuiGraphics graphics, CrosshairType type, float x, float y, ClientGunIndex gunIndex, LocalPlayer player){
//        //计算扩散
//        float spread = getSpread(type, gunIndex, player);
//        //System.out.println("spread = " + spread);
//        //绘制准星
//        switch (type){
//            case CROSSHAIR-> drawCrosshair(x, y, spread);//绘制十字准星
//            case RECT-> drawRectCrosshair(x, y, spread);//绘制方形准星
//            case RIGHT_ANGLE-> drawRightAngleCrosshair(x, y, spread);//绘制直角准星
//            case ARC -> drawArcCrosshair(graphics, x, y, spread);
//            case POINT-> drawDot(x, y);//绘制点状准星
//            default-> drawCrosshair(x, y, spread);//未知情况，正常情况不会触发
//        }
//    }
    public static void renderCrosshair(GuiGraphics graphics, CrosshairType type, float x, float y, ClientGunIndex gunIndex, LocalPlayer player){

        //点状准星直接绘制
        if(lastCrosshairType == CrosshairType.POINT){
            AbstractCrosshair.drawDot(x, y);//点状准星
            return;
        }

        //还未初始化准星或类型不一样了
        if(crosshair == null || type != lastCrosshairType){
            crosshair = switch (type){
                case CROSSHAIR-> new Crosshair();//十字准星
                case RECT-> new RectCrosshair();//方形准星
                case RIGHT_ANGLE-> new RightAngleCrosshair();//直角准星
                case ARC -> new ArcCrosshair();//括号准星
                case RULER ->  new RulerCrosshair();//标尺准星
                default-> new Crosshair();//未知情况，正常情况不会触发
            };
        }

        //计算扩散
        float spread = Math.max(getSpread(type, gunIndex, player), crosshair.getMinRadius());

        //渲染准星
        crosshair.Render(x, y, spread);
    }

    //准星扩散值计算
    private static float getSpread(CrosshairType type, ClientGunIndex gunIndex, LocalPlayer player) {
        //选择散射类型
        //boolean inaccuracy = inaccuracySpread.get();
        return switch(spreadTypes.get()){
            case REAL -> getRealSpread(type, gunIndex, player);
            case VIRTUAL -> getVirtualSpread(type, player);
            case SPEED -> getSpeedSpread(type, player);
        };
    }

    //根据枪械的真实准星扩散，考虑真实扩散
//    private static float getRealSpread(CrosshairType type, ClientGunIndex gunIndex, LocalPlayer player){
//        //使用散射映射表获取枪械扩散值
//        //GUN_DATA = gunIndex.getGunData();
//        GunData gunData = gunIndex.getGunData();
//
//        //获取准星扩散数据
//        CrosshairSpread crosshairSpread = getCrosshairSpread(gunData);
//        float move = crosshairSpread.spreadData().move();//如果枪包内数据不规范导致格式错误这三个数据可能为NAN，需健壮性检查
//        //float sneak = crosshairSpread.spreadData().sneak();
//        //float lie = crosshairSpread.spreadData().lie();
//
//        //获取玩家状态，根据状态（潜行、趴下）决定是否缩小准星默认半径倍率
//        InaccuracyType playerStatus = InaccuracyType.getInaccuracyType(player);
//        float status = switch (playerStatus){
//            case SNEAK -> crosshairSpread.spreadData().sneak();
//            case LIE -> crosshairSpread.spreadData().lie();
//            default -> 1f;
//        };
//
//        //获取移动/站立时的实际扩散值（tacz的状态不可靠因此自己判断）
//        //float raw = isMoving(player) ? 3 * move - 2 : 1f;//由(move + (move-1)* 2)得来，意思是move + move大于1的部分 *2，不*2变化就太小了
//        float raw = isMoving(player) ? Math.max(0.1f, 1f + (move - 1f) * SHOOT_PENALTY_SCALE) : 1f;
//
//        //获取准星默认半径
//        float radius = getRadius(type);
//        //结合扩散和速度影响，计算目标准星扩散
//        //baseSpread（基础扩散） = 默认准星半径 * 由潜行和趴下影响的倍率
//        float baseSpread = radius * status * raw;//基础扩散
//        return lerpAndUpdateSpread(baseSpread, radius);
//    }

    public static float getRealSpread(CrosshairType type, ClientGunIndex gunIndex, LocalPlayer player) {
        IGunOperator operator = IGunOperator.fromLivingEntity(player);
        AttachmentCacheProperty cacheProperty = operator.getCacheProperty();

        // 1. 获取“活”的面板基础值 (包含配件修饰)
        float liveStand = 1.0f;
        float liveSneak = 1.0f;
        float liveLie = 1.0f;
        float liveAim = 0.1f; // 瞄准时扩散极小

        if (cacheProperty != null) {
            Map<InaccuracyType, Float> liveInaccuracyMap = cacheProperty.getCache(InaccuracyModifier.ID);
            if (liveInaccuracyMap != null) {
                liveStand = liveInaccuracyMap.getOrDefault(InaccuracyType.STAND, 1.0f);
                liveSneak = liveInaccuracyMap.getOrDefault(InaccuracyType.SNEAK, 1.0f);
                liveLie = liveInaccuracyMap.getOrDefault(InaccuracyType.LIE, 1.0f);
                liveAim = liveInaccuracyMap.getOrDefault(InaccuracyType.AIM, 0.1f);
            }
        }

        // 2. 判断当前主导姿态乘区 (排他性 if-else，防止多个状态冲突)
        float currentStanceSpread;
        boolean isMoving = player.getDeltaMovement().horizontalDistanceSqr() > 0.005;

        if (!player.onGround()) {
            currentStanceSpread = liveStand * 1.8f; // 空中大幅度扩散
        } else if (isMoving) {
            // TaCZ 的 InaccuracyType 默认没有 MOVE，我们使用静态数据的比例来模拟
            float staticStand = gunIndex.getGunData().getInaccuracy(InaccuracyType.STAND);
            // 如果你的静态JSON里有move的字段，可以替换成获取move。这里暂设移动是站立的1.3倍
            float moveRatio = staticStand > 0 ? 1.3f : 1.3f;
            currentStanceSpread = liveStand * moveRatio;
        } else if (player.getPose() == net.minecraft.world.entity.Pose.SWIMMING || operator.getDataHolder().isCrawling) {
            currentStanceSpread = liveLie;
        } else if (player.isCrouching()) {
            currentStanceSpread = liveSneak;
        } else {
            currentStanceSpread = liveStand; // 站立静止
        }

        // 视觉放大器 (将姿态带来的差异放大，且保证绝对不会出现负数)
        // 逻辑：以站立基础值为基准1，变化的部分放大 VISUAL_SCALE 倍
        float stateRatio = currentStanceSpread / Math.max(0.001f, liveStand);
        float visualStanceRatio = Math.max(0.1f, 1f + (stateRatio - 1f) * VISUAL_SCALE);

        // 叠加瞄准进度 (ADS)
        float aimProgress = operator.getSynAimingProgress(); // 范围 0.0 ~ 1.0
        // 开镜时准星收缩：从腰射的视觉倍率 平滑过渡(Lerp) 到 瞄准的极限倍率
        float finalBaseRatio = Mth.lerp(aimProgress, visualStanceRatio, liveAim);

        //获取准星初始半径并计算目标散布
        float defaultRadius = getRadius(type);
        float targetSpread = defaultRadius * finalBaseRatio;

        //加上开火跳动的惩罚值
        targetSpread += CrosshairShootManager.currentShootPenalty;

        //平滑更新
        return lerpAndUpdateSpread(targetSpread, defaultRadius);
    }

    //虚拟的准星扩散，不考虑真实扩散
    private static float getVirtualSpread(CrosshairType type, LocalPlayer player){
        //获取玩家状态，根据状态（潜行、趴下）决定是否缩小准星默认半径倍率
        InaccuracyType playerStatus = InaccuracyType.getInaccuracyType(player);
        float status = switch (playerStatus){
            case SNEAK -> sneakGunSpread;
            case LIE -> lieGunSpread;
            default -> 1f;
        };

        //判断是否正在移动
        float speedFactor = isMoving(player) ? 2 : 1;
        //获取准星默认半径
        float radius = getRadius(type);

        //结合扩散和速度影响，计算目标准星扩散
        float baseSpread = radius * status * speedFactor;//baseSpread = 默认准星半径 * 由潜行和趴下影响的倍率

        return lerpAndUpdateSpread(baseSpread, radius);
    }

    //虚拟的准星扩散，不考虑真实扩散
    private static float getSpeedSpread(CrosshairType type, LocalPlayer player){
        //获取玩家状态，根据状态（潜行、趴下）决定是否缩小准星默认半径倍率
        InaccuracyType playerStatus = InaccuracyType.getInaccuracyType(player);
        float status = switch (playerStatus){
            case SNEAK -> sneakGunSpread;
            case LIE -> lieGunSpread;
            default -> 1f;
        };

        //获取玩家速度（XZ平面速度）
        float speed = (float) player.getDeltaMovement().horizontalDistance();
        float speedFactor = Mth.clamp(speed, 0f, 1f) * 80;//速度阈值保护。限制在 [0,1]
        //获取准星默认半径
        float radius = getRadius(type);

        //结合扩散和速度影响，计算目标准星扩散
        float baseSpread = radius * status + speedFactor;//baseSpread = 默认准星半径 * 由潜行和趴下影响的倍率

        return lerpAndUpdateSpread(baseSpread, radius);
    }

    private static boolean isMoving(LocalPlayer player){
        return player.getDeltaMovement().horizontalDistanceSqr() > 0.005;
    }

    //getVisalSpread()和getRealSpread()算出目标扩散值后，由此方法完成插值、更新lastSpread，衰减fireSpread
//    private static float lerpAndUpdateSpread(float baseSpread, float radius){
//        //获取开火叠加值
//        float fireSpread = PlayerFireHandler.getFireSpread();
//        //targetSpread = 基础扩散 + 速度影响 + 开火抖动
//        float targetSpread = Math.min(baseSpread, radius + maxSpread.get()) + fireSpread;//限制在最大扩散范围内（不限制开火扩散），加上radius是需要不受默认半径影响
//
//        float smoothing = animSpeed.get();
//
//        //自然衰减 fireSpread（真实时间驱动）
//        float tickDelta = deltaTime.updateTimeAndGetDeltaSec(); // 秒
//        float decayAlpha = 1 - (float)Math.exp(-smoothing * tickDelta);
//        PlayerFireHandler.setFireSpread(Mth.lerp(decayAlpha, fireSpread, 0f));
//
//        //固定时间步长，保证开火扩散低帧率不跳大
//        float fixedDelta = 1 / 60f;
//        float lerpAlpha = 1 - (float)Math.exp(-smoothing * fixedDelta);
//
//        //平滑靠近 targetSpread
//        float spread = Mth.lerp(lerpAlpha, lastSpread, targetSpread);
//        lastSpread = spread;//更新lastSpread
//        return spread;
//    }
    private static float lerpAndUpdateSpread(float baseSpread, float radius){
        // 从新的管理器中获取开火激增值
        float fireSpread = CrosshairShootManager.currentShootPenalty;

        // targetSpread = 基础扩散 + 速度影响 + 开火抖动
        // 限制在最大扩散范围内（不限制开火扩散），加上radius是需要不受默认半径影响
        float targetSpread = Math.min(baseSpread, radius + maxSpread.get()) + fireSpread;

        float smoothing = animSpeed.get();

        // 自然衰减 fireSpread（基于真实时间驱动，渲染层帧率解耦）
        float tickDelta = deltaTime.updateTimeAndGetDeltaSec(); // 秒
        float decayAlpha = 1 - (float)Math.exp(-smoothing * tickDelta);

        // 将衰减后的结果直接写回管理器的公开静态变量
        CrosshairShootManager.currentShootPenalty = Mth.lerp(decayAlpha, fireSpread, 0f);

        // 固定时间步长，保证开火扩散低帧率不跳大
        float fixedDelta = 1 / 60f;
        float lerpAlpha = 1 - (float)Math.exp(-smoothing * fixedDelta);

        // 平滑靠近 targetSpread
        float spread = Mth.lerp(lerpAlpha, lastSpread, targetSpread);
        lastSpread = spread; // 更新lastSpread

        return spread;
    }

    //根据准星类型获取配置中的默认半径
    private static float getRadius(CrosshairType type){
        return switch (type) {
            case CROSSHAIR -> (float) crosshairRadius.get();//十字准星
            case RECT -> (float) rectCrosshairRadius.get();//方形准星
            case RIGHT_ANGLE -> (float) rightAngleCrosshairRadius.get();//直角准星
            default -> (float) crosshairRadius.get();//点状准星和未知情况
        };
    }

    //获取枪械扩散值
    private static GunSpread getGunSpread(Map<InaccuracyType, Float> map){
        float stand = map.getOrDefault(STAND, 1.0f);
        return new GunSpread(
                new SpreadData(
                        stand,
                        map.getOrDefault(MOVE, stand),//默认使用stand避免空值//move
                        map.getOrDefault(SNEAK, stand),//sneak
                        map.getOrDefault(LIE, stand)//lie
                )
        );
    }

    //将枪械扩散值归一化为准星扩散值，这个版本的区别是不需要inaccuracy（因为现在的getVisalSpread()暂时不需要一个弄复杂的Factor，直接取这两个常量就完了
    private static CrosshairSpread getCrosshairSpread(GunData data){
        //gunSpread数据
        GunSpread gunSpread = getGunSpread(data.getInaccuracy());
        float base = gunSpread.spreadData().stand();
        float move = gunSpread.spreadData().move();
        float sneak = gunSpread.spreadData().sneak();
        float lie = gunSpread.spreadData().lie();

        //以站立为基准归一化各状态扩散，基准为1

        //防止除0导致NaN特写一个if
        if(base != 0){
            return new CrosshairSpread(new SpreadData(base, move / base, sneak / base, lie / base));
        }
        else{
            return new CrosshairSpread(new SpreadData(1 , 1, 1, 1));
        }
    }
}
