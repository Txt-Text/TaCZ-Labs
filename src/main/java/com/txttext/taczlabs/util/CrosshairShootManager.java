package com.txttext.taczlabs.util;

import com.tacz.guns.api.event.common.GunShootEvent;
import com.tacz.guns.resource.pojo.data.gun.GunRecoilKeyFrame;
import com.txttext.taczlabs.config.fileconfig.HudConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.resource.modifier.AttachmentCacheProperty;
import com.tacz.guns.resource.modifier.custom.RecoilModifier;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class CrosshairShootManager {
    //准星加大小的动态活值，公开给渲染层读取和修改
    public static float currentShootPenalty = 0f;
    //衰减速度，越小恢复越快 (0.85 表示每 Tick 缩小 15%)
    private static final float DECAY_RATE = 0.70f;

    /**
     * 监听 TaCZ 的开火事件
     */
//    @SubscribeEvent
//    public static void onGunShoot(GunShootEvent event) {
//        //确保是客户端玩家自己开火
//        if (event.getLogicalSide().isClient() && event.getShooter() == net.minecraft.client.Minecraft.getInstance().player) {
//            //甚至可以在这里根据 event.getGunItemStack() 获取该枪的真实后坐力数据，来动态改变 SHOOT_JUMP_AMOUNT
//            currentShootPenalty += SHOOT_JUMP_AMOUNT;
//
//            //设置一个上限，防止准星无限变大
//            if (currentShootPenalty > 40.0f) {
//                currentShootPenalty = 40.0f;
//            }
//        }
//    }
    @SubscribeEvent
    public static void onGunShoot(GunShootEvent event) {
        // 确保是客户端本地玩家开火
        if (event.getLogicalSide().isClient() && event.getShooter() == net.minecraft.client.Minecraft.getInstance().player) {

            if(!HudConfig.shootingSpread.get()) {
                currentShootPenalty = 0f;
                return;
            }

            ItemStack gunItem = event.getGunItemStack();
            IGun iGun = IGun.getIGunOrNull(gunItem);
            if (iGun == null) return;

            ResourceLocation gunId = iGun.getGunId(gunItem);

            TimelessAPI.getClientGunIndex(gunId).ifPresent(index -> {
                GunData gunData = index.getGunData();

                // === 动态获取枪械真实后坐力系数 ===
                float baseKick = 5.0f; // 最终保底值

                try {
                    // 尝试方案 A：从后坐力关键帧中提取真实的 Pitch（上下跳动）第一帧爆发值
                    if (gunData.getRecoil() != null) {
                        GunRecoilKeyFrame[] pitchFrames = gunData.getRecoil().getPitch();

                        if (pitchFrames != null && pitchFrames.length > 0) {

                            // 获取第一帧的向量数组
                            float[] frameValue = pitchFrames[0].getValue();

                            // 确保数组不为空且至少有一个值
                            if (frameValue != null && frameValue.length > 0) {
                                // 取出数组里的第一个值 [0]，这就是真正的浮点数大小
                                baseKick = Math.abs(frameValue[0]) * 15.0f;
                            }
                        }
                    }
                } catch (Exception e) {
                    // 尝试方案 B：代偿估算算法（如果枪械包没有写标准的 Recoil 关键帧）
                    // 逻辑：通常枪械的单发伤害越大，后坐力/准星跳动越猛（如狙击枪 vs 冲锋枪）
                    if (gunData.getBulletData() != null) {
                        float damage = gunData.getBulletData().getDamageAmount();
                        // 假设冲锋枪伤害 4 (kick=6)，狙击枪伤害 20 (kick=30)
                        baseKick = damage * 1.5f;
                    }
                }

                // 限制基础跳动的最低下限，防止数值过小导致没反馈
                if (baseKick < 3.0f) baseKick = 3.0f;

                // === 获取配件对后坐力的“活”衰减乘区 ===
                float recoilModifier = 1.0f;
                IGunOperator operator = IGunOperator.fromLivingEntity(event.getShooter());
                AttachmentCacheProperty cache = operator.getCacheProperty();

                if (cache != null) {
                    try {
                        // 读取 RecoilModifier 的配件属性 (比如垂直握把减小后坐力，返回 0.8)
                        Object modifierData = cache.getCache(RecoilModifier.ID);
                        if (modifierData instanceof Float f) {
                            recoilModifier = f;
                        } else if (modifierData instanceof Double d) {
                            recoilModifier = d.floatValue();
                        }
                    } catch (Exception ignored) {
                        // 静默捕捉：某些枪械没装配件时可能为空，直接保持 1.0 即可
                    }
                }

                // === 计算最终激增值并叠加 ===
                // 公式：基础踢力 * 配件衰减乘区
                float finalJumpAmount = baseKick * recoilModifier;
                currentShootPenalty += finalJumpAmount;

                // === 设置动态上限 ===
                // 防止高射速武器（如加特林、Vector）一直开火把准星撑满全屏幕
                // 动态上限设置为单发跳动量的 4 倍，或最低 30 像素
                float maxPenalty = Math.max(HudConfig.maxSpread.get(), finalJumpAmount * 4.0f);
                if (currentShootPenalty > maxPenalty) {
                    currentShootPenalty = maxPenalty;
                }
            });
        }
    }

    /**
     * 监听客户端 Tick 事件，让准星平滑恢复
     */
//    @SubscribeEvent
//    public static void onClientTick(TickEvent.ClientTickEvent event) {
//        if (event.phase == TickEvent.Phase.END) {
//            if (currentShootPenalty > 0) {
//                //每 tick 衰减
//                currentShootPenalty *= DECAY_RATE;
//
//                //抹零头，防止浮点数无限运算
//                if (currentShootPenalty < 0.1f) {
//                    currentShootPenalty = 0f;
//                }
//            }
//        }
//    }
}
