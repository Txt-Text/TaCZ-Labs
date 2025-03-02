package com.txttext.taczlabs.mixin;

import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.entity.ReloadState;
import com.tacz.guns.client.gameplay.LocalPlayerDataHolder;
import com.tacz.guns.client.gameplay.LocalPlayerSprint;
import com.tacz.guns.entity.shooter.ShooterDataHolder;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayerSprint.class)
public abstract class LocalPlayerSprintMixin {
    @Shadow
    private final LocalPlayerDataHolder data;
    @Shadow
    private final LocalPlayer player;
    public LocalPlayerSprintMixin(LocalPlayerDataHolder data, LocalPlayer player) {
        this.data = data;
        this.player = player;
    }

    @Inject(
            method = "getProcessedSprintStatus",
            at = @At(value = "RETURN", ordinal = 1),
            remap = false,
            cancellable = true
    )
    public void Taczlzbs$getProcessedSprintStatus(boolean sprinting, CallbackInfoReturnable<Boolean> cir){
        IGunOperator gunOperator = IGunOperator.fromLivingEntity(player);
            if(gunOperator.getSynShootCoolDown() > 0){
                cir.setReturnValue(false);
            }
        }
//    @Inject(
//            method = "getProcessedSprintStatus",
//            at = @At(value = "HEAD"),
//            remap = false,
//            cancellable = true
//    )
//    // 这里的逻辑应该严格与服务端对应，如果不对应，会出现客户端表现和服务端不符的情况。
//    // （例如客户端的视觉效果是玩家在冲刺，而服务端玩家实际上没有冲刺）
//    public void Taczlzbs$getProcessedSprintStatus(boolean sprinting, CallbackInfoReturnable<Boolean> cir){
//        IGunOperator gunOperator = IGunOperator.fromLivingEntity(player);
//        ReloadState.StateType reloadStateType = gunOperator.getSynReloadState().getStateType();
//            if (gunOperator.getSynIsAiming()
//                    || (reloadStateType.isReloading() && !reloadStateType.isReloadFinishing())
//                    || gunOperator.getSynShootCoolDown() >0
//            ) {
//                cir.setReturnValue(false);
//        } else {
//            cir.setReturnValue(sprinting);
//        }
}
