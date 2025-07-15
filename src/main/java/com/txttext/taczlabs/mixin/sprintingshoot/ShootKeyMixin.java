package com.txttext.taczlabs.mixin.sprintingshoot;

import com.tacz.guns.api.item.IGun;
import com.tacz.guns.client.input.ShootKey;
import com.tacz.guns.util.InputExtraCheck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.tacz.guns.client.input.ShootKey.SHOOT_KEY;
import static com.txttext.taczlabs.event.shoot.ShootKeyHandler.isMouseDown;

@Mixin(ShootKey.class)
public abstract class ShootKeyMixin {

    @Inject(
            method = "semiShoot",
            at = @At("HEAD"),
            remap = false
    )
    private static void onSemiShoot(InputEvent.MouseButton.Post event, CallbackInfo ci) {
//        if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
        if (InputExtraCheck.isInGame()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if(player == null) return;
            Item mainHand = player.getMainHandItem().getItem();
            if(mainHand instanceof IGun && SHOOT_KEY.matchesMouse(event.getButton())){
                isMouseDown = event.getAction() == GLFW.GLFW_PRESS;
                //System.out.println("当前鼠标按下状态：" + isMouseDown);
//            if (!isMouseDown) {
//                //添加客户端状态跟踪
//                long taczlabs$lastReleaseTime = System.currentTimeMillis();
//            }
            }
        }
    }
//    private static boolean isFiring() {
//        //鼠标按下状态，或松开后300ms内仍视为开火状态（用于动画过渡）
//        return isMouseDown/* || (System.currentTimeMillis() - lastReleaseTime < 300)*/;
//    }

}
