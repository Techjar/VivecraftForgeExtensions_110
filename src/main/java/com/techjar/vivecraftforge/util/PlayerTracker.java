package com.techjar.vivecraftforge.util;

import com.techjar.vivecraftforge.network.packet.PacketController0Data;
import com.techjar.vivecraftforge.network.packet.PacketController1Data;
import com.techjar.vivecraftforge.network.packet.PacketHeadData;
import com.techjar.vivecraftforge.network.packet.PacketUberPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.players.PlayerList;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerTracker {
	public static Map<UUID, VRPlayerData> players = new ConcurrentHashMap<>();
	public static Set<UUID> nonvrPlayers = ConcurrentHashMap.newKeySet();

	public static void tick() {
		PlayerList playerList = ServerLifecycleHooks.getCurrentServer().getPlayerList();
		for (Iterator<Map.Entry<UUID, VRPlayerData>> it = players.entrySet().iterator(); it.hasNext();) {
			Map.Entry<UUID, VRPlayerData> entry = it.next();
			Entity entity = playerList.getPlayer(entry.getKey());
			if (entity == null) {
				it.remove();
			}
		}
		for (Iterator<UUID> it = nonvrPlayers.iterator(); it.hasNext();) {
			UUID uuid = it.next();
			Entity entity = playerList.getPlayer(uuid);
			if (entity == null) {
				it.remove();
			}
		}
	}

	public static VRPlayerData getPlayerData(Player entity, boolean createIfMissing) {
		VRPlayerData data = players.get(entity.getGameProfile().getId());
		if (data == null && createIfMissing) {
			data = new VRPlayerData();
			players.put(entity.getGameProfile().getId(), data);
		}
		return data;
	}

	public static VRPlayerData getPlayerData(Player entity) {
		return getPlayerData(entity, false);
	}

	public static VRPlayerData getPlayerDataAbsolute(Player entity) {
		VRPlayerData data = getPlayerData(entity);
		if (data == null)
			return null;

		VRPlayerData absData = new VRPlayerData();

		absData.handsReversed = data.handsReversed;
		absData.worldScale = data.worldScale;
		absData.seated = data.seated;
		absData.freeMove = data.freeMove;
		absData.bowDraw = data.bowDraw;
		absData.height = data.height;
		absData.activeHand = data.activeHand;
		absData.crawling = data.crawling;

		absData.head.setPos(data.head.getPos().add(entity.position()).add(data.offset));
		absData.head.setRot(data.head.getRot());
		absData.controller0.setPos(data.controller0.getPos().add(entity.position()).add(data.offset));
		absData.controller0.setRot(data.controller0.getRot());
		absData.controller1.setPos(data.controller1.getPos().add(entity.position()).add(data.offset));
		absData.controller1.setRot(data.controller1.getRot());

		return absData;
	}

	public static boolean hasPlayerData(Player entity) {
		return players.containsKey(entity.getGameProfile().getId());
	}

	public static PacketUberPacket getPlayerDataPacket(UUID uuid, VRPlayerData data) {
		PacketHeadData headData = new PacketHeadData(data.seated, (float)data.head.posX, (float)data.head.posY, (float)data.head.posZ, data.head.rotW, data.head.rotX, data.head.rotY, data.head.rotZ);
		PacketController0Data controller0Data = new PacketController0Data(data.handsReversed, (float)data.controller0.posX, (float)data.controller0.posY, (float)data.controller0.posZ, data.controller0.rotW, data.controller0.rotX, data.controller0.rotY, data.controller0.rotZ);
		PacketController1Data controller1Data = new PacketController1Data(data.handsReversed, (float)data.controller1.posX, (float)data.controller1.posY, (float)data.controller1.posZ, data.controller1.rotW, data.controller1.rotX, data.controller1.rotY, data.controller1.rotZ);
		return new PacketUberPacket(uuid, headData, controller0Data, controller1Data, data.worldScale, data.height);
	}

	public static PacketUberPacket getPlayerDataPacket(UUID uuid) {
		VRPlayerData data = players.get(uuid);
		if (data != null) {
			return getPlayerDataPacket(uuid, data);
		}
		return null;
	}

	public static PacketUberPacket getPlayerDataPacket(Player entity) {
		return getPlayerDataPacket(entity.getGameProfile().getId());
	}
}
