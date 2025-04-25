package com.txttext.taczlabs.mixin.sprint_shoot;

import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.client.gameplay.LocalPlayerDataHolder;
import com.tacz.guns.client.gameplay.LocalPlayerSprint;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayerSprint.class)
@SuppressWarnings("all")
public abstract class LocalPlayerSprintMixin {
    @Shadow(remap = false)
    private final LocalPlayerDataHolder data;
    @Shadow(remap = false)
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
            /*射击时不展示冲刺动画，防止动画错误*/
            if(gunOperator.getSynShootCoolDown() > 0){
                cir.setReturnValue(false);
            }
        }
}
