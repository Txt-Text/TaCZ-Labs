//package com.txttext.taczlabs.mixin.Crosshair;
//
//import net.minecraft.client.gui.Gui;
//import net.minecraft.client.gui.GuiGraphics;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(Gui.class)
//public class GuiMixin {
//    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
//    private void disableVanillaCrosshair(GuiGraphics guiGraphics, CallbackInfo ci) {
//        ci.cancel();
////        if (Config.disableVanillaCrosshair.get()) {
////            ci.cancel();
////        }
//    }
//}
