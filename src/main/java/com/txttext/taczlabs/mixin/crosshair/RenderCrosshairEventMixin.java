package com.txttext.taczlabs.mixin.crosshair;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.client.event.RenderCrosshairEvent;
import com.tacz.guns.client.resource.index.ClientGunIndex;
import com.txttext.taczlabs.hud.crosshair.Crosshair;
import com.txttext.taczlabs.hud.crosshair.CrosshairType;
import com.txttext.taczlabs.config.fileconfig.HudConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

import static com.txttext.taczlabs.config.fileconfig.HudConfig.*;

@Mixin(RenderCrosshairEvent.class)
public class RenderCrosshairEventMixin {
    @Inject(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/Window;getGuiScaledWidth()I"),
            //remap = false,
            cancellable = true
    )
    private static void renderTlCrosshair(GuiGraphics graphics, Window window, CallbackInfo ci){
        //确认配置开启
        if(!HudConfig.ENABLE_TL_CROSSHAIR.get()) return;
        //获取GUI信息
        int width = window.getGuiScaledWidth();
        int height = window.getGuiScaledHeight();
        float x = width / 2f;
        float y = height / 2f;
        //获取玩家实例
        LocalPlayer player = Minecraft.getInstance().player;//客户端的准星获取客户端实体就够了
        if (player == null) return;//玩家无效则返回
        IGunOperator gunOperator = IGunOperator.fromLivingEntity(player);
        //在瞄准时不进行计算
        if(gunOperator.getSynIsAiming()) return;
        //获取玩家手持物品，根据枪的种类判断要渲染的准星
        ItemStack gun = player.getMainHandItem();//获取ItemStack
        //由于tacz前面已经检查过，这里转化时不再检查是否为枪
        IGun igun = (IGun) gun.getItem();//转化为Item
        Optional<ClientGunIndex> gunIndex = TimelessAPI.getClientGunIndex(igun.getGunId(gun));//获取枪械Index
        if (gunIndex.isEmpty()) return;
        ClientGunIndex gunIndex1 = gunIndex.get();
        String type = gunIndex1.getType();//获取枪械类型
        //float inaccuracy = gunIndex1.getGunData().getInaccuracy(InaccuracyType.getInaccuracyType(player));//获取扩散值
        CrosshairType currentType = switch (type){//获取当前手持武器类型
            case "pistol"-> pistolCrosshair.get();//手枪
            case "smg"-> smgCrosshair.get();//冲锋枪 TODO：做出一个这样的括号准星给冲锋枪：( · )
            //case "rifle"-> rifleCrosshair.get();//步枪
            case "mg"-> mgCrosshair.get();//机枪
            case "shotgun"-> shotgunCrosshair.get();//霰弹枪 TODO：把霰弹枪的圆形准星做进去
            case "sniper"-> sniperCrosshair.get();//狙击枪
            case "rpg"-> rpgCrosshair.get();//重武器
            default-> rifleCrosshair.get();//步枪和未知情况，未知情况按说不可能出现
        };
        if(currentType == CrosshairType.TACZ) return;//原版准星直接渲染原版tacz
        Crosshair.renderCrosshair(graphics, currentType, x, y, gunIndex1, player);//渲染准星
        ci.cancel();
    }

    //给命中叉擦屁股，爆头时由于 setShaderColor 之后没改回来导致我的准星包括所有 HUD 也会一起变红
    @Inject(
            method = "renderHitMarker",
            at = @At(value = "TAIL"),
            remap = false
    )
    private static void taczlabs$resetShaderColor(GuiGraphics graphics, Window window, CallbackInfo ci){
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);//在渲染完成后重置渲染器颜色
    }
}
