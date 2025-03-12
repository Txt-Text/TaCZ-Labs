//package com.txttext.taczlabs.event.ammo;
//
//import com.tacz.guns.api.event.server.AmmoHitBlockEvent;
//import com.tacz.guns.config.common.AmmoConfig;
//import com.tacz.guns.entity.EntityKineticBullet;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.AbstractGlassBlock;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.IronBarsBlock;
//import net.minecraft.world.level.block.StainedGlassPaneBlock;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.server.command.ConfigCommand;
//
//@Mod.EventBusSubscriber
//public class DestroyBlock {
//    @SubscribeEvent
//    public static void onAmmoHitBlock(AmmoHitBlockEvent event) {
//        /*Level level = event.getLevel();
//        BlockState state = event.getState();
//        BlockPos pos = event.getHitResult().getBlockPos();
//        EntityKineticBullet ammo = event.getAmmo();
//        Block stateBlock = state.getBlock();
//        Class[] BlockCanBeDestroy = null;
//        if (AmmoConfig.DESTROY_GLASS.get() && (stateBlock instanceof BlockCanBeDestroy))
//        if (AmmoConfig.DESTROY_GLASS.get() && (stateBlock instanceof AbstractGlassBlock)) {
//            level.destroyBlock(pos, false, ammo.getOwner());
//        }*/
//    }
//}
