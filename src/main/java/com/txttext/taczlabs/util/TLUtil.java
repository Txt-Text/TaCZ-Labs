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

    ///获取解包 Optional 后的的枪械 Index
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static ClientGunIndex getClientGunIndex(ItemStack gun){
        if(gun.getItem() instanceof IGun iGun){
            Optional<ClientGunIndex> clientGunIndex = TimelessAPI.getClientGunIndex(iGun.getGunId(gun));
            return clientGunIndex.orElse(null);
        }
        return null;
    }

    /// 获取枪械 Data
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static GunData getGunData(ItemStack gun){
        ClientGunIndex clientGunIndex = getClientGunIndex(gun);
        return clientGunIndex != null ? clientGunIndex.getGunData() : null;//判null
    }


}
