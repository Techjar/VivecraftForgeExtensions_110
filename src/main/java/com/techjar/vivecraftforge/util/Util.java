package com.techjar.vivecraftforge.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

public class Util {
	public static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				scheduler.shutdownNow();
			}
		});
	}

	private Util() {
	}

	/*
	 * This is mostly copied from VSE
	 */
	public static boolean isHeadshot(LivingEntity target, ArrowEntity arrow) {
		if (target.isPassenger()) return false;
		if (target instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)target;
			if (player.isSneaking()) {
				//totalHeight = 1.65;
				//bodyHeight = 1.20;
				//headHeight = 0.45;
				if (arrow.getPosY() >= player.getPosY() + 1.20) return true;
			} else {
				//totalHeight = 1.80;
				//bodyHeight = 1.35;
				//headHeight = 0.45;
				if (arrow.getPosY() >= player.getPosY() + 1.35) return true;
			}
		} else {
			// TODO: mobs
		}
		return false;
	}

	public static boolean shouldEndermanAttackVRPlayer(EndermanEntity enderman, PlayerEntity player) {
		ItemStack itemstack = player.inventory.armorInventory.get(3);
		if (!itemstack.isEnderMask(player, enderman)) {
			VRPlayerData data = PlayerTracker.getPlayerDataAbsolute(player);
			Quaternion quat = data.head.getRot();
			Vector3d vector3d = quat.multiply(new Vector3d(0, 0, -1));
			Vector3d vector3d1 = new Vector3d(enderman.getPosX() - data.head.posX, enderman.getPosYEye() - data.head.posY, enderman.getPosZ() - data.head.posZ);
			double d0 = vector3d1.length();
			vector3d1 = vector3d1.normalize();
			double d1 = vector3d.dotProduct(vector3d1);
			return d1 > 1.0D - 0.025D / d0 && canEntityBeSeen(enderman, data.head.getPos());
		}

		return false;
	}

	public static boolean canEntityBeSeen(Entity entity, Vector3d playerEyePos) {
		Vector3d entityEyePos = new Vector3d(entity.getPosX(), entity.getPosYEye(), entity.getPosZ());
		return entity.world.rayTraceBlocks(new RayTraceContext(playerEyePos, entityEyePos, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity)).getType() == RayTraceResult.Type.MISS;
	}

	public static void replaceAIGoal(MobEntity entity, GoalSelector goalSelector, Class<? extends Goal> targetGoal, Supplier<Goal> newGoalSupplier) {
		PrioritizedGoal goal = goalSelector.goals.stream().filter((g) -> targetGoal.isInstance(g.getGoal())).findFirst().orElse(null);
		if (goal != null) {
			goalSelector.removeGoal(goal.getGoal());
			goalSelector.addGoal(goal.getPriority(), newGoalSupplier.get());
			LogHelper.debug("Replaced {} in {}", targetGoal.getSimpleName(), entity);
		} else {
			LogHelper.debug("Couldn't find {} in {}", targetGoal.getSimpleName(), entity);
		}
	}

	public static String getMoney() {
		return "\n||====================================================================||\n" +
			"||//$\\\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\//$\\\\||\n" +
			"||(100)==================| FEDERAL RESERVE NOTE |================(100)||\n" +
			"||\\\\$//        ~         '------========--------'                \\\\$//||\n" +
			"||<< /        /$\\              // ____ \\\\                         \\ >>||\n" +
			"||>>|  12    //L\\\\            // ///..) \\\\         L38036133B   12 |<<||\n" +
			"||<<|        \\\\ //           || <||  >\\  ||                        |>>||\n" +
			"||>>|         \\$/            ||  $$ --/  ||        One Hundred     |<<||\n" +
			"||<<|      L38036133B        *\\\\  |\\_/  //* series                 |>>||\n" +
			"||>>|  12                     *\\\\/___\\_//*   1989                  |<<||\n" +
			"||<<\\      Treasurer     ______/Franklin\\________     Secretary 12 />>||\n" +
			"||//$\\                 ~|UNITED STATES OF AMERICA|~               /$\\\\||\n" +
			"||(100)===================  ONE HUNDRED DOLLARS =================(100)||\n" +
			"||\\\\$//\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\\\$//||\n" +
			"||====================================================================||";
	}
}
