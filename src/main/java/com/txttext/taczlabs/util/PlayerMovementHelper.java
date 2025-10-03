//package com.txttext.taczlabs.util;
//
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.phys.Vec3;
//
//public class PlayerMovementHelper {
//
//    public static class MovementInfo {
//        public final double speedPerTick;
//        public final double speedPerSecond;
//        public final boolean isFalling;
//        public final boolean isSprinting;
//        public final boolean isMoving;
//        public final boolean isStill;
//
//        public MovementInfo(Player player) {
//            Vec3 vel = player.getDeltaMovement();
//            this.speedPerTick = vel.length();
//            this.speedPerSecond = speedPerTick * 20;
//
//            this.isFalling = !player.onGround() && vel.y < 0;
//            this.isSprinting = player.isSprinting();
//            this.isMoving = speedPerTick > 0.01;
//            this.isStill = !isMoving;
//        }
//
//        @Override
//        public String toString() {
//            return String.format(
//                    "速度: %.3f blocks/tick (%.2f blocks/sec), 状态: %s%s%s%s",
//                    speedPerTick,
//                    speedPerSecond,
//                    isFalling ? "下落 " : "",
//                    isSprinting ? "疾跑 " : "",
//                    isMoving ? "移动中 " : "",
//                    isStill ? "静止" : ""
//            );
//        }
//    }
//
//    public static MovementInfo getMovementInfo(Player player) {
//        return new MovementInfo(player);
//    }
//}


//        /*获取玩家速度*/
//        //PlayerMovementHelper.MovementInfo info = PlayerMovementHelper.getMovementInfo(player);
//        //double speed = info.speedPerTick;//获取速度向量（三方向速度）
//        Vec3 velocity = player.getDeltaMovement();
//        float speed = (float) velocity.horizontalDistance();//只要XZ平面速度