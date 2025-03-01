package com.txttext.taczlabs.mixin;

import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.entity.ShootResult;
import com.tacz.guns.client.gameplay.LocalPlayerDataHolder;
import com.tacz.guns.client.gameplay.LocalPlayerShoot;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(LocalPlayerShoot.class)
public class LocalPlayerShootMixin {
    /*target是控制客户端开火事件的类*/

    @Shadow
    private final LocalPlayerDataHolder data;
    @Shadow
    private final LocalPlayer player;

    public LocalPlayerShootMixin(LocalPlayerDataHolder data, LocalPlayer player) {
        this.data = data;
        this.player = player;
    }

    @Inject(
            method = "shoot",
            at = @At(value = "RETURN", ordinal = 12),
            remap = false,
            cancellable = true
    )
    public void Taczlzbs$shoot(CallbackInfoReturnable<ShootResult> cir){
        if (cir.getReturnValue() == ShootResult.IS_SPRINTING) {
            player.setSprinting(false);
            cir.setReturnValue(ShootResult.SUCCESS);
            cir.cancel();
        }
    }
}
