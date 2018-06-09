package werewolvesAndVampires.werewolves;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import werewolvesAndVampires.core.WVCore;
import werewolvesAndVampires.packets.PacketRegister;
import werewolvesAndVampires.packets.SyncWerewolfCap;
import werewolvesAndVampires.werewolves.capability.IWerewolf;
import werewolvesAndVampires.werewolves.capability.WerewolfProvider;
import werewolvesAndVampires.werewolves.rendering.WerewolfRenderPlayer;

@Mod.EventBusSubscriber
public class WerewolfEventhandler {

	public static final ResourceLocation werewolfCapLoc = new ResourceLocation(WVCore.MODID, "werewolf");

	@SideOnly(Side.CLIENT)
	private static WerewolfRenderPlayer wereRender = null;

	@SubscribeEvent
	public static void attachCapabilitys(AttachCapabilitiesEvent<Entity> e) {
		if (e.getObject() instanceof EntityPlayer) {
			e.addCapability(werewolfCapLoc, new WerewolfProvider());
		}
	}

	@SubscribeEvent
	public static void renderPlayer(RenderPlayerEvent.Pre e) {
		IWerewolf were = e.getEntityPlayer().getCapability(WerewolfProvider.WEREWOLF_CAP, null);
		if (were.getIsTransformed()) {
			e.setCanceled(true);
			if (wereRender == null)
				wereRender = new WerewolfRenderPlayer(Minecraft.getMinecraft().getRenderManager());
			wereRender.doRender((EntityPlayerSP) e.getEntityPlayer(), e.getX(), e.getY(), e.getZ(),
					e.getEntityPlayer().rotationYaw, e.getPartialRenderTick());
		}
	}

			/*EntityPlayer p = (EntityPlayer) e.getEntity();
			IWerewolf were = p.getCapability(WerewolfProvider.WEREWOLF_CAP, null);
			were.setIsTransformed(!were.getIsTransformed());
			PacketRegister.INSTANCE.sendTo(new SyncWerewolfCap(were), (EntityPlayerMP) p);*/
}
