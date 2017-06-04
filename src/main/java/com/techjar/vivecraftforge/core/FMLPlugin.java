package com.techjar.vivecraftforge.core;

import java.io.File;
import java.util.Map;

import com.techjar.vivecraftforge.core.asm.ClassTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name("Vivecraft Forge Extensions Core")
@IFMLLoadingPlugin.MCVersion("1.11.2")
@IFMLLoadingPlugin.TransformerExclusions("com.techjar.vivecraftforge.core")
//@SortingIndex(1001)
public class FMLPlugin implements IFMLLoadingPlugin {
	public static File location;

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{ClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return VivecraftForgeCore.class.getName();
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		location = (File)data.get("coremodLocation");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
