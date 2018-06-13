package werewolvesAndVampires.werewolves;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import werewolvesAndVampires.core.WVCore;
import werewolvesAndVampires.core.WVItems;
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
	
	@SideOnly(Side.CLIENT)
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

	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent e) {
		EntityPlayer p = e.player;
		IWerewolf were = p.getCapability(WerewolfProvider.WEREWOLF_CAP, null);
		if (e.side.isServer() && e.player.world.getCurrentMoonPhaseFactor() == 1F && !e.player.world.isDaytime()
				&& !e.player.inventory.hasItemStack(new ItemStack(WVItems.werewolf_totem))
				&& e.player.world.canBlockSeeSky(new BlockPos(e.player.posX, e.player.posY, e.player.posZ))) {

			if (!were.getIsTransformed()) {
				WerewolfHelpers.TransformPlayer(p, were, true);
			}
		} else if (e.side.isServer() && !e.player.inventory.hasItemStack(new ItemStack(WVItems.werewolf_totem))) {

			if (were.getIsTransformed()) {
				WerewolfHelpers.TransformPlayer(p, were, false);
			}
		}

		if (were.getIsTransformed()) {
			p.stepHeight = 1.25F;
			p.addPotionEffect(new PotionEffect(Potion.getPotionById(16), 300, 0, false, false));
			p.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8);
			p.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15000000149011612D);
		} else {
			p.stepHeight = 0.6F;
			p.removePotionEffect(Potion.getPotionById(16));
			p.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1);
			p.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.10000000149011612D);
		}
	}

	@SubscribeEvent
	public static void onFall(LivingFallEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer && !e.getEntity().world.isRemote) {
			EntityPlayer p = (EntityPlayer) e.getEntityLiving();
			IWerewolf were = p.getCapability(WerewolfProvider.WEREWOLF_CAP, null);
			if (were.getIsTransformed() && e.getDistance() < 5) {
				e.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onJump(LivingEvent.LivingJumpEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer && e.getEntity().world.isRemote) {
			EntityPlayer p = (EntityPlayer) e.getEntityLiving();
			IWerewolf were = p.getCapability(WerewolfProvider.WEREWOLF_CAP, null);
			if (were.getIsTransformed()) {
				p.motionY += 0.2;
			}
		}
	}

	@SubscribeEvent
	public static void onDamage(LivingHurtEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer && !e.getEntity().world.isRemote) {
			EntityPlayer p = (EntityPlayer) e.getEntityLiving();
			IWerewolf were = p.getCapability(WerewolfProvider.WEREWOLF_CAP, null);
			if (were.getIsTransformed()) {
				if (e.getAmount() < 4) {
					e.setCanceled(true);
				} else {
					e.setAmount(e.getAmount() / 2);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onJoin(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof EntityPlayer && !e.getWorld().isRemote) {
			PacketRegister.INSTANCE.sendTo(
					new SyncWerewolfCap(
							((EntityPlayerMP) e.getEntity()).getCapability(WerewolfProvider.WEREWOLF_CAP, null)),
					((EntityPlayerMP) e.getEntity()));
		}
	}

}
