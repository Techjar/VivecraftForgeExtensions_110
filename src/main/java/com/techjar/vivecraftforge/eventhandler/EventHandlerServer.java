package com.techjar.vivecraftforge.eventhandler;

import com.techjar.vivecraftforge.Config;
import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.network.packet.PacketUberPacket;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.Util;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class EventHandlerServer {
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			PlayerTracker.tick();
			int viewDist = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getViewDistance();
			float range = MathHelper.clamp(viewDist / 8.0F, 1.0F, 2.5F) * 64.0F; // This is how the client determines entity render distance
			for (Map.Entry<UUID, VRPlayerData> entry : PlayerTracker.players.entrySet()) {
				Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(entry.getKey());
				if (entity != null) {
					PacketUberPacket packet = PlayerTracker.getPlayerDataPacket(entry.getKey(), entry.getValue());
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
						VRPlayerData targetData = PlayerTracker.getPlayerData(target);
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
						VRPlayerData targetData = PlayerTracker.getPlayerData(target);
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
					VRPlayerData targetData = PlayerTracker.getPlayerData(target);
					if (targetData.seated) { // ...seated VR
						if (!Config.seatedVrVsNonVR) event.setCanceled(true);
					} else { // ...VR
						if (!Config.vrVsNonVR) event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onArrowLoose(ArrowLooseEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		VRPlayerData data = PlayerTracker.getPlayerData(player);
		if (data != null && !data.seated) {
			event.setCharge(Math.round(data.bowDraw * 20));
		}
	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		EntityLivingBase target = event.getEntityLiving();
		DamageSource source = event.getSource();
		if (source.getImmediateSource() instanceof EntityArrow && source.getTrueSource() instanceof EntityPlayer) {
			EntityArrow arrow = (EntityArrow)source.getImmediateSource();
			EntityPlayer attacker = (EntityPlayer)source.getTrueSource();
			if (PlayerTracker.hasPlayerData(attacker)) {
				VRPlayerData data = PlayerTracker.getPlayerData(attacker);
				boolean headshot = Util.isHeadshot(target, arrow);
				if (data.seated) {
					if (headshot) event.setAmount(event.getAmount() * Config.bowSeatedHeadshotMul);
					else event.setAmount(event.getAmount() * Config.bowSeatedMul);
				} else {
					if (headshot) event.setAmount(event.getAmount() * Config.bowStandingHeadshotMul);
					else event.setAmount(event.getAmount() * Config.bowStandingMul);
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof EntityPlayerMP) {
			final EntityPlayerMP player = (EntityPlayerMP)event.getEntity();
			if (Config.vrOnly) {
				player.sendMessage(new TextComponentString(Config.vrOnlyKickMessage));
				player.sendMessage(new TextComponentString("You will be kicked in " + Config.vrOnlyKickDelay + " seconds."));
				Util.scheduler.schedule(new Runnable() {
					@Override
					public void run() {
						FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
							@Override
							public void run() {
								if (player.connection.getNetworkManager().isChannelOpen()) {
									player.connection.disconnect(new TextComponentString(Config.vrOnlyKickMessage));
								}
							}
						});
					}
				}, Math.round(Config.vrOnlyKickDelay * 1000), TimeUnit.MILLISECONDS);
			}
		}
	}
}
