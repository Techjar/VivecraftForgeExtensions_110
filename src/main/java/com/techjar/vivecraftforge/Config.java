package com.techjar.vivecraftforge;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.techjar.vivecraftforge.util.BlockListMode;
import com.techjar.vivecraftforge.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class Config {
	public static Configuration config;
	public static boolean vrOnly;
	public static double vrOnlyKickDelay;
	public static boolean printMoney;
	public static String vrOnlyKickMessage;
	public static boolean enableJoinMessages;
	public static String joinMessageVR;
	public static String joinMessageCompanion;
	public static double creeperSwellDistance;
	public static float movedTooQuicklyThreshold;
	public static double movedWronglyThreshold;
	public static boolean vrVsVR;
	public static boolean vrVsSeatedVR;
	public static boolean vrVsNonVR;
	public static boolean seatedVrVsSeatedVR;
	public static boolean seatedVrVsNonVR;
	public static float bowStandingMul;
	public static float bowSeatedMul;
	public static float bowStandingHeadshotMul;
	public static float bowSeatedHeadshotMul;
	public static boolean climbeyEnabled;
	public static BlockListMode blockListMode;
	public static ArrayList<String> blockList;

	private Config() {
	}

	public static void init(File file) {
		if (FMLLaunchHandler.side() != Side.SERVER) return;
		config = new Configuration(file);
		config.load();
		// General
		vrOnly = config.get("general", "vronly", false, "Enable to allow only VR players to play.").getBoolean();
		vrOnlyKickDelay = config.get("general", "vronlykickdelay", 5.0, "How many seconds to wait before kicking non-VR players. Default: 5.0, Minimum: 5.0", 5.0, Double.MAX_VALUE).getDouble();
		printMoney = config.get("general", "printmoney", false, "Don't get caught using this!").getBoolean();
		// Messages
		vrOnlyKickMessage = config.get("messages", "vronlykickmessage", "This server only allows VR players.", "Kick message displayed to non-VR players, if vronly is enabled.").getString();
		enableJoinMessages = config.get("messages", "joinmessages", false, "Enables or disables all join messages.").getBoolean();
		joinMessageVR = config.get("messages", "joinmessagevr", "\u00A79%player% has joined using \u00A76VR\u00A79!", "Message displayed when a player joins using VR. Leave blank to disable.").getString();
		joinMessageCompanion = config.get("messages", "joinmessagecompanion", "\u00A7a%player% has joined using non-VR companion!", "Message displayed when a player joins using the non-VR companion. Leave blank to disable.").getString();
		// VR Changes
		creeperSwellDistance = config.get("vrchanges", "creeperswelldistance", 1.75, "Distance at which creepers swell and explode for VR players. Default: 1.75, Vanilla: 3").getDouble();
		movedTooQuicklyThreshold = (float)config.get("vrchanges", "movedtooquicklythreshold", 64.0, "Increase this if you experience rubber banding when teleporting.").getDouble();
		movedWronglyThreshold = config.get("vrchanges", "movedwronglythreshold", 15.0, "Increase this if you experience rubber banding when teleporting.").getDouble();
		// PvP
		vrVsVR = config.get("pvp", "vrvsvr", true, "Allows standing VR players to attack standing VR players.").getBoolean();
		vrVsSeatedVR = config.get("pvp", "vrvsseatedvr", true, "Allows standing VR players to attack seated VR players.").getBoolean();
		vrVsNonVR = config.get("pvp", "vrvsnonvr", true, "Allows standing VR players to attack non-VR players.").getBoolean();
		seatedVrVsSeatedVR = config.get("pvp", "seatedvrvsseatedvr", true, "Allows seated VR players to attack seated VR players.").getBoolean();
		seatedVrVsNonVR = config.get("pvp", "seatedvrvsnonvr", true, "Allows seated VR players to attack non-VR players.").getBoolean();
		// Bow
		bowStandingMul = (float)config.get("bow", "standingmultiplier", 2.0, "Archery damage multiplier for standing VR players. Default: 2.0").getDouble();
		bowSeatedMul = (float)config.get("bow", "seatedmultiplier", 1.0, "Archery damage multiplier for seated VR players. Default: 1.0").getDouble();
		bowStandingHeadshotMul = (float)config.get("bow", "standingheadshotmultiplier", 3.0, "Archery headshot damage multiplier for standing VR players. Default: 3.0").getDouble();
		bowSeatedHeadshotMul = (float)config.get("bow", "seatedheadshotmultiplier", 2.0, "Archery headshot damage multiplier for seated VR players. Default: 2.0").getDouble();
		// Climbey
		climbeyEnabled = config.get("climbey", "enabled", true, "Whether or not climbey is allowed on this server.").getBoolean();
		{
			Property prop = config.get("climbey", "blocklist", new String[]{"minecraft:wool:0", "minecraft:dirt", "grass"}, "List of blocks to whitelist or blacklist, in format blockname, blockname:meta, blockid or blockid:meta.");
			blockList = Lists.newArrayList(prop.getStringList());
			boolean changed = false;
			for (Iterator<String> it = blockList.iterator(); it.hasNext();) {
				String str = it.next();
				String fullStr = str;
				int colon = str.lastIndexOf(':');
				if (colon != -1) {
					String meta = str.substring(colon + 1);
					str = str.substring(0, colon);
					if (Ints.tryParse(meta) == null) {
						if (str.indexOf(':') != -1) { // Extra colons, must be meta
							LogHelper.warning("Invalid meta for blocklist item: %s", fullStr);
							it.remove();
							changed = true;
							continue;
						} else { // Only colon, must be part of block name
							str = fullStr;
						}
					}
				}
				if (Ints.tryParse(str) != null) {
					if (Block.getBlockById(Integer.parseInt(str)) == Blocks.AIR) {
						LogHelper.warning("Unknown block for blocklist item: %s", fullStr);
						it.remove();
						changed = true;
					}
				} else {
					if (Block.getBlockFromName(str) == Blocks.AIR || Block.getBlockFromName(str) == null) {
						LogHelper.warning("Unknown block for blocklist item: %s", fullStr);
						it.remove();
						changed = true;
					}
				}
			}
			if (changed) prop.set(blockList.toArray(new String[blockList.size()]));
		}
		{
			Property prop = config.get("climbey", "blocklistmode", BlockListMode.DISABLED.toString(), "Mode for block list. DISABLED: All blocks are climbable. WHITELIST: Only blocks on list are climbable. BLACKLIST: All blocks except those on list are climbable.");
			try {
				blockListMode = BlockListMode.valueOf(prop.getString());
			} catch (IllegalArgumentException ex) {
				prop.set(prop.getDefault());
				blockListMode = BlockListMode.valueOf(prop.getString());
			}
		}
		if (config.hasChanged()) config.save();
	}
}
