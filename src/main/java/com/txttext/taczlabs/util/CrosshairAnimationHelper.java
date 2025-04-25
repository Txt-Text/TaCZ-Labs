package com.txttext.taczlabs.util;

import net.minecraft.util.Mth;

public class CrosshairAnimationHelper {
    private float lastSpread = 0f;
    private float recoilOffset = 0f;

    private final float smoothFactor;
    private final float recoilDamping;

    public CrosshairAnimationHelper(float smoothFactor, float recoilDamping) {
        this.smoothFactor = smoothFactor;
        this.recoilDamping = recoilDamping;
    }

    /**
     * 调用每帧更新准星扩散动画（根据当前速度）
     * @param velocityXZ 水平速度（0~1）
     * @return 当前应该渲染的 spread 值
     */
    public float update(float velocityXZ) {
        float targetSpread = Mth.clamp(velocityXZ, 0f, 1.0f) * 30f;
        lastSpread = Mth.lerp(smoothFactor, lastSpread, targetSpread);
        recoilOffset = Mth.lerp(recoilDamping, recoilOffset, 0f);
        return lastSpread + recoilOffset;
    }

    /**
     * 每次开枪时调用，增加准星的后坐力抖动
     * @param amount 扩张强度（推荐 5~12）
     */
    public void triggerRecoil(float amount) {
        recoilOffset = amount;
    }

    public float getLastSpread() {
        return lastSpread;
    }
}
/*
*
这个类 CrosshairAnimationHelper 会在你每帧调用 update() 时平滑过渡到新 spread 值，并通过 triggerRecoil() 实现开枪时的瞬时扩张抖动效果。

使用示例：
// 定义在类中（比如 RenderCrosshairEventMixin 里）：
private static final CrosshairAnimationHelper SPREAD_ANIM = new CrosshairAnimationHelper(0.15f, 0.25f);

// 每帧更新：
float velocity = player.getDeltaMovement().horizontalDistance();
float spread = SPREAD_ANIM.update(velocity);

// 每次开枪后调用：
SPREAD_ANIM.triggerRecoil(8.0f);
* */
