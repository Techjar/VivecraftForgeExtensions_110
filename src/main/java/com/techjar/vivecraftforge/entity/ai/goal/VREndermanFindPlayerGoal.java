package com.techjar.vivecraftforge.entity.ai.goal;

import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.Util;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;

public class VREndermanFindPlayerGoal extends EndermanEntity.FindPlayerGoal {
	private final EntityPredicate targetPredicate;
	private final EntityPredicate lineOfSightPredicate = (new EntityPredicate()).setLineOfSiteRequired();

	public VREndermanFindPlayerGoal(EndermanEntity enderman) {
		super(enderman);
		this.targetPredicate = (new EntityPredicate()).setDistance(this.getTargetDistance()).setCustomPredicate((p) -> {
			if (PlayerTracker.hasPlayerData((PlayerEntity)p))
				return Util.shouldEndermanAttackVRPlayer(enderman, (PlayerEntity)p);
			else
				return enderman.shouldAttackPlayer((PlayerEntity)p);
		});
	}

	@Override
	public boolean shouldExecute() {
		this.player = this.enderman.world.getClosestPlayer(this.targetPredicate, this.enderman);
		return this.player != null;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.player == null || !PlayerTracker.hasPlayerData(this.player))
			return super.shouldContinueExecuting();

		if (this.player != null) {
			if (!Util.shouldEndermanAttackVRPlayer(this.enderman, this.player)) {
				return false;
			} else {
				this.enderman.faceEntity(this.player, 10.0F, 10.0F);
				return true;
			}
		} else {
			return (this.nearestTarget != null && this.lineOfSightPredicate.canTarget(this.enderman, this.nearestTarget)) || super.shouldContinueExecuting();
		}
	}

	@Override
	public void tick() {
		if (this.nearestTarget == null || !PlayerTracker.hasPlayerData((PlayerEntity)this.nearestTarget)) {
			super.tick();
			return;
		}

		if (this.enderman.getAttackTarget() == null) {
			super.setNearestTarget(null);
		}

		if (this.player != null) {
			if (--this.aggroTime <= 0) {
				this.nearestTarget = this.player;
				this.player = null;
				super.startExecuting();
			}
		} else {
			if (this.nearestTarget != null && !this.enderman.isPassenger()) {
				if (Util.shouldEndermanAttackVRPlayer(this.enderman, (PlayerEntity)this.nearestTarget)) {
					if (this.nearestTarget.getDistanceSq(this.enderman) < 16.0D) {
						this.enderman.teleportRandomly();
					}

					this.teleportTime = 0;
				} else if (this.nearestTarget.getDistanceSq(this.enderman) > 256.0D && this.teleportTime++ >= 30 && this.enderman.teleportToEntity(this.nearestTarget)) {
					this.teleportTime = 0;
				}
			}

			// don't work but doesn't matter
			//super.tick();
		}
	}
}
