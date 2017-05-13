package com.techjar.vivecraftforge;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.techjar.vivecraftforge.util.BlockListMode;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class Config {
	public static Configuration config;
	public static double creeperSwellDistance;
	public static boolean vrVsVR;
	public static boolean vrVsSeatedVR;
	public static boolean vrVsNonVR;
	public static boolean seatedVrVsSeatedVR;
	public static boolean seatedVrVsNonVR;
	public static boolean climbeyEnabled;
	public static BlockListMode blockListMode;
	public static ArrayList<String> blockList;

	private Config() {
	}

	public static void init(File file) {
		config = new Configuration(file);
		config.load();
		// VR Changes
		creeperSwellDistance = config.get("vrchanges", "creeperswelldistance", 1.75, "Distance at which creepers swell and explode for VR players. Default: 1.75, Vanilla: 3").getDouble();
		// PvP
		vrVsVR = config.get("pvp", "vrvsvr", true, "Allows standing VR players to attack standing VR players.").getBoolean();
		vrVsSeatedVR = config.get("pvp", "vrvsseatedvr", true, "Allows standing VR players to attack seated VR players.").getBoolean();
		vrVsNonVR = config.get("pvp", "vrvsnonvr", true, "Allows standing VR players to attack non-VR players.").getBoolean();
		seatedVrVsSeatedVR = config.get("pvp", "seatedvrvsseatedvr", true, "Allows seated VR players to attack seated VR players.").getBoolean();
		seatedVrVsNonVR = config.get("pvp", "seatedvrvsnonvr", true, "Allows seated VR players to attack non-VR players.").getBoolean();
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
							VivecraftForgeLog.warning("Invalid meta for blocklist item: %s", fullStr);
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
						VivecraftForgeLog.warning("Unknown block for blocklist item: %s", fullStr);
						it.remove();
						changed = true;
					}
				} else {
					if (Block.getBlockFromName(str) == Blocks.AIR || Block.getBlockFromName(str) == null) {
						VivecraftForgeLog.warning("Unknown block for blocklist item: %s", fullStr);
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
