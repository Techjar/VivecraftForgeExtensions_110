package com.techjar.vivecraftforge.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class ASMDelegator {
	private ASMDelegator() {
	}

	public static double playerEntityReachDistance(EntityPlayer player, double originalValue) { // TODO
		//return originalValue < 256 * 256 && ProxyServer.isVRPlayer(player) ? 256 * 256 : originalValue;
		return 0;
	}

	public static double playerBlockReachDistance(EntityPlayer player, double originalValue) { // TODO
		//return originalValue < 256 && ProxyServer.isVRPlayer(player) ? 256 : originalValue;
		return 0;
	}

	public static double creeperSwellDistance(double originalValue, EntityLivingBase entity) { // TODO
		if (entity == null || !(entity instanceof EntityPlayer)) return originalValue;
		//if (ProxyServer.isVRPlayer((EntityPlayer)entity) && !ProxyServer.getVRPlayerSeated((EntityPlayer)entity)) return 1.75D * 1.75D;
		return originalValue;
	}

	public static Vec3d endermanLook(Vec3d origLook, EntityPlayer player) { // TODO
		/*if (ProxyServer.isVRPlayer(player)) {
			VRPlayerData data = ProxyServer.vrPlayers.get(player);
			if (data.entities.size() > 0) {
				EntityVRObject entity = data.entities.get(0);
				ServerQuaternion quat = new ServerQuaternion(entity.rotW, entity.rotX, entity.rotY, entity.rotZ);
				return quat.multiply(Vec3.createVectorHelper(0, 0, -1));
			}
		}*/
		return origLook;
	}
}
