package com.txttext.taczlabs.mixin.sprintingshoot;

import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.client.gameplay.LocalPlayerDataHolder;
import com.tacz.guns.client.gameplay.LocalPlayerShoot;
import com.txttext.taczlabs.config.fileconfig.FunctionConfig;
import com.txttext.taczlabs.hud.crosshair.Crosshair;
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
//    //添加一个客户端本地的射击状态标记
//    private boolean shootingSprinting = false;

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
        //未开启跑射正常执行逻辑
        if (!FunctionConfig.ENABLE_SPRINTING_SHOOT.get()) return iGunOperator.getSynSprintTime();
        player.setSprinting(false);
        return 0;//使originalTime=0，跳过原条件
    }
}
