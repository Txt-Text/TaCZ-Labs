//package com.txttext.taczlabs.util;
//
//import com.tacz.guns.api.TimelessAPI;
//import com.tacz.guns.api.item.IGun;
//import com.tacz.guns.client.resource.index.ClientGunIndex;
//import com.tacz.guns.resource.pojo.data.gun.GunData;
//import net.minecraft.client.player.LocalPlayer;
//import net.minecraft.world.item.ItemStack;
//
//import java.util.Optional;
//
//public class LocalPlayerInfoHelper {
//    private static GunData gunData = null;
//
//    public static void getPlayerGun(LocalPlayer player){
//        //获取玩家手持ItemStack
//        ItemStack gun = player.getMainHandItem();
//        //由于tacz前面已经检查过，这里转化时不再检查
//        IGun igun = (IGun) gun.getItem();//转化为Item
//        Optional<ClientGunIndex> gunIndex = TimelessAPI.getClientGunIndex(igun.getGunId(gun));//获取枪械Index
//    }
//
//    public static void getPlayerGunSafe(LocalPlayer player){
//        //获取玩家手持ItemStack
//        ItemStack gun = player.getMainHandItem();
//        //检查
//        if(!(gun.getItem() instanceof IGun igun)) return;
//
//        Optional<ClientGunIndex> gunIndex = TimelessAPI.getClientGunIndex(igun.getGunId(gun));//获取枪械Index
//
//        gunData = gunIndex.get().getGunData();
//    }
//}
