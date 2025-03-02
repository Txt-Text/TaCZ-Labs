package com.txttext.taczlabs.mixin;

import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.entity.ShootResult;
import com.tacz.guns.client.gameplay.LocalPlayerDataHolder;
import com.tacz.guns.client.gameplay.LocalPlayerShoot;
import com.tacz.guns.entity.shooter.ShooterDataHolder;
import net.minecraft.client.player.LocalPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(LocalPlayerShoot.class)
public abstract class LocalPlayerShootMixin {
    /*target是控制客户端开火事件的类*/

    @Shadow(remap = false)
    private final LocalPlayerDataHolder data;
    @Shadow(remap = false)
    private final LocalPlayer player;

    public LocalPlayerShootMixin(LocalPlayerDataHolder data, LocalPlayer player) {
        this.data = data;
        this.player = player;
    }

    // 重定向getSynSprintTime()方法调用
    @Redirect(
            method = "shoot", // 目标方法名
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tacz/guns/api/entity/IGunOperator;getSynSprintTime()F" // 替换为实际类路径
            ),
            remap = false
    )
    public float redirectGetSynSprintTime(IGunOperator instance) {
        float originalTime = instance.getSynSprintTime();
        if (originalTime > 0) {
            player.setSprinting(false);//取消疾跑
            return 0;//返回0使原条件判断失败
        }
        // 其他情况返回原值，保持原有逻辑
        return originalTime;
    }
//    @Redirect(
//            method = "shoot",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lcom/tacz/guns/api/entity/IGunOperator;getSynSprintTime()F",
//                    opcode = Opcodes.GETFIELD
//            ),
//            remap = false
//    )
//    public float redirectSynSprintTime(IGunOperator iGunOperator) {
//        return 0; //使 data.sprintTimeS > 0 变为 0 > 0，永久 false
//    }

//    @ModifyConstant(
//            method = "shoot",
//            constant = @Constant(floatValue = 0)
//    )
//    private float Taczlzbs$shoot(float original){
//        return 100000;
//    }
//    @Inject(
//            method = "shoot",
//            at = @At(value = "RETURN", ordinal = 12),
//            remap = false,
//            cancellable = true
//    )
//    public void Taczlzbs$shoot(CallbackInfoReturnable<ShootResult> cir){
//        //player.setSprinting(false);
//        if (cir.getReturnValue() == ShootResult.IS_SPRINTING) {
//            cir.setReturnValue(ShootResult.SUCCESS);
////            cir.cancel();
//        }
//    }
//    @Inject(
//            method = "shoot",
//            at = @At(
//                    value = "JUMP",
//                    opcode = Opcodes.IFLE,
//                    ordinal = 0
//            ),
//            remap = false
//    )
//    public void forceJumpReturn(CallbackInfoReturnable<ShootResult> cir){
//        //player.setSprinting(false);
//    }

//    @Inject(
//            method = "shoot",
//            at = @At(
//                    "HEAD"
////                    value = "RETURN", ordinal = 11,
////                    shift = At.Shift.AFTER
////                    value = "JUMP",
////                    opcode = Opcodes.IFLE,
////                    ordinal = 0,
////                    shift = At.Shift.AFTER
//            ),
//            remap = false
//    )
//    public void Taczlzbs$shoot(CallbackInfoReturnable<ShootResult> cir) {
////        do {
//            IGunOperator gunOperator = IGunOperator.fromLivingEntity(player);
//            if (gunOperator.getSynSprintTime() > 0) {
//                player.setSprinting(false);
//            }
////        } while (false);
//    }
}
