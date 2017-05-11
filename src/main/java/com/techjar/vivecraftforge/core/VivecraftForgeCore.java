package com.techjar.vivecraftforge.core;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class VivecraftForgeCore extends DummyModContainer {
	public VivecraftForgeCore() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "vivecraftforgeextensionscore";
		meta.name = "Vivecraft Forge Extensions Core";
		meta.version = "@VERSION@"; //String.format("%d.%d.%d.%d", majorVersion, minorVersion, revisionVersion, buildVersion);
		meta.credits = "blah";
		meta.authorList = Arrays.asList("Techjar");
		meta.description = "";
		meta.url = "https://github.com/Techjar/VivecraftForgeExtensions_110";
		meta.screenshots = new String[0];
		meta.logoFile = "";
	}
	
	@Override
    public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
    }
}
