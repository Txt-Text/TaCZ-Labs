package com.txttext.taczlabs.util;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class FallTracker {

    // 每个玩家的下落信息（tick 数、起始 Y 坐标）
    private static final Map<UUID, FallInfo> playerFallInfo = new HashMap<>();

    public static class FallInfo {
        public int ticksFalling;
        public double startY;
        public double currentY;

        public double getFallDistance() {
            return startY - currentY;
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.level().isClientSide() || event.phase != TickEvent.Phase.END) return;

        UUID uuid = player.getUUID();
        double y = player.getY();
        boolean isFalling = !player.onGround() && player.getDeltaMovement().y < 0;

        FallTracker.updateFall(uuid, y, isFalling);
    }

    public static void updateFall(UUID uuid, double currentY, boolean isFalling) {
        if (isFalling) {
            FallInfo info = playerFallInfo.getOrDefault(uuid, new FallInfo());
            if (info.ticksFalling == 0) {
                info.startY = currentY;
            }
            info.ticksFalling++;
            info.currentY = currentY;
            playerFallInfo.put(uuid, info);
        } else {
            playerFallInfo.remove(uuid);
        }
    }

    public static FallInfo getFallInfo(UUID uuid) {
        return playerFallInfo.get(uuid);
    }

    public static boolean isFalling(UUID uuid) {
        return playerFallInfo.containsKey(uuid);
    }
}

