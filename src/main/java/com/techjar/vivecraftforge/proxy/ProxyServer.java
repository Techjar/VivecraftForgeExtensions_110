package com.techjar.vivecraftforge.proxy;

import com.techjar.vivecraftforge.Config;
import com.techjar.vivecraftforge.eventhandler.EventHandlerServer;
import com.techjar.vivecraftforge.network.ChannelHandler;

import com.techjar.vivecraftforge.VivecraftForge;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ProxyServer extends ProxyCommon {
	@Override
	public void registerEventHandlers() {
		super.registerEventHandlers();
		MinecraftForge.EVENT_BUS.register(new EventHandlerServer());
		FMLCommonHandler.instance().bus().register(new EventHandlerServer());
	}
	
	@Override
	public void registerNetwork() {
		super.registerNetwork();
		VivecraftForge.packetPipeline = ChannelHandler.init();
	}
}
