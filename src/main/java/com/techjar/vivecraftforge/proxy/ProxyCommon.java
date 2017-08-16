package com.techjar.vivecraftforge.proxy;

import com.techjar.vivecraftforge.Config;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ProxyCommon {
	public void registerEventHandlers() {
	}
	
	public void registerNetwork() {
	}

	public void registerRecipes() {
		if (Config.climbeyEnabled || FMLLaunchHandler.side() == Side.CLIENT) {
			ItemStack jumpBoots = new ItemStack(Items.LEATHER_BOOTS).setStackDisplayName("Jump Boots");
			jumpBoots.getTagCompound().setBoolean("Unbreakable", true);
			jumpBoots.getTagCompound().setInteger("HideFlags", 4);
			GameRegistry.addShapedRecipe(new ResourceLocation("vivecraftforgeextensions:jump_boots"), null, jumpBoots, "B", "S", 'S', Blocks.SLIME_BLOCK, 'B', Items.LEATHER_BOOTS);
			ItemStack climbClaws = new ItemStack(Items.SHEARS).setStackDisplayName("Climb Claws");
			climbClaws.getTagCompound().setBoolean("Unbreakable", true);
			climbClaws.getTagCompound().setInteger("HideFlags", 4);
			GameRegistry.addShapedRecipe(new ResourceLocation("vivecraftforgeextensions:climb_claws"), null, climbClaws, "S S", "C C", 'S', Items.SPIDER_EYE, 'C', Items.SHEARS);
		}
	}
}
