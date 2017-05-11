package com.techjar.vivecraftforge.proxy;

import com.techjar.vivecraftforge.eventhandler.HandlerServerTick;
import com.techjar.vivecraftforge.network.VivecraftForgeChannelHandler;

import com.techjar.vivecraftforge.VivecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ProxyServer extends ProxyCommon {
	@Override
	public void registerEventHandlers() {
		FMLCommonHandler.instance().bus().register(new HandlerServerTick());
	}
	
	@Override
	public void registerNetwork() {
		super.registerNetwork();
		VivecraftForge.packetPipeline = VivecraftForgeChannelHandler.init();
	}
}
