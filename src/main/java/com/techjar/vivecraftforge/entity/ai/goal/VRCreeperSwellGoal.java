package com.techjar.vivecraftforge.entity.ai.goal;

import com.techjar.vivecraftforge.Config;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;

public class VRCreeperSwellGoal extends CreeperSwellGoal {
	public VRCreeperSwellGoal(CreeperEntity p_i1655_1_) {
		super(p_i1655_1_);
	}

	@Override
	public boolean shouldExecute() {
		LivingEntity target = this.swellingCreeper.getAttackTarget();
		if (target instanceof PlayerEntity) {
			VRPlayerData data = PlayerTracker.getPlayerData((PlayerEntity)target);
			if (data != null && !data.seated)
				return this.swellingCreeper.getCreeperState() > 0 || this.swellingCreeper.getDistanceSq(target) < Config.creeperSwellDistance.get() * Config.creeperSwellDistance.get();
		}
		return super.shouldExecute();
	}
}
