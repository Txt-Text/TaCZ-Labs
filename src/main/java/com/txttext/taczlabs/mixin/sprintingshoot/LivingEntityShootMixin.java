package com.txttext.taczlabs.mixin.sprintingshoot;

import com.tacz.guns.entity.shooter.LivingEntityDrawGun;
import com.tacz.guns.entity.shooter.LivingEntityShoot;
import com.tacz.guns.entity.shooter.ShooterDataHolder;
import com.txttext.taczlabs.config.fileconfig.FunctionConfig;
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
        //没有启用跑射则正常执行逻辑
        if (!FunctionConfig.ENABLE_SPRINTING_SHOOT.get()) return holder.sprintTimeS;
        //开枪时取消疾跑（需要启用“修复跑射”配置）
        shooter.setSprinting(false);
        //PlayerFireHandler.lastShootTime = System.currentTimeMillis();
        return 0;//返回0摧毁原条件判断
    }
}
