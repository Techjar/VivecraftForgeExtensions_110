package com.techjar.vivecraftforge.util;

import com.techjar.vivecraftforge.network.packet.PacketController0Data;
import com.techjar.vivecraftforge.network.packet.PacketController1Data;
import com.techjar.vivecraftforge.network.packet.PacketHeadData;
import com.techjar.vivecraftforge.network.packet.PacketUberPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerTracker {
	public static Map<UUID, VRPlayerData> players = new HashMap<UUID, VRPlayerData>();
	public static Set<UUID> companionPlayers = new HashSet<UUID>();

	public static void tick() {
		for (Iterator<Map.Entry<UUID, VRPlayerData>> it = players.entrySet().iterator(); it.hasNext();) {
			Map.Entry<UUID, VRPlayerData> entry = it.next();
			Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(entry.getKey());
			if (entity == null) {
				it.remove();
			}
		}
		for (Iterator<UUID> it = companionPlayers.iterator(); it.hasNext();) {
			UUID uuid = it.next();
			Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(uuid);
			if (entity == null) {
				it.remove();
			}
		}
	}

	public static VRPlayerData getPlayerData(EntityPlayer entity, boolean createIfMissing) {
		VRPlayerData data = players.get(entity.getGameProfile().getId());
		if (data == null && createIfMissing) {
			data = new VRPlayerData();
			players.put(entity.getGameProfile().getId(), data);
		}
		return data;
	}

	public static VRPlayerData getPlayerData(EntityPlayer entity) {
		return getPlayerData(entity, false);
	}

	public static boolean hasPlayerData(EntityPlayer entity) {
		return players.containsKey(entity.getGameProfile().getId());
	}

	public static PacketUberPacket getPlayerDataPacket(UUID uuid, VRPlayerData data) {
		PacketHeadData headData = new PacketHeadData(data.seated, data.head.posX, data.head.posY, data.head.posZ, data.head.rotW, data.head.rotX, data.head.rotY, data.head.rotZ);
		PacketController0Data controller0Data = new PacketController0Data(data.handsReversed, data.controller0.posX, data.controller0.posY, data.controller0.posZ, data.controller0.rotW, data.controller0.rotX, data.controller0.rotY, data.controller0.rotZ);
		PacketController1Data controller1Data = new PacketController1Data(data.handsReversed, data.controller1.posX, data.controller1.posY, data.controller1.posZ, data.controller1.rotW, data.controller1.rotX, data.controller1.rotY, data.controller1.rotZ);
		return new PacketUberPacket(uuid, headData, controller0Data, controller1Data);
	}

	public static PacketUberPacket getPlayerDataPacket(UUID uuid) {
		VRPlayerData data = players.get(uuid);
		if (data != null) {
			return getPlayerDataPacket(uuid, data);
		}
		return null;
	}

	public static PacketUberPacket getPlayerDataPacket(EntityPlayer entity) {
		VRPlayerData data = players.get(entity.getGameProfile().getId());
		if (data != null) {
			getPlayerDataPacket(entity.getGameProfile().getId(), data);
		}
		return null;
	}
}
