package com.techjar.vivecraftforge;

import net.minecraftforge.common.config.Configuration;

import com.techjar.vivecraftforge.network.ChannelHandler;
import com.techjar.vivecraftforge.proxy.ProxyCommon;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "vivecraftforgeextensions", name = "Vivecraft Forge Extensions", version = "@VERSION@", dependencies = "required-after:Forge@[12.18.3.2281,)", acceptableRemoteVersions = "*")
public class VivecraftForge {
	public static final String MOD_ID = "vivecraftforgeextensions";
	public static final String MOD_NAME = "Vivecraft Forge Extensions";
	public static final String MOD_VERSION = "@VERSION@";

	@Mod.Instance("VivecraftForge")
	public static VivecraftForge instance;
	
	@SidedProxy(clientSide = "com.techjar.vivecraftforge.proxy.ProxyClient", serverSide = "com.techjar.vivecraftforge.proxy.ProxyServer")
	public static ProxyCommon proxy;
	
	public static ChannelHandler packetPipeline;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		Config.vrCreeperSwellDistance = config.get(Configuration.CATEGORY_GENERAL, "vrCreeperSwellDistance", 1.75, "Distance at which creepers swell and explode for VR players. Default: 1.75").getDouble(1.75D);
		if (config.hasChanged()) config.save();
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.registerNetwork();
		proxy.registerEventHandlers();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}
}
