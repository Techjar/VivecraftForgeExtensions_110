package com.techjar.vivecraftforge.eventhandler;

import com.techjar.vivecraftforge.Config;
import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.entity.ai.goal.VRCreeperSwellGoal;
import com.techjar.vivecraftforge.entity.ai.goal.VREndermanStareGoal;
import com.techjar.vivecraftforge.network.ChannelHandler;
import com.techjar.vivecraftforge.network.packet.PacketUberPacket;
import com.techjar.vivecraftforge.util.LogHelper;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.ReflectionHelper;
import com.techjar.vivecraftforge.util.Util;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Map;
import java.util.Set;
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
			if (Config.vrOnly.get()) {
				player.sendMessage(new StringTextComponent(Config.vrOnlyKickMessage.get()), net.minecraft.util.Util.DUMMY_UUID);
				player.sendMessage(new StringTextComponent("You will be kicked in " + Config.vrOnlyKickDelay.get() + " seconds."), net.minecraft.util.Util.DUMMY_UUID);
				Util.scheduler.schedule(() -> {
					ServerLifecycleHooks.getCurrentServer().deferTask(() -> {
						if (player.connection.getNetworkManager().isChannelOpen()) {
							player.connection.disconnect(new StringTextComponent(Config.vrOnlyKickMessage.get()));
						}
					});
				}, Math.round(Config.vrOnlyKickDelay.get() * 1000), TimeUnit.MILLISECONDS);
			}
		} else if (event.getEntity() instanceof CreeperEntity) {
			CreeperEntity creeper = (CreeperEntity)event.getEntity();
			PrioritizedGoal goal = ((Set<PrioritizedGoal>)ReflectionHelper.GoalSelector_goals.get(creeper.goalSelector)).stream().filter((g) -> g.getGoal() instanceof CreeperSwellGoal).findFirst().orElse(null);
			if (goal != null) {
				creeper.goalSelector.removeGoal(goal.getGoal());
				creeper.goalSelector.addGoal(goal.getPriority(), new VRCreeperSwellGoal(creeper));
				LogHelper.debug("Replaced CreeperSwellGoal in %s", creeper);
			} else {
				LogHelper.warning("Couldn't find CreeperSwellGoal in %s", creeper);
			}
		} else if (event.getEntity() instanceof EndermanEntity) {
			EndermanEntity enderman = (EndermanEntity)event.getEntity();
			PrioritizedGoal goal = ((Set<PrioritizedGoal>)ReflectionHelper.GoalSelector_goals.get(enderman.goalSelector)).stream().filter((g) -> g.getGoal() instanceof EndermanEntity.StareGoal).findFirst().orElse(null);
			if (goal != null) {
				enderman.goalSelector.removeGoal(goal.getGoal());
				enderman.goalSelector.addGoal(goal.getPriority(), new VREndermanStareGoal(enderman));
				LogHelper.debug("Replaced EndermanEntity.StareGoal in %s", enderman);
			} else {
				LogHelper.warning("Couldn't find EndermanEntity.StareGoal in %s", enderman);
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

			pos = pos.add(aim.mul(0.6, 0.6, 0.6));
			double vel = projectile.getMotion().length();
			projectile.setMotion(aim.mul(vel, vel, vel));
			projectile.setPosition(pos.x, pos.y, pos.z);
		}
	}
}
