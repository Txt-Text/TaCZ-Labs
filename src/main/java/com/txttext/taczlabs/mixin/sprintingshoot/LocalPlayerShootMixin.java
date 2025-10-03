//package com.txttext.taczlabs.mixin.sprintingshoot;
//
//import com.tacz.guns.api.entity.IGunOperator;
//import com.tacz.guns.client.gameplay.LocalPlayerDataHolder;
//import com.tacz.guns.client.gameplay.LocalPlayerShoot;
//import com.txttext.taczlabs.config.fileconfig.FunctionConfig;
//import net.minecraft.client.player.LocalPlayer;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.*;
//
//@Mixin(LocalPlayerShoot.class)
//@SuppressWarnings("all")
//public abstract class LocalPlayerShootMixin {
//    /*target是控制客户端开火事件的类*/
//    @Shadow(remap = false)
//    private final LocalPlayerDataHolder data;
//    @Shadow(remap = false)
//    private final LocalPlayer player;
//
//    public LocalPlayerShootMixin(LocalPlayerDataHolder data, LocalPlayer player) {
//        this.data = data;
//        this.player = player;
//    }
//
//    @Redirect(
//            method = "shoot",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lcom/tacz/guns/api/entity/IGunOperator;getSynSprintTime()F"
//            ),
//            remap = false
//    )
//    public float redirectGetSynSprintTime(IGunOperator iGunOperator) {
//        //未开启跑射正常执行逻辑
//        float sprintTime = iGunOperator.getSynSprintTime();
//        if (!FunctionConfig.ENABLE_SPRINTING_SHOOT.get()) return sprintTime;
//        //取消疾跑
//        player.setSprinting(false);
//        //如果疾跑没有被取消掉（即疾跑时间还未归零）也不会进行跑射，这为了应对服务端没有安装mod的情况，确保客户端不会假开枪。
//        //而不得不这样妥协的原因是，客户端和服务端的开枪行为没有进行任何通信，也就是说这仅仅依靠代码的逻辑来保证双端同步。
//        //技术细节：客户端和服务端都有一个“疾跑时间”属性，所以我必须确保他俩一样...
//        if(sprintTime != 0.0F) return sprintTime;
//        return 0;//使originalTime=0，跳过原条件
//    }
//}
