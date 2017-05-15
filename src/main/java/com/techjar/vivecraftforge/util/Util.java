package com.techjar.vivecraftforge.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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

	public static TextFormatting findTextFormatByCode(char ch) {
		ch = Character.toLowerCase(ch);
		switch (ch) {
			case '0': return TextFormatting.BLACK;
			case '1': return TextFormatting.DARK_BLUE;
			case '2': return TextFormatting.DARK_GREEN;
			case '3': return TextFormatting.DARK_AQUA;
			case '4': return TextFormatting.DARK_RED;
			case '5': return TextFormatting.DARK_PURPLE;
			case '6': return TextFormatting.GOLD;
			case '7': return TextFormatting.GRAY;
			case '8': return TextFormatting.DARK_GRAY;
			case '9': return TextFormatting.BLUE;
			case 'a': return TextFormatting.GREEN;
			case 'b': return TextFormatting.AQUA;
			case 'c': return TextFormatting.RED;
			case 'd': return TextFormatting.LIGHT_PURPLE;
			case 'e': return TextFormatting.YELLOW;
			case 'f': return TextFormatting.WHITE;
			case 'k': return TextFormatting.OBFUSCATED;
			case 'l': return TextFormatting.BOLD;
			case 'm': return TextFormatting.STRIKETHROUGH;
			case 'n': return TextFormatting.UNDERLINE;
			case 'o': return TextFormatting.ITALIC;
			case 'r': return TextFormatting.RESET;
		}
		return null;
	}

	public static void applyTextFormatting(Style style, TextFormatting... formats) {
		for (TextFormatting formatting : formats) {
			switch (formatting) {
				case OBFUSCATED:
					style.setObfuscated(true);
					break;
				case BOLD:
					style.setBold(true);
					break;
				case STRIKETHROUGH:
					style.setStrikethrough(true);
					break;
				case UNDERLINE:
					style.setUnderlined(true);
					break;
				case ITALIC:
					style.setItalic(true);
					break;
				case RESET:
					style.setObfuscated(false);
					style.setBold(false);
					style.setStrikethrough(false);
					style.setUnderlined(false);
					style.setItalic(false);
					style.setColor(null);
					break;
				default:
					style.setColor(formatting);
			}
		}
	}
}
