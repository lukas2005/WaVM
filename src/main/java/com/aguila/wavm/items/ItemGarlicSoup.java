package com.aguila.wavm.items;

import com.aguila.wavm.core.WVCore;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGarlicSoup extends ItemSoup {

	public ItemGarlicSoup() {
		super(5);
		setRegistryName("garlic_soup");
		setUnlocalizedName(WVCore.MODID + ".garlic_soup");
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {

	}

}
