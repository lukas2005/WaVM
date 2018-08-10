package com.aguila.wavm.items;

import com.aguila.wavm.core.WVCore;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseItemFood extends ItemFood {

	public BaseItemFood(int amount, float saturation, boolean isWolfFood, String name) {
		super(amount, saturation, isWolfFood);
		setRegistryName(name);
		setUnlocalizedName(WVCore.MODID + "." + name);
		setCreativeTab(WVCore.ctab);
	}

	public BaseItemFood(int amount, boolean isWolfFood, String name) {
		this(amount, 0.6F, isWolfFood, name);
	}

}
