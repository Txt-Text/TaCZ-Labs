package com.txttext.taczlabs.util;

import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.client.resource.index.ClientGunIndex;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;

public class TLUtil {

    @Nullable
    public static GunData getGunData(ItemStack gun){
        if(gun.getItem() instanceof IGun iGun){
            return TimelessAPI.getClientGunIndex(iGun.getGunId(gun)).get().getGunData();
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static ClientGunIndex getClientGunIndex(ItemStack gun){
        if(gun.getItem() instanceof IGun iGun){
            return TimelessAPI.getClientGunIndex(iGun.getGunId(gun)).get();
        }
        return null;
    }
}
