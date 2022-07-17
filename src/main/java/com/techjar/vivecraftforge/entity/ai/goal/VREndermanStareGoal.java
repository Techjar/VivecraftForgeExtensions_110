package com.techjar.vivecraftforge.entity.ai.goal;

import com.techjar.vivecraftforge.util.LogHelper;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.Quaternion;
import com.techjar.vivecraftforge.util.Util;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;

public class VREndermanStareGoal extends EnderMan.EndermanFreezeWhenLookedAt {
	public VREndermanStareGoal(EnderMan enderman) {
		super(enderman);
	}

	@Override
	public boolean canUse() {
		boolean orig = super.canUse(); // call this always so stuff gets set up

		LivingEntity target = this.enderman.getTarget();
		if (target instanceof Player && PlayerTracker.hasPlayerData((Player)target)) {
			double dist = target.distanceToSqr(this.enderman);
			return dist <= 256.0D && Util.shouldEndermanAttackVRPlayer(this.enderman, (Player)target);
		}

		return orig;
	}

	@Override
	public void tick() {
		LivingEntity target = this.enderman.getTarget();
		if (target instanceof Player && PlayerTracker.hasPlayerData((Player)target)) {
			VRPlayerData data = PlayerTracker.getPlayerDataAbsolute((Player)target);
			this.enderman.getLookControl().setLookAt(data.head.posX, data.head.posY, data.head.posZ);
		} else {
			super.tick();
		}
	}
}
