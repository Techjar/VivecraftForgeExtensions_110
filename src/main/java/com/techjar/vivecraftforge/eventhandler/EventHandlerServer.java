package com.techjar.vivecraftforge.eventhandler;

import com.techjar.vivecraftforge.Config;
import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.network.packet.PacketUberPacket;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.Map;
import java.util.UUID;

public class EventHandlerServer {
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			int viewDist = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getViewDistance();
			float range = MathHelper.clamp(viewDist / 8.0F, 1.0F, 2.5F) * 64.0F; // This is how the client determines entity render distance
			for (Map.Entry<UUID, VRPlayerData> entry : PlayerTracker.players.entrySet()) {
				Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(entry.getKey());
				if (entity != null) {
					PacketUberPacket packet = PlayerTracker.getPlayerDataPacket(entry.getKey());
					NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, range);
					VivecraftForge.packetPipeline.sendToAllAround(packet, point);
				}
			}
		}
	}

	@SubscribeEvent
	public void onAttackEntity(AttackEntityEvent event) {
		if (event.getTarget() instanceof EntityPlayer) {
			EntityPlayer player = event.getEntityPlayer();
			EntityPlayer target = (EntityPlayer)event.getTarget();
			if (PlayerTracker.hasPlayerData(player)) {
				VRPlayerData data = PlayerTracker.getPlayerData(player);
				if (data.seated) { // Seated VR vs...
					if (PlayerTracker.hasPlayerData(target)) {
						VRPlayerData targetData = PlayerTracker.getPlayerData(player);
						if (targetData.seated) { // ...seated VR
							if (!Config.seatedVrVsSeatedVR) event.setCanceled(true);
						} else { // ...VR
							if (!Config.vrVsSeatedVR) event.setCanceled(true);
						}
					} else { // ...non-VR
						if (!Config.seatedVrVsNonVR) event.setCanceled(true);
					}
				} else { // VR vs...
					if (PlayerTracker.hasPlayerData(target)) {
						VRPlayerData targetData = PlayerTracker.getPlayerData(player);
						if (targetData.seated) { // ...seated VR
							if (!Config.vrVsSeatedVR) event.setCanceled(true);
						} else { // ...VR
							if (!Config.vrVsVR) event.setCanceled(true);
						}
					} else { // ...non-VR
						if (!Config.vrVsNonVR) event.setCanceled(true);
					}
				}
			} else { // Non-VR vs...
				if (PlayerTracker.hasPlayerData(target)) {
					VRPlayerData targetData = PlayerTracker.getPlayerData(player);
					if (targetData.seated) { // ...seated VR
						if (!Config.seatedVrVsNonVR) event.setCanceled(true);
					} else { // ...VR
						if (!Config.vrVsNonVR) event.setCanceled(true);
					}
				}
			}
		}
	}
}
