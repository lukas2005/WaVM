package com.aguila.wavm.init;

import com.aguila.wavm.core.WVCore;
import com.aguila.wavm.entity.EntityVampire;
import com.aguila.wavm.entity.EntityWerewolf;
import com.aguila.wavm.render.entity.RenderEntityVampire;
import com.aguila.wavm.render.entity.RenderEntityWerewolf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WVEntities {
	
	public static void init() {
	    int id = 1;
	    EntityRegistry.registerModEntity(new ResourceLocation(WVCore.MODID ,"werewolf_entity"), EntityWerewolf.class, "werewolf_entity", id++, WVCore.instance, 64, 3, true, 0xcc0c0c, 0x665b5b);
		EntityRegistry.registerModEntity(new ResourceLocation(WVCore.MODID ,"vampire_entity"), EntityVampire.class, "vampire_entity", id++, WVCore.instance, 64, 3, true, 0xcc0c0c, 0x665b5b);
	}
	
	@SideOnly(Side.CLIENT)
    public static void initRender() {
		RenderingRegistry.registerEntityRenderingHandler(EntityWerewolf.class, RenderEntityWerewolf.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityVampire.class, RenderEntityVampire.FACTORY);
	}
}
