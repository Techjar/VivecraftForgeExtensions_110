package com.techjar.vivecraftforge.eventhandler;

import com.techjar.vivecraftforge.Config;
import com.techjar.vivecraftforge.entity.ai.goal.VRCreeperSwellGoal;
import com.techjar.vivecraftforge.entity.ai.goal.VREndermanFindPlayerGoal;
import com.techjar.vivecraftforge.entity.ai.goal.VREndermanStareGoal;
import com.techjar.vivecraftforge.network.ChannelHandler;
import com.techjar.vivecraftforge.network.packet.PacketUberPacket;
import com.techjar.vivecraftforge.util.AimFixHandler;
import com.techjar.vivecraftforge.util.LogHelper;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.Util;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class EventHandlerServer {
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			PlayerTracker.tick();
			PlayerList playerList = ServerLifecycleHooks.getCurrentServer().getPlayerList();
			int viewDist = playerList.getViewDistance();
			float range = MathHelper.clamp(viewDist / 8.0F, 1.0F, 2.5F) * 64.0F; // This is how the client determines entity render distance
			for (Map.Entry<UUID, VRPlayerData> entry : PlayerTracker.players.entrySet()) {
				ServerPlayerEntity player = playerList.getPlayerByUUID(entry.getKey());
				if (player != null) {
					PacketUberPacket packet = PlayerTracker.getPlayerDataPacket(entry.getKey(), entry.getValue());
					ChannelHandler.sendToAllTrackingEntity(packet, player);
				}
			}
		}
	}

	@SubscribeEvent
	public void onAttackEntity(AttackEntityEvent event) {
		if (event.getTarget() instanceof PlayerEntity) {
			PlayerEntity player = event.getPlayer();
			PlayerEntity target = (PlayerEntity)event.getTarget();
			if (PlayerTracker.hasPlayerData(player)) {
				VRPlayerData data = PlayerTracker.getPlayerData(player);
				if (data.seated) { // Seated VR vs...
					if (PlayerTracker.hasPlayerData(target)) {
						VRPlayerData targetData = PlayerTracker.getPlayerData(target);
						if (targetData.seated) { // ...seated VR
							if (!Config.seatedVrVsSeatedVR.get()) event.setCanceled(true);
						} else { // ...VR
							if (!Config.vrVsSeatedVR.get()) event.setCanceled(true);
						}
					} else { // ...non-VR
						if (!Config.seatedVrVsNonVR.get()) event.setCanceled(true);
					}
				} else { // VR vs...
					if (PlayerTracker.hasPlayerData(target)) {
						VRPlayerData targetData = PlayerTracker.getPlayerData(target);
						if (targetData.seated) { // ...seated VR
							if (!Config.vrVsSeatedVR.get()) event.setCanceled(true);
						} else { // ...VR
							if (!Config.vrVsVR.get()) event.setCanceled(true);
						}
					} else { // ...non-VR
						if (!Config.vrVsNonVR.get()) event.setCanceled(true);
					}
				}
			} else { // Non-VR vs...
				if (PlayerTracker.hasPlayerData(target)) {
					VRPlayerData targetData = PlayerTracker.getPlayerData(target);
					if (targetData.seated) { // ...seated VR
						if (!Config.seatedVrVsNonVR.get()) event.setCanceled(true);
					} else { // ...VR
						if (!Config.vrVsNonVR.get()) event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onArrowLoose(ArrowLooseEvent event) {
		PlayerEntity player = event.getPlayer();
		VRPlayerData data = PlayerTracker.getPlayerData(player);
		if (data != null && !data.seated && data.bowDraw > 0) {
			LogHelper.debug("Bow draw: " + data.bowDraw);
			event.setCharge(Math.round(data.bowDraw * 20));
		}
	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		LivingEntity target = event.getEntityLiving();
		DamageSource source = event.getSource();
		if (source.getImmediateSource() instanceof ArrowEntity && source.getTrueSource() instanceof PlayerEntity) {
			ArrowEntity arrow = (ArrowEntity) source.getImmediateSource();
			PlayerEntity attacker = (PlayerEntity)source.getTrueSource();
			if (PlayerTracker.hasPlayerData(attacker)) {
				VRPlayerData data = PlayerTracker.getPlayerData(attacker);
				boolean headshot = Util.isHeadshot(target, arrow);
				if (data.seated) {
					if (headshot) event.setAmount(event.getAmount() * Config.bowSeatedHeadshotMul.get().floatValue());
					else event.setAmount(event.getAmount() * Config.bowSeatedMul.get().floatValue());
				} else {
					if (headshot) event.setAmount(event.getAmount() * Config.bowStandingHeadshotMul.get().floatValue());
					else event.setAmount(event.getAmount() * Config.bowStandingMul.get().floatValue());
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof ServerPlayerEntity) {
			final ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
			if (Config.vrOnly.get() && !player.hasPermissionLevel(2)) {
				Util.scheduler.schedule(() -> {
					ServerLifecycleHooks.getCurrentServer().runAsync(() -> {
						if (player.connection.getNetworkManager().isChannelOpen() && !PlayerTracker.hasPlayerData(player)) {
							player.sendMessage(new StringTextComponent(Config.vrOnlyKickMessage.get()), net.minecraft.util.Util.DUMMY_UUID);
							player.sendMessage(new StringTextComponent("If this is not a VR client, you will be kicked in " + Config.vrOnlyKickDelay.get() + " seconds."), net.minecraft.util.Util.DUMMY_UUID);
							Util.scheduler.schedule(() -> {
								ServerLifecycleHooks.getCurrentServer().runAsync(() -> {
									if (player.connection.getNetworkManager().isChannelOpen() && !PlayerTracker.hasPlayerData(player)) {
										player.connection.disconnect(new StringTextComponent(Config.vrOnlyKickMessage.get()));
									}
								});
							}, Math.round(Config.vrOnlyKickDelay.get() * 1000), TimeUnit.MILLISECONDS);
						}
					});
				}, 1000, TimeUnit.MILLISECONDS);
			}
		} else if (event.getEntity() instanceof ProjectileEntity) {
			ProjectileEntity projectile = (ProjectileEntity)event.getEntity();
			if (!(projectile.func_234616_v_() instanceof PlayerEntity))
				return;
			PlayerEntity shooter = (PlayerEntity)projectile.func_234616_v_();
			if (!PlayerTracker.hasPlayerData(shooter))
				return;

			boolean arrow = projectile instanceof AbstractArrowEntity && !(projectile instanceof TridentEntity);
			VRPlayerData data = PlayerTracker.getPlayerDataAbsolute(shooter);
			Vector3d pos = data.getController(data.activeHand).getPos();
			Vector3d aim = data.getController(data.activeHand).getRot().multiply(new Vector3d(0, 0, -1));

			if (arrow && !data.seated && data.bowDraw > 0) {
				pos = data.getController(0).getPos();
				aim = data.getController(1).getPos().subtract(pos).normalize();
			}

			pos = pos.add(aim.scale(0.6));
			double vel = projectile.getMotion().length();
			projectile.setPosition(pos.x, pos.y, pos.z);
			projectile.shoot(aim.x, aim.y, aim.z, (float)vel, 0.0f);

			Vector3d shooterMotion = shooter.getMotion();
			projectile.setMotion(projectile.getMotion().add(shooterMotion.x, shooter.isOnGround() ? 0.0 : shooterMotion.y, shooterMotion.z));

			LogHelper.debug("Projectile direction: {}", aim);
			LogHelper.debug("Projectile velocity: {}", vel);
		} else if (event.getEntity() instanceof CreeperEntity) {
			CreeperEntity creeper = (CreeperEntity)event.getEntity();
			Util.replaceAIGoal(creeper, creeper.goalSelector, CreeperSwellGoal.class, () -> new VRCreeperSwellGoal(creeper));
		} else if (event.getEntity() instanceof EndermanEntity) {
			EndermanEntity enderman = (EndermanEntity)event.getEntity();
			Util.replaceAIGoal(enderman, enderman.goalSelector, EndermanEntity.StareGoal.class, () -> new VREndermanStareGoal(enderman));
			Util.replaceAIGoal(enderman, enderman.targetSelector, EndermanEntity.FindPlayerGoal.class, () -> new VREndermanFindPlayerGoal(enderman));
		}
	}

	@SubscribeEvent
	public void onItemToss(ItemTossEvent event) {
		if (!PlayerTracker.hasPlayerData(event.getPlayer()))
			return;

		VRPlayerData data = PlayerTracker.getPlayerDataAbsolute(event.getPlayer());
		ItemEntity item = event.getEntityItem();

		Vector3d pos = data.getController(0).getPos();
		Vector3d aim = data.getController(0).getRot().multiply(new Vector3d(0, 0, -1));
		Vector3d aimUp = data.getController(0).getRot().multiply(new Vector3d(0, 1, 0));
		double pitch = Math.toDegrees(Math.asin(-aim.y));

		pos = pos.add(aim.scale(0.2)).subtract(aimUp.scale(0.4 * (1 - Math.abs(pitch) / 90)));
		double vel = 0.3;
		item.setPosition(pos.x, pos.y, pos.z);
		item.setMotion(aim.scale(vel));
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		NetworkManager netManager = ((ServerPlayerEntity)event.getPlayer()).connection.getNetworkManager();
		netManager.channel().pipeline().addBefore("packet_handler", "vr_aim_fix", new AimFixHandler(netManager));
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			VRPlayerData data = PlayerTracker.getPlayerData(event.player);
			if (data != null && data.crawling)
				event.player.setPose(Pose.SWIMMING);
		}
	}
}
