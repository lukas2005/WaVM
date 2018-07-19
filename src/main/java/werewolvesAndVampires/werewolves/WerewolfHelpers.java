package werewolvesAndVampires.werewolves;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import werewolvesAndVampires.core.WVPotions;
import werewolvesAndVampires.packets.PacketRegister;
import werewolvesAndVampires.packets.SyncWerewolfCap;
import werewolvesAndVampires.werewolves.capability.IWerewolf;
import werewolvesAndVampires.werewolves.entity.EntityAngryPlayer;

public class WerewolfHelpers {
	public static Random rand = new Random();
	public static Map<String,Integer> playerToWereForm = new HashMap<String,Integer>();
	
	public static void transformEntity(EntityLivingBase p, IWerewolf were, boolean transform) {
		were.setIsTransformed(transform);
		PacketRegister.INSTANCE.sendToDimension(new SyncWerewolfCap(were, p), p.dimension);
		WorldServer ws = (WorldServer) p.world;
		int i = 0;
		while (i < 450) {
			ws.spawnParticle(EnumParticleTypes.CLOUD, p.posX + rand.nextGaussian(),
					p.posY + rand.nextGaussian(), p.posZ + rand.nextGaussian(), 1, 0, 1, 0, 0,
					null);
			++i;
		}

		p.addPotionEffect(new PotionEffect(WVPotions.WW_HANGOVER, 1, 0, false, false));
	}
	
	public static void loseControl(EntityPlayer p) {
		EntityAngryPlayer ap = new EntityAngryPlayer(p.world, p);
		p.world.spawnEntity(ap);
	}
	
	public static void gainControl(EntityPlayer p) {
		p.world.getEntityByID(playerToWereForm.get(p.getName())).setDead();
	}

	public static long timeUntilFullMoon(World world) {
		if (world != null) {
			long worldTime = world.getWorldTime();

			MoonPhase moonPhase = MoonPhase.byOrdinal(world.getMoonPhase());

			long timeSinceBeginningOfDay = (long) (worldTime - Math.floor(worldTime / 24000) * 24000);
			long timeUntilMoonRises = 12566 - timeSinceBeginningOfDay;

			return moonPhase == MoonPhase.FULL ? (timeUntilMoonRises < 0 ? 0 : timeUntilMoonRises) : (moonPhase.getDaysToFullMoon() * 24000 + (24000 - 12566)) - timeSinceBeginningOfDay;
		}
		return  1;
	}
}
