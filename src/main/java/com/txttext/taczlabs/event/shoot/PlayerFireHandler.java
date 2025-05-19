package com.txttext.taczlabs.event.shoot;

import com.tacz.guns.api.event.common.GunFireEvent;
import com.tacz.guns.resource.pojo.data.gun.GunRecoil;
import com.tacz.guns.resource.pojo.data.gun.GunRecoilKeyFrame;
import com.txttext.taczlabs.config.fileconfig.HudConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.txttext.taczlabs.TaCZLabs.MODID;
import static com.txttext.taczlabs.hud.crosshair.Crosshair.gunData;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PlayerFireHandler {
    public static float fireSpread;

//    public static void register() {
//        MinecraftForge.EVENT_BUS.register(new PlayerFireHandler());
//    }

    @SubscribeEvent
    public void onPlayerFire(GunFireEvent event){
        //这个事件在所有玩家开枪时触发，因此须区分开枪者是否为本地玩家（只响应本地玩家自己的事件），避免多人游戏中同步到其他人
        if(!event.getLogicalSide().isClient()) return;
        LocalPlayer player = Minecraft.getInstance().player;
        if(event.getShooter() != player) return;

        //获取枪械后坐力
        GunRecoil recoil = gunData.getRecoil();
        if (recoil == null) return;
        float kick = getRecoilKick(recoil, 1f);
        PlayerFireHandler.fireSpread += Math.max(kick, 2) * HudConfig.shootingSpread.get();//有一个保底值，不然都看不出来动了//调倍率让视觉更明显
    }

    public static float getRecoilKick(GunRecoil recoil, float modifier) {
        GunRecoilKeyFrame[] frames = recoil.getPitch();
        float maxKick = 0f;
        if (frames != null) {
            for (GunRecoilKeyFrame frame : frames) {
                float[] v = frame.getValue();
                if (v != null && v.length == 2) {
                    maxKick = Math.max(maxKick, Math.max(v[0], v[1]) * modifier);
                }
            }
        }
        return maxKick;
    }
//        // 获取 keyframe（用来取第一个时间点的实际后坐力）
//        GunRecoilKeyFrame[] pitchFrames = recoil.getPitch();
//        if (pitchFrames == null || pitchFrames.length == 0) return;
//
//        float[] pitchRange = pitchFrames[0].getValue(); // value 是 float[2]，表示最小值和最大值
//        float pitch = (float) (pitchRange[0] + Math.random() * (pitchRange[1] - pitchRange[0]));
//
//        // 简单缩放，映射为 fireSpread
//        float spreadBoost = pitch * 0.4f; // 自定义比例，0.4f 可根据实际感觉调节
//
//        // 添加到准星扩张值中（你已经在渲染函数中平滑处理 fireSpread）
//        PlayerFireHandler.fireSpread += spreadBoost;

}
