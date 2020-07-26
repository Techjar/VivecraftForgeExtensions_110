package com.techjar.vivecraftforge.entity.ai.goal;

import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.Quaternion;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;

public class VREndermanStareGoal extends EndermanEntity.StareGoal {
	public VREndermanStareGoal(EndermanEntity enderman) {
		super(enderman);
	}

	public boolean shouldExecute() {
		boolean orig = super.shouldExecute(); // call this always so stuff gets set up

		LivingEntity target = this.enderman.getAttackTarget();
		if (target instanceof PlayerEntity && PlayerTracker.hasPlayerData((PlayerEntity)target)) {
			double dist = target.getDistanceSq(this.enderman);
			return dist <= 256.0D && this.shouldAttackVRPlayer((PlayerEntity)target);
		}

		return orig;
	}

	private boolean shouldAttackVRPlayer(PlayerEntity player) {
		ItemStack itemstack = player.inventory.armorInventory.get(3);
		if (!itemstack.isEnderMask(player, this.enderman)) {
			VRPlayerData data = PlayerTracker.getPlayerData(player);
			Quaternion quat = new Quaternion(data.head.rotW, data.head.rotX, data.head.rotY, data.head.rotZ);
			Vector3d vector3d = quat.multiply(new Vector3d(0, 0, -1));
			Vector3d vector3d1 = new Vector3d(this.enderman.getPosX() - player.getPosX(), this.enderman.getPosYEye() - player.getPosYEye(), this.enderman.getPosZ() - player.getPosZ());
			double d0 = vector3d1.length();
			vector3d1 = vector3d1.normalize();
			double d1 = vector3d.dotProduct(vector3d1);
			return d1 > 1.0D - 0.025D / d0 && player.canEntityBeSeen(this.enderman);
		}

		return false;
	}
}
