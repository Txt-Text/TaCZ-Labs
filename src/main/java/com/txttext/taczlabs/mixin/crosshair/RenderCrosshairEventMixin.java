package com.txttext.taczlabs.mixin.crosshair;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.client.event.RenderCrosshairEvent;
import com.tacz.guns.resource.index.CommonGunIndex;
import com.txttext.taczlabs.config.fileconfig.HudConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

import static com.txttext.taczlabs.config.fileconfig.HudConfig.*;
import static com.txttext.taczlabs.hud.crosshair.CrosshairRender.*;

@Mixin(RenderCrosshairEvent.class)
public class RenderCrosshairEventMixin {
    @Unique private static float taczlabs$lastSpread = 0f;//保存上一tick的 spread
    @Unique private static float taczlabs$space;
    
    @Inject(
            method = "renderCrosshair",
            at = @At(value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/Window;getGuiScaledWidth()I"),
            remap = false,
            cancellable = true
    )
    private static void renderTlCrosshair(GuiGraphics graphics, Window window, CallbackInfo ci){
        //确认配置开启
        if(!HudConfig.ENABLE_TL_CROSSHAIR.get()) return;
        //获取玩家实例
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) return;//玩家无效则返回

        //获取GUI信息
        int width = window.getGuiScaledWidth();
        int height = window.getGuiScaledHeight();
        float x = width / 2f;
        float y = height / 2f;
        /*获取玩家速度*/
        //PlayerMovementHelper.MovementInfo info = PlayerMovementHelper.getMovementInfo(player);
        //double speed = info.speedPerTick;//获取速度向量（三方向速度）
        Vec3 velocity = player.getDeltaMovement();
        float speed = (float) velocity.horizontalDistance();//只要XZ平面速度
        /*计算当前目标 spread（准星扩张大小）*/
        //根据玩家状态量化准星扩张（不同状态有不同的散布）
        //ShooterDataHolder data = new ShooterDataHolder();
        taczlabs$space = HudConfig.crosshairRadius.get().floatValue();//获取配置值
        if (player.isCrouching()){
            taczlabs$space *= 0.5f;
        }
//        float baseSpread = 0f; // 默认站立状态的 spread
//        /*if (data.isCrawling) {//趴下状态
//            baseSpread = 0.5f;
//        } else */if (player.isCrouching()) {//潜行状态
//            baseSpread = - 0.7f;
//        }

        //因素乘起来的量化
        float t = Mth.clamp(speed, 0f, 1.0f);//阈值
        //非线性缩放（调参）
        float targetSpread = t * 30f;//加上量化
        /*
        * 一个建议优化（可选）
目前 t 是速度的线性映射，如果你想让玩家稍微一动准星就开始放大，而不是完全线性递增，可以试试：

float t = Mth.clamp(speed * speed, 0f, 1.0f); // 二次曲线，微动也扩散
或者更灵活一点：

float t = Mth.clamp((float) Math.pow(speed, 1.5), 0f, 1.0f); // 曲线自由调
        * */
        //平滑过渡（Lerp插值）
        float alpha = 0.1f;//越小越平滑
        float spread = Mth.lerp(alpha, taczlabs$lastSpread, targetSpread);
        //更新保存的值
        taczlabs$lastSpread = spread;

        //获取玩家手持物品，根据枪的种类判断要渲染的准星
        ItemStack gun = player.getMainHandItem();//获取ItemStack
        //由于tacz前面已经检查过，这里不再检查是否为枪
        IGun igun = (IGun) gun.getItem();//转化为Item
        Optional<CommonGunIndex> gunIndex = TimelessAPI.getCommonGunIndex(igun.getGunId(gun));//获取枪械Index
        if (gunIndex.isEmpty()) return;
        String type = gunIndex.get().getType();//获取枪械类型
        switch (type){
            //手枪
            case "pistol"-> {
                renderCrosshairType(pistolCrosshair.get(), x, y, spread);
                ci.cancel();
            }
            //冲锋枪
            case "smg"-> {
                renderCrosshairType(smgCrosshair.get(), x, y, spread);
                ci.cancel();
            }
            //步枪
            case "rifle"-> {
                renderCrosshairType(rifleCrosshair.get(), x, y, spread);
                ci.cancel();
            }
            //机枪
            case "mg"-> {
                renderCrosshairType(mgCrosshair.get(), x, y, spread);
                ci.cancel();
            }
            //霰弹枪（如果可以，我会把霰弹枪的圆形准星做进去...）
            case "shotgun"-> {
                renderCrosshairType(shotgunCrosshair.get(), x, y, spread);
                ci.cancel();
            }
            //狙击枪
            case "sniper"-> {
                renderCrosshairType(sniperCrosshair.get(), x, y, spread);
                ci.cancel();
            }
            //重武器
            case "rpg"-> {
                renderCrosshairType(rpgCrosshair.get(), x, y, spread);
                ci.cancel();
            }
            //未知情况渲染常规准星
            default-> {
                drawCrosshair(x, y, spread);//绘制十字准星
                ci.cancel();
            }
            //其余情况渲染TaCZ准星
        }
        //ci.cancel();
    }
    //小十字点状准星
//      taczlabs$drawRect(x - 0.5F, y - 1.0F, x + 0.5F, y + 1.0F, 0xE6FFFFFF);
//      taczlabs$drawRect(x - 1.0F, y - 0.5F, x + 1.0F, y + 0.5F, 0xE6FFFFFF);

    //给命中叉擦屁股，爆头时由于setShaderColor之后没改回来导致我的准星（所有HUD）也红了
    @Inject(
            method = "renderHitMarker",
            at = @At(
                    value = "TAIL"
            ),
            remap = false
    )
    private static void taczlabs$resetShaderColor(GuiGraphics graphics, Window window, CallbackInfo ci){
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
    }
}
