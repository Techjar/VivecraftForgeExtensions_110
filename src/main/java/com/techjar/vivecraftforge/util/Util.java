package com.techjar.vivecraftforge.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
	public static boolean isHeadshot(EntityLivingBase target, EntityArrow arrow) {
		if (target.isRiding()) return false;
		if (target instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)target;
			if (player.isSneaking()) {
				//totalHeight = 1.65;
				//bodyHeight = 1.20;
				//headHeight = 0.45;
				if (arrow.posY >= player.posY + 1.20) return true;
			} else {
				//totalHeight = 1.80;
				//bodyHeight = 1.35;
				//headHeight = 0.45;
				if (arrow.posY >= player.posY + 1.35) return true;
			}
		} else {
			// TODO: mobs
		}
		return false;
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
