package com.txttext.taczlabs.config.fileconfig;

import com.txttext.taczlabs.config.clothconfig.CrosshairType;
import net.minecraftforge.common.ForgeConfigSpec;
/**客户端的 HUD 配置*/
public class HudConfig {
    public static ForgeConfigSpec.BooleanValue ENABLE_TL_CROSSHAIR;//是否启用本模组的准星
    /*全局设置*/
    public static ForgeConfigSpec.IntValue color;//准星颜色值（RGBA HEX）
    public static ForgeConfigSpec.IntValue R;//准星颜色值（RGBA）
    public static ForgeConfigSpec.IntValue G;//准星颜色值（RGBA）
    public static ForgeConfigSpec.IntValue B;//准星颜色值（RGBA）
    public static ForgeConfigSpec.IntValue A;//准星颜色值（RGBA）
    public static ForgeConfigSpec.DoubleValue shadowOffset;//阴影偏移量
    //public static ForgeConfigSpec.DoubleValue shadowWidth;//阴影厚度
    public static ForgeConfigSpec.DoubleValue shadowAlpha;//阴影不透明度
    public static ForgeConfigSpec.DoubleValue crosshairSpread;//准星扩散（十字准星）
    public static ForgeConfigSpec.DoubleValue shootingSpread;//开火扩散
    public static ForgeConfigSpec.EnumValue<CrosshairType> pistolCrosshair;//手枪准星
    public static ForgeConfigSpec.EnumValue<CrosshairType> smgCrosshair;//冲锋枪准星
    public static ForgeConfigSpec.EnumValue<CrosshairType> rifleCrosshair;//步枪准星
    public static ForgeConfigSpec.EnumValue<CrosshairType> mgCrosshair;//机枪准星
    public static ForgeConfigSpec.EnumValue<CrosshairType> shotgunCrosshair;//霰弹枪准星
    public static ForgeConfigSpec.EnumValue<CrosshairType> sniperCrosshair;//狙击枪准星
    public static ForgeConfigSpec.EnumValue<CrosshairType> rpgCrosshair;//重武器准星
    /*局部设置*/
    public static ForgeConfigSpec.DoubleValue crosshairLength;//准星长度（十字准星）
    public static ForgeConfigSpec.DoubleValue crosshairWidth;//准星宽度（十字准星）
    public static ForgeConfigSpec.DoubleValue crosshairRadius;//准星半径（十字准星）
    public static ForgeConfigSpec.DoubleValue rectCrosshairLength;//准星长度（方形准星）
    public static ForgeConfigSpec.DoubleValue rectCrosshairWidth;//准星宽度（方形准星）
    public static ForgeConfigSpec.DoubleValue rectCrosshairRadius;//准星半径（方形准星）
    public static ForgeConfigSpec.DoubleValue rightAngleCrosshairLength;//准星长度（直角准星）
    public static ForgeConfigSpec.DoubleValue rightAngleCrosshairWidth;//准星宽度（直角准星）
    public static ForgeConfigSpec.DoubleValue rightAngleCrosshairRadius;//准星半径（直角准星）


    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("HUD");

        /*是否启用本模组的准星*/
        ENABLE_TL_CROSSHAIR = builder
                .comment("New Crosshair of TaCZ:Labs.")
                .define("Enable Tl Crosshair", true);

        /*全局设置*/
        /*准星颜色值（RGBA）*/
        color = builder
                .comment("Crosshair RGBA HEX color, format is 0xAARRGGBB.")
                .defineInRange("Crosshair Color", 0xE6FFFFCC, Integer.MIN_VALUE, Integer.MAX_VALUE);
        A = builder.defineInRange("A", 0xE6, 0, 255);
        R = builder.defineInRange("R", 0xFF, 0, 255);
        G = builder.defineInRange("G", 0xFF, 0, 255);
        B = builder.defineInRange("B", 0xCC, 0, 255);
        //builder.pop();
//        R = builder
//                .comment("")
//                .defineInRange("", 0, 255);
        /*准星阴影偏移量*/
        shadowOffset = builder
                .comment("Shadow offset of crosshair, range 0.0 ~ 3.0, default 0.5 .")
                .defineInRange("Shadow Offset", 0.5D, 0.0D, 3.0D);

