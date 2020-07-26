package com.techjar.vivecraftforge;

import com.techjar.vivecraftforge.eventhandler.EventHandlerServer;
import com.techjar.vivecraftforge.network.ChannelHandler;
import com.techjar.vivecraftforge.util.Util;
import com.techjar.vivecraftforge.util.LogHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;

@Mod("vivecraftforgeextensions")
public class VivecraftForge {
	public static IModInfo MOD_INFO;

	public VivecraftForge() {
		MOD_INFO = ModLoadingContext.get().getActiveContainer().getModInfo();
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> serverInit(eventBus));
	}

	private void serverInit(IEventBus eventBus) {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.config);
		eventBus.addListener(this::onSetup);
		MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
		MinecraftForge.EVENT_BUS.register(new EventHandlerServer());
	}

	private void onSetup(FMLCommonSetupEvent event) {
		ChannelHandler.init();
	}

	private void onServerStarting(FMLServerStartingEvent event) {
		if (Config.printMoney.get())
			LogHelper.warning(Util.getMoney());
	}
}
