package com.txttext.taczlabs.mixin.SprintShoot;

import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.client.gameplay.LocalPlayerDataHolder;
import com.tacz.guns.client.gameplay.LocalPlayerShoot;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LocalPlayerShoot.class)
@SuppressWarnings("all")
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

    @Redirect(
            method = "shoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tacz/guns/api/entity/IGunOperator;getSynSprintTime()F"
            ),
            remap = false
    )
    public float redirectGetSynSprintTime(IGunOperator iGunOperator) {
        float originalTime = iGunOperator.getSynSprintTime();
        if (originalTime > 0) {
            /*使 originalTime = 0 跳过原条件*/
            return 0;
        }
        /*其他情况返回原值保持原有逻辑*/
        return originalTime;
    }

//吐槽：注释掉的比我写的多
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
//        do {
//            IGunOperator gunOperator = IGunOperator.fromLivingEntity(player);
//            if (gunOperator.getSynSprintTime() > 0) {
//                player.setSprinting(false);
//            }
//        } while (false);
//    }
}
