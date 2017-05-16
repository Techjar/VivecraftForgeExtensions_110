package com.techjar.vivecraftforge.util;

import com.techjar.vivecraftforge.Config;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class ASMDelegator {
	private ASMDelegator() {
	}

	public static double playerBlockReachDistance(EntityPlayer player, double originalValue) {
		return originalValue < 512 * 512 && PlayerTracker.hasPlayerData(player) ? 512 * 512 : originalValue;
	}

	public static double playerEntityReachDistance(EntityPlayer player, double originalValue) {
		return originalValue < 512 * 512 && PlayerTracker.hasPlayerData(player) ? 512 * 512 : originalValue;
	}

	public static boolean playerEntitySeenOverride(EntityPlayer player, boolean originalValue) {
		return PlayerTracker.hasPlayerData(player) || originalValue;
	}

	public static double creeperSwellDistance(double originalValue, EntityLivingBase entity) {
		if (entity instanceof EntityPlayer) {
			VRPlayerData data = PlayerTracker.getPlayerData((EntityPlayer)entity);
			if (data != null && !data.seated) return Config.creeperSwellDistance * Config.creeperSwellDistance;
		}
		return originalValue;
	}

	public static Vec3d endermanLook(Vec3d originalLook, EntityPlayer player) {
		if (PlayerTracker.hasPlayerData(player)) {
			VRPlayerData data = PlayerTracker.getPlayerData(player);
			Quaternion quat = new Quaternion(data.head.rotW, data.head.rotX, data.head.rotY, data.head.rotZ);
			return quat.multiply(new Vec3d(0, 0, -1));
		}
		return originalLook;
	}

	public static float movedTooQuicklyThreshold(EntityPlayer player, boolean elytra) {
		if (PlayerTracker.hasPlayerData(player))
			return Config.movedTooQuicklyThreshold * Config.movedTooQuicklyThreshold;
		return elytra ? 300 : 100;
	}

	public static double movedWronglyThreshold(EntityPlayer player) {
		if (PlayerTracker.hasPlayerData(player))
			return Config.movedWronglyThreshold * Config.movedWronglyThreshold;
		return 0.0625;
	}
}
