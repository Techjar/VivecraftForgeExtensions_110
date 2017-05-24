package com.techjar.vivecraftforge;

import com.techjar.vivecraftforge.network.ChannelHandler;
import com.techjar.vivecraftforge.proxy.ProxyCommon;
import com.techjar.vivecraftforge.util.Util;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "vivecraftforgeextensions", name = "Vivecraft Forge Extensions", version = "@VERSION@", acceptableRemoteVersions = "*")
public class VivecraftForge {
	public static final String MOD_ID = "vivecraftforgeextensions";
	public static final String MOD_NAME = "Vivecraft Forge Extensions";
	public static final String MOD_VERSION = "@VERSION@";

	@Mod.Instance("vivecraftforgeextensions")
	public static VivecraftForge instance;
	
	@SidedProxy(clientSide = "com.techjar.vivecraftforge.proxy.ProxyClient", serverSide = "com.techjar.vivecraftforge.proxy.ProxyServer")
	public static ProxyCommon proxy;
	
	public static ChannelHandler packetPipeline;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.init(event.getSuggestedConfigurationFile());
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.registerRecipes();
		proxy.registerNetwork();
		proxy.registerEventHandlers();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (Config.printMoney) VivecraftForgeLog.warning(Util.getMoney());
	}
}
