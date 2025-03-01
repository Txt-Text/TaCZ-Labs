package com.txttext.taczlabs.mixin;

import com.tacz.guns.api.entity.ShootResult;
import com.tacz.guns.entity.shooter.LivingEntityDrawGun;
import com.tacz.guns.entity.shooter.LivingEntityShoot;
import com.tacz.guns.entity.shooter.ShooterDataHolder;
import net.minecraft.world.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.xml.crypto.Data;
import java.util.function.Supplier;

@Mixin(LivingEntityShoot.class)
public class LivingEntityShootMixin {
    /*target是控制服务端开火事件的类*/

    @Shadow
    private final LivingEntity shooter;
    @Shadow
    private final ShooterDataHolder data;
    @Shadow
    private final LivingEntityDrawGun draw;
    public LivingEntityShootMixin(LivingEntity shooter, ShooterDataHolder data, LivingEntityDrawGun draw) {
        this.shooter = shooter;
        this.data = data;
        this.draw = draw;
    }

    @Inject(
            method = "shoot",
            at = @At(value = "RETURN", ordinal = 9),
            remap = false,
            cancellable = true
    )

    public void Taczlzbs$shoot(Supplier<Float> pitch, Supplier<Float> yaw, long timestamp, CallbackInfoReturnable<ShootResult> cir){
        if (cir.getReturnValue() == ShootResult.IS_SPRINTING) {
            shooter.setSprinting(false);
            cir.setReturnValue(ShootResult.SUCCESS);
            cir.cancel();
        }
    }


}