        /*准星动态扩散*/
        crosshairSpread = builder
                .comment("Spread of crosshair, range 0.0 ~ 60.0, default 30.0 .")
                .defineInRange("Crosshair Spread", 30.0D, 0.0D, 60.0D);

        /*准星开火扩散*/
        shootingSpread = builder
                .comment("Spread of crosshair, range 0.0 ~ , default  .")
                .defineInRange("Crosshair Spread", 30.0D, 0.0D, 60.0D);//TODO

        //builder.push("Local");
        /*局部设置*/
        //准星类型
        builder.push("Crosshair Type");
        pistolCrosshair = builder.comment("Crosshair of Pistol").defineEnum("Pistol", CrosshairType.RIGHT_ANGLE);//手枪
        smgCrosshair = builder.comment("Crosshair of Sub-Machine Gun").defineEnum("Sub-Machine Gun", CrosshairType.CROSSHAIR);//冲锋枪
        rifleCrosshair = builder.comment("Crosshair of Rifle").defineEnum("Rifle", CrosshairType.CROSSHAIR);//步枪
        mgCrosshair = builder.comment("Crosshair of Machine Gun").defineEnum("Machine Gun", CrosshairType.CROSSHAIR);//机枪
        sniperCrosshair = builder.comment("Crosshair of Sniper").defineEnum("Sniper", CrosshairType.CROSSHAIR);//狙击枪
        shotgunCrosshair = builder.comment("Crosshair of Shotgun").defineEnum("Shotgun", CrosshairType.RECT);//霰弹枪
        rpgCrosshair = builder.comment("Crosshair of Heavy Weapon").defineEnum("Heavy Weapon", CrosshairType.RECT);//重武器
        builder.pop();

        /*十字准星长度*/
        crosshairLength = builder
                .comment("Length of crosshair, range 0.1 ~ 20.0, default 6.0 .")
                .defineInRange("Crosshair Length", 6.0D, 0.1D, 20.0D);

        crosshairWidth = builder
                .comment("Width of crosshair, range 0.1 ~ 10.0, default 2.0 .")
                .defineInRange("Crosshair Width", 2.0D, 0.1D, 10.0D);

        /*十字准星半径*/
        crosshairRadius = builder
                .comment("Radius of crosshair, range 0.0 ~ 10.0, default 5.0 .")
                .defineInRange("Crosshair Radius", 5.0D, 0.0D, 10.0D);

        /*方形准星长度*/
        rectCrosshairLength = builder
                .comment("Length of rect crosshair, range 0.0 ~ 20.0, default 6.0 .")
                .defineInRange("Rect Crosshair Length", 6.0D, 0.0D, 20.0D);

        rectCrosshairWidth = builder
                .comment("Width of rect crosshair, range 0.0 ~ 10.0, default 1.0 .")
                .defineInRange("Rect Crosshair Width", 1.0D, 0.1D, 10.0D);

        /*方形准星半径*/
        rectCrosshairRadius = builder
                .comment("Radius of rect crosshair, range 0.0 ~ 10.0, default 5.0 .")
                .defineInRange("Rect Crosshair Radius", 5.0D, 0.0D, 10.0D);

        /*直角准星长度*/
        rightAngleCrosshairLength = builder
                .comment("Length of right angle crosshair, range 0.0 ~ 30.0, default 12.0 .")
                .defineInRange("Right Angle Crosshair Length", 12.0D, 0.0D, 30.0D);

        rightAngleCrosshairWidth = builder
                .comment("Width of right angle crosshair, range 0.0 ~ 10.0, default 2.0 .")
                .defineInRange("Right Angle Crosshair Width", 2.0D, 0.1D, 10.0D);

        /*直角准星半径*/
        rightAngleCrosshairRadius = builder
                .comment("Radius of right angle crosshair, range 0.0 ~ 20.0, default 10.0 .")
                .defineInRange("Right Angle Crosshair Radius", 10.0D, 0.0D, 20.0D);
        builder.pop();
    }
}
