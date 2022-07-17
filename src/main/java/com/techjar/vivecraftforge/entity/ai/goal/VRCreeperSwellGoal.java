package com.techjar.vivecraftforge.entity.ai.goal;

import com.techjar.vivecraftforge.Config;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;

public class VRCreeperSwellGoal extends SwellGoal {
	public VRCreeperSwellGoal(Creeper p_i1655_1_) {
		super(p_i1655_1_);
	}

	@Override
	public boolean canUse() {
		LivingEntity target = this.creeper.getTarget();
		if (target instanceof Player) {
			VRPlayerData data = PlayerTracker.getPlayerData((Player)target);
			if (data != null && !data.seated)
				return this.creeper.getSwellDir() > 0 || this.creeper.distanceToSqr(target) < Config.creeperSwellDistance.get() * Config.creeperSwellDistance.get();
		}
		return super.canUse();
	}
}
