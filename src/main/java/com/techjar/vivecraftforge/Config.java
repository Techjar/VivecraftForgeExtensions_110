package com.techjar.vivecraftforge;

import com.techjar.vivecraftforge.util.BlockListMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

public class Config {
	public static ForgeConfigSpec config;

	public static ForgeConfigSpec.BooleanValue vrOnly;
	public static ForgeConfigSpec.DoubleValue vrOnlyKickDelay;
	public static ForgeConfigSpec.BooleanValue printMoney;
	public static ForgeConfigSpec.ConfigValue<String> vrOnlyKickMessage;
	public static ForgeConfigSpec.BooleanValue enableJoinMessages;
	public static ForgeConfigSpec.ConfigValue<String> joinMessageVR;
	public static ForgeConfigSpec.ConfigValue<String> joinMessageNonVR;
	public static ForgeConfigSpec.DoubleValue creeperSwellDistance;
	public static ForgeConfigSpec.BooleanValue vrVsVR;
	public static ForgeConfigSpec.BooleanValue vrVsSeatedVR;
	public static ForgeConfigSpec.BooleanValue vrVsNonVR;
	public static ForgeConfigSpec.BooleanValue seatedVrVsSeatedVR;
	public static ForgeConfigSpec.BooleanValue seatedVrVsNonVR;
	public static ForgeConfigSpec.DoubleValue bowStandingMul;
	public static ForgeConfigSpec.DoubleValue bowSeatedMul;
	public static ForgeConfigSpec.DoubleValue bowStandingHeadshotMul;
	public static ForgeConfigSpec.DoubleValue bowSeatedHeadshotMul;
	public static ForgeConfigSpec.BooleanValue climbeyEnabled;
	public static ForgeConfigSpec.EnumValue<BlockListMode> blockListMode;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> blockList;
	public static ForgeConfigSpec.BooleanValue crawlingEnabled;
	public static ForgeConfigSpec.BooleanValue teleportEnabled;
	public static ForgeConfigSpec.BooleanValue teleportLimited;
	public static ForgeConfigSpec.IntValue teleportLimitUp;
	public static ForgeConfigSpec.IntValue teleportLimitDown;
	public static ForgeConfigSpec.IntValue teleportLimitHoriz;
	public static ForgeConfigSpec.BooleanValue worldScaleLimited;
	public static ForgeConfigSpec.DoubleValue worldScaleMin;
	public static ForgeConfigSpec.DoubleValue worldScaleMax;

	static {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

		builder.comment("General settings").push("general");
		vrOnly = builder.comment("Enable to allow only VR players to play.").define("vronly", false);
		vrOnlyKickDelay = builder.comment("How many seconds to wait before kicking non-VR players.").defineInRange("vronlykickdelay", 5.0, 1.0, Double.MAX_VALUE);
		printMoney = builder.comment("Don't get caught using this!").define("printmoney", false);
		builder.pop(); // general

		builder.comment("Chat messages").push("messages");
		vrOnlyKickMessage = builder.comment("Kick message displayed to non-VR players, if vronly is enabled.").define("vronlykickmessage", "This server only allows VR players.");
		enableJoinMessages = builder.comment("Enable or disable all join messages.").define("enablejoinmessages", false);
		joinMessageVR = builder.comment("Message displayed when a player joins using VR. Leave blank to disable.").define("joinmessagevr", "\u00A79%s has joined using \u00A76VR\u00A79!");
		joinMessageNonVR = builder.comment("Message displayed when a player joins using the non-VR companion. Leave blank to disable.").define("joinmessagenonvr", "\u00A7a%s has joined using non-VR!");
		builder.pop(); // messages

		builder.comment("Vanilla modifications for VR players").push("vrchanges");
		creeperSwellDistance = builder.comment("Distance at which creepers swell and explode for VR players. Vanilla: 3").defineInRange("creeperswelldistance", 1.75, 1, 3);
		builder.comment("Bow damage adjustments").push("bow");
		bowStandingMul = builder.comment("Archery damage multiplier for standing VR players.").defineInRange("standingmultiplier", 2.0, 1.0, 3.0);
		bowSeatedMul = builder.comment("Archery damage multiplier for seated VR players.").defineInRange("seatedmultiplier", 1.0, 1.0, 3.0);
		bowStandingHeadshotMul = builder.comment("Archery headshot damage multiplier for standing VR players.").defineInRange("standingheadshotmultiplier", 3.0, 1.0, 3.0);
		bowSeatedHeadshotMul = builder.comment("Archery headshot damage multiplier for seated VR players.").defineInRange("seatedheadshotmultiplier", 2.0, 1.0, 3.0);
		builder.pop(); // bow
		builder.pop(); // vrchanges

		builder.comment("VR vs. non-VR vs. seated player PVP settings").push("pvp");
		vrVsVR = builder.comment("Allow standing VR players to attack standing VR players.").define("vrvsvr", true);
		vrVsSeatedVR = builder.comment("Allow standing VR players to attack seated VR players.").define("vrvsseatedvr", true);
		vrVsNonVR = builder.comment("Allow standing VR players to attack non-VR players.").define("vrvsnonvr", true);
		seatedVrVsSeatedVR = builder.comment("Allow seated VR players to attack seated VR players.").define("seatedvrvsseatedvr", true);
		seatedVrVsNonVR = builder.comment("Allow seated VR players to attack non-VR players.").define("seatedvrvsnonvr", true);
		builder.pop(); // pvp

		builder.comment("Climbey motion settings").push("climbey");
		climbeyEnabled = builder.comment("Whether or not climbey is allowed on this server.").define("enabled", true);
		blockListMode = builder.comment("Mode for block list.").defineEnum("blocklistmode", BlockListMode.DISABLED);
		blockList = builder.comment("List of blocks to whitelist or blacklist").defineList("blocklist", Arrays.asList("white_wool", "dirt", "grass_block"), (s) -> s instanceof String && ForgeRegistries.BLOCKS.containsKey(new ResourceLocation((String)s)));
		builder.pop(); // climbey

		builder.comment("Roomscale crawling settings").push("crawling");
		crawlingEnabled = builder.comment("Whether clients will be allowed to use roomscale crawling. Does not disable vanilla forced-into-small-space crawling mechanic.").define("enabled", true);
		builder.pop(); // crawling

		builder.comment("Teleport settings").push("teleport");
		teleportEnabled = builder.comment("Whether teleport is allowed. Recommended for players prone to VR sickness").define("enabled", true);
		teleportLimited = builder.comment("Whether to limit teleport distance and frequency in survival").define("limitedsurvival", false);
		teleportLimitUp = builder.comment("Maximum blocks players can teleport up, set 0 to disable").defineInRange("uplimit", 1, 0, 16);
		teleportLimitDown = builder.comment("Maximum blocks players can teleport down, set 0 to disable").defineInRange("downlimit", 4, 0, 16);
		teleportLimitHoriz = builder.comment("Maximum blocks players can teleport horizontally, set 0 to disable").defineInRange("horizontallimit", 16, 0, 32);
		builder.pop(); // teleport

		builder.comment("World scale settings").push("worldscale");
		worldScaleLimited = builder.comment("Limit the range of world scale clients may use").define("limitrange", false);
		worldScaleMax = builder.comment("Upper limit of world scale").defineInRange("max", 2, 0.1, 100);
		worldScaleMin = builder.comment("Lower limit of world scale").defineInRange("min", 0.5, 0.1, 100);
		builder.pop(); // worldscale

		config = builder.build();
	}
}
