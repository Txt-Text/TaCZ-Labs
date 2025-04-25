package com.txttext.taczlabs.mixin.sprint_shoot;

import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.client.gameplay.LocalPlayerDataHolder;
import com.tacz.guns.client.gameplay.LocalPlayerShoot;
import com.txttext.taczlabs.config.fileconfig.FunctionConfig;
import com.txttext.taczlabs.config.fileconfig.HudConfig;
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
        if (originalTime > 0 && FunctionConfig.ENABLE_SPRINTING_SHOOT.get()) {//需要启用”修复跑射“配置
            //使 originalTime = 0 ，跳过原条件
            return 0;
        }
        //其他情况返回原值保持原有逻辑
        return originalTime;
    }
}
