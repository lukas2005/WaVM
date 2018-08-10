package com.aguila.wavm.utils;

import com.aguila.wavm.capability.werewolf.IWerewolf;
import com.aguila.wavm.init.WVItems;
import com.aguila.wavm.init.WVPotions;
import com.aguila.wavm.packets.PacketRegister;
import com.aguila.wavm.packets.SyncWerewolfCap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;
import java.util.Random;

public class WerewolfHelpers {
	public static Random rand = new Random();

	public static void transformEntity(EntityLivingBase p, IWerewolf were, boolean transform) {
		were.setIsTransformed(transform);
		if (p instanceof EntityPlayer) {
			EntityPlayer pl = (EntityPlayer) p;
			for (int i = 0; i < pl.inventory.mainInventory.size(); ++i) {
				if (pl.inventory.mainInventory.get(i).getItem() == WVItems.WEREWOLF_TOTEM) {
					pl.dropItem(pl.inventory.mainInventory.get(i), true, false);
					pl.inventory.mainInventory.set(i, ItemStack.EMPTY);
				}
			}
			for (int i = 0; i < pl.inventory.armorInventory.size(); ++i) {
				if (pl.inventory.armorInventory.get(i).getItem() == WVItems.WEREWOLF_TOTEM) {
					pl.inventory.armorInventory.get(i).damageItem(Integer.MAX_VALUE, pl);
				}
			}
			if (doesPlayerHaveTotem(pl)) {
				were.setBloodLust(-1);
			} else {
				were.setBloodLust(0);
			}
		}

		// TODO Fix this
		if (!transform && p instanceof EntityPlayer)
			were.setBloodLust(0);

		WorldServer ws = (WorldServer) p.world;
		int i = 0;
		while (i < 250) {
			ws.spawnParticle(EnumParticleTypes.CLOUD, p.posX + rand.nextGaussian(), p.posY + rand.nextGaussian(),
					p.posZ + rand.nextGaussian(), 1, 0, 1, 0, 0, null);
			++i;
		}

		p.addPotionEffect(new PotionEffect(WVPotions.WW_HANGOVER, 1, 0, false, false));
		if (were.getIsTransformed()) {
			p.setEntityBoundingBox(p.getEntityBoundingBox().expand(0, 1, 0));
		} else {
			p.setEntityBoundingBox(p.getEntityBoundingBox().contract(0, 1, 0));
		}
		PacketRegister.INSTANCE.sendToDimension(new SyncWerewolfCap(were, p), p.dimension);
	}

	public static long timeUntilFullMoon(World world) {
		if (world != null) {
			long worldTime = world.getWorldTime();

			MoonPhase moonPhase = MoonPhase.byOrdinal(world.getMoonPhase());

			long timeSinceBeginningOfDay = (long) (worldTime - Math.floor(worldTime / 24000) * 24000);
			long timeUntilMoonRises = 12566 - timeSinceBeginningOfDay;

			return moonPhase == MoonPhase.FULL ? (timeUntilMoonRises < 0 ? 0 : timeUntilMoonRises)
					: (moonPhase.getDaysToFullMoon() * 24000 + (24000 - 12566)) - timeSinceBeginningOfDay;
		}
		return 1;
	}

	public static void attackNearby(EntityPlayer p, IWerewolf were) {
		List<Entity> list = p.world.getEntitiesInAABBexcluding(p, p.getEntityBoundingBox().grow(2.5),
				EntitySelectors.CAN_AI_TARGET);
		if (!list.isEmpty())
			p.attackTargetEntityWithCurrentItem(list.get(0));
		were.setBloodLust(20);
		p.swingArm(EnumHand.MAIN_HAND);
	}

	public static void controlTick(TickEvent.PlayerTickEvent e, IWerewolf were) {
		if (were.getBloodLust() == 0) {
			attackNearby(e.player, were);
		} else if (were.getBloodLust() > 0) {
			were.setBloodLust(were.getBloodLust() - 1);
		}
	}

	public static boolean doesPlayerHaveTotem(EntityPlayer player) {
		for (ItemStack i : player.inventory.mainInventory) {
			if (i.getItem() == WVItems.WEREWOLF_TOTEM)
				return true;
		}
		if (player.inventory.offHandInventory.get(0).getItem() == WVItems.WEREWOLF_TOTEM)
			return true;
		return false;
	}
}
