package com.txttext.taczlabs.mixin.SprintShoot;

import com.tacz.guns.entity.shooter.LivingEntityDrawGun;
import com.tacz.guns.entity.shooter.LivingEntityShoot;
import com.tacz.guns.entity.shooter.ShooterDataHolder;
import net.minecraft.world.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntityShoot.class)
@SuppressWarnings("all")
public abstract class LivingEntityShootMixin {
    /*target是控制服务端开火事件的类*/
    @Shadow(remap = false)
    private final LivingEntity shooter;
    @Shadow(remap = false)
    private final ShooterDataHolder data;
    @Shadow(remap = false)
    private final LivingEntityDrawGun draw;

    public LivingEntityShootMixin(LivingEntity shooter, ShooterDataHolder data, LivingEntityDrawGun draw) {
        this.shooter = shooter;
        this.data = data;
        this.draw = draw;
    }

    @Redirect(
            method = "shoot",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/tacz/guns/entity/shooter/ShooterDataHolder;sprintTimeS:F",
                    opcode = Opcodes.GETFIELD
            ),
            remap = false
    )
    public float redirectSprintTimeS(ShooterDataHolder holder) {
        float sprintTime = holder.sprintTimeS;
        if (sprintTime > 0) {
            /*开枪时取消疾跑*/
            shooter.setSprinting(false);
            /*返回0摧毁原条件判断*/
            return 0;
        }
        /*其他情况返回原值保持原有逻辑*/
        return sprintTime;
    }
//    @ModifyConstant(
//            method = "shoot",
//            constant = @Constant(
//                    floatValue = 0,
//                    ordinal = 8
//            )
//    )
//    private float Taczlzbs$shoot(float original){
//        return 100000;
//    }

    //    @Inject(
//            method = "shoot",
//            at = @At(value = "RETURN", ordinal = 9),
//            remap = false,
//            cancellable = true
//    )
//    public void Taczlzbs$shoot(Supplier<Float> pitch, Supplier<Float> yaw, long timestamp, CallbackInfoReturnable<ShootResult> cir) {
//        //shooter.setSprinting(false);
//        if (cir.getReturnValue() == ShootResult.IS_SPRINTING) {
//            cir.setReturnValue(ShootResult.SUCCESS);
//            //cir.cancel();
//        }
//    }
//    @Inject(
//        method = "shoot",
//        at = @At(
//                value = "JUMP",
//                opcode = Opcodes.IFLE,
//                ordinal = 2
//        ),
//            remap = false
//    )
//    public void forceJumpReturn(CallbackInfoReturnable<ShootResult> cir){
//        //shooter.setSprinting(false);
//    }
}
