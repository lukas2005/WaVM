package com.aguila.wavm.core;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ServerProxy extends CommonProxy {

	@Override
	public World getWorld() {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
	}
}
