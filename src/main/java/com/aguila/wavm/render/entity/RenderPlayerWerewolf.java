package com.aguila.wavm.render.entity;

import com.aguila.wavm.capability.werewolf.IWerewolf;
import com.aguila.wavm.capability.werewolf.WerewolfProvider;
import com.aguila.wavm.init.WVCore;
import com.aguila.wavm.model.WerewolfModel;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderPlayerWerewolf extends RenderLivingBase<EntityPlayerSP> {
	
	private ResourceLocation calmTexture = new ResourceLocation(WVCore.MODID, "textures/entity/werewolf.png");
	private ResourceLocation angryTexture = new ResourceLocation(WVCore.MODID, "textures/entity/werewolfangry.png");
	
	public RenderPlayerWerewolf(RenderManager renderManager) {
		super(renderManager, new WerewolfModel(), 0.5F);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityPlayerSP entity) {
		IWerewolf were = entity.getCapability(WerewolfProvider.WEREWOLF_CAP, null);
		return calmTexture;
	}
}
