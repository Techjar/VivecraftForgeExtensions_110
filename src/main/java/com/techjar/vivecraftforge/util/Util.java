package com.techjar.vivecraftforge.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

public class Util {
	public static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
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
	public static boolean isHeadshot(LivingEntity target, Arrow arrow) {
		if (target.isPassenger()) return false;
		if (target instanceof Player) {
			Player player = (Player)target;
			if (player.isShiftKeyDown()) {
				//totalHeight = 1.65;
				//bodyHeight = 1.20;
				//headHeight = 0.45;
				if (arrow.getY() >= player.getY() + 1.20) return true;
			} else {
				//totalHeight = 1.80;
				//bodyHeight = 1.35;
				//headHeight = 0.45;
				if (arrow.getY() >= player.getY() + 1.35) return true;
			}
		} else {
			// TODO: mobs
		}
		return false;
	}

	public static boolean shouldEndermanAttackVRPlayer(EnderMan enderman, Player player) {
		ItemStack itemstack = player.getInventory().armor.get(3);
		if (!itemstack.isEnderMask(player, enderman)) {
			VRPlayerData data = PlayerTracker.getPlayerDataAbsolute(player);
			Quaternion quat = data.head.getRot();
			Vec3 vector3d = quat.multiply(new Vec3(0, 0, -1));
			Vec3 vector3d1 = new Vec3(enderman.getX() - data.head.posX, enderman.getEyeY() - data.head.posY, enderman.getZ() - data.head.posZ);
			double d0 = vector3d1.length();
			vector3d1 = vector3d1.normalize();
			double d1 = vector3d.dot(vector3d1);
			return d1 > 1.0D - 0.025D / d0 && canEntityBeSeen(enderman, data.head.getPos());
		}

		return false;
	}

	public static boolean canEntityBeSeen(Entity entity, Vec3 playerEyePos) {
		Vec3 entityEyePos = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
		return entity.level.clip(new ClipContext(playerEyePos, entityEyePos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS;
	}

	public static void replaceAIGoal(Mob entity, GoalSelector goalSelector, Class<? extends Goal> targetGoal, Supplier<Goal> newGoalSupplier) {
		WrappedGoal goal = goalSelector.availableGoals.stream().filter((g) -> targetGoal.isInstance(g.getGoal())).findFirst().orElse(null);
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
