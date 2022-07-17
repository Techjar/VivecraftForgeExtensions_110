package com.techjar.vivecraftforge.entity.ai.goal;

import java.util.function.Predicate;
import javax.annotation.Nullable;

import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.Util;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;

public class VREndermanFindPlayerGoal extends EnderMan.EndermanLookForPlayerGoal {
	private final TargetingConditions targetPredicate;
	private final TargetingConditions lineOfSightPredicate = TargetingConditions.forCombat().ignoreLineOfSight();

	public VREndermanFindPlayerGoal(EnderMan enderman, @Nullable Predicate<LivingEntity> p_i241912_2_) {
		super(enderman, p_i241912_2_);
		this.targetPredicate = TargetingConditions.forCombat().range(this.getFollowDistance()).selector((p) -> {
			if (PlayerTracker.hasPlayerData((Player)p))
				return Util.shouldEndermanAttackVRPlayer(enderman, (Player)p);
			else
				return enderman.isLookingAtMe((Player)p);
		});
	}

	@Override
	public boolean canUse() {
		this.pendingTarget = this.enderman.level.getNearestPlayer(this.targetPredicate, this.enderman);
		return this.pendingTarget != null;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.pendingTarget == null || !PlayerTracker.hasPlayerData(this.pendingTarget))
			return super.canContinueToUse();

		if (this.pendingTarget != null) {
			if (!Util.shouldEndermanAttackVRPlayer(this.enderman, this.pendingTarget)) {
				return false;
			} else {
				this.enderman.lookAt(this.pendingTarget, 10.0F, 10.0F);
				return true;
			}
		} else {
			return (this.target != null && this.lineOfSightPredicate.test(this.enderman, this.target)) || super.canContinueToUse();
		}
	}

	@Override
	public void tick() {
		if (this.target == null || !PlayerTracker.hasPlayerData((Player)this.target)) {
			super.tick();
			return;
		}

		if (this.enderman.getTarget() == null) {
			super.setTarget(null);
		}

		if (this.pendingTarget != null) {
			if (--this.aggroTime <= 0) {
				this.target = this.pendingTarget;
				this.pendingTarget = null;
				super.start();
			}
		} else {
			if (this.target != null && !this.enderman.isPassenger()) {
				if (Util.shouldEndermanAttackVRPlayer(this.enderman, (Player)this.target)) {
					if (this.target.distanceToSqr(this.enderman) < 16.0D) {
						this.enderman.teleport();
					}

					this.teleportTime = 0;
				} else if (this.target.distanceToSqr(this.enderman) > 256.0D && this.teleportTime++ >= 30 && this.enderman.teleportTowards(this.target)) {
					this.teleportTime = 0;
				}
			}

			// don't work but doesn't matter
			//super.tick();
		}
	}
}
