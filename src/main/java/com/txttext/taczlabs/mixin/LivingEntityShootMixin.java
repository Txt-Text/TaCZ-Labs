package com.txttext.taczlabs.mixin;

import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.entity.ShootResult;
import com.tacz.guns.api.event.common.GunShootEvent;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.gun.AbstractGunItem;
import com.tacz.guns.entity.shooter.LivingEntityDrawGun;
import com.tacz.guns.entity.shooter.LivingEntityShoot;
import com.tacz.guns.entity.shooter.ShooterDataHolder;
import com.tacz.guns.network.NetworkHandler;
import com.tacz.guns.network.message.event.ServerMessageGunShoot;
import com.tacz.guns.resource.index.CommonGunIndex;
import com.tacz.guns.resource.pojo.data.gun.Bolt;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@Mixin(LivingEntityShoot.class)
public abstract class LivingEntityShootMixin {
    /*target是控制服务端开火事件的类*/
    @Shadow(remap = false)
    private final LivingEntity shooter;
    @Shadow(remap = false)
    private final ShooterDataHolder data;
    @Shadow(remap = false)
    private final LivingEntityDrawGun draw;
//    @Shadow
//    public abstract ShootResult shoot(Supplier<Float> pitch, Supplier<Float> yaw, long timestamp);

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
            shooter.setSprinting(false);
            return 0.0F;
        }
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
