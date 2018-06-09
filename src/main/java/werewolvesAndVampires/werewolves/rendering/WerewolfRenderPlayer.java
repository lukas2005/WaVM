package werewolvesAndVampires.werewolves.rendering;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import werewolvesAndVampires.core.WVCore;

public class WerewolfRenderPlayer extends RenderLivingBase<EntityPlayerSP> {
	
	private ResourceLocation calmTexture = new ResourceLocation(WVCore.MODID, "textures/entity/werewolf.png");
	private ResourceLocation angryTexture = new ResourceLocation(WVCore.MODID, "textures/entity/werewolfangry.png");
	
	public WerewolfRenderPlayer(RenderManager renderManager) {
		super(renderManager, new WerewolfModel(), 0.5F);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityPlayerSP entity) {
		return calmTexture;
	}
}
