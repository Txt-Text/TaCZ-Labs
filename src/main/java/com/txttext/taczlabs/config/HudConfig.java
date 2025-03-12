package com.txttext.taczlabs.config;

import com.txttext.taczlabs.hud.crosshair.SprintingCrosshair;
import net.minecraftforge.common.ForgeConfigSpec;
/**客户端的 HUD 配置*/
public class HudConfig {
    public static ForgeConfigSpec.BooleanValue ENABLE_TL_CROSSHAIR;
    public static ForgeConfigSpec.EnumValue<SprintingCrosshair> CROSSHAIR_STATUS_DURING_SPRINTING;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("HUD");//builder.comment("");

        /*启用tl的新准星*/
        ENABLE_TL_CROSSHAIR = builder
                .comment("New Crosshair of TaCZ:Labs")
                .define("EnableTLCrosshair", true);

        /*冲刺时准星的显示状态*/
        CROSSHAIR_STATUS_DURING_SPRINTING = builder
                .comment("""
                        Control crosshair display status during sprinting:
                        TL_CROSSHAIR: TaCZ:Labs crosshair(request enable "Enable TL crosshair")
                        TACZ_VANILLA: TaCZ vanilla crosshair(disabled "Enable TL crosshair")
                        POINT: Middle point
                        HIDE: Hide crosshair
                        """)
                .defineEnum("SprintingCrosshair", SprintingCrosshair.TL_CROSSHAIR);//默认显示tl的新准星，改之后影响HudClothConfig的默认值
        builder.pop();
    }
}
