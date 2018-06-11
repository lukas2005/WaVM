package werewolvesAndVampires.werewolves;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import werewolvesAndVampires.packets.PacketRegister;
import werewolvesAndVampires.packets.SyncWerewolfCap;
import werewolvesAndVampires.werewolves.capability.IWerewolf;

public class WerewolfHelpers {
	private static Random rand = new Random();
	
	public static void TransformPlayer(EntityPlayer p, IWerewolf were, boolean transform) {
		were.setIsTransformed(transform);
		PacketRegister.INSTANCE.sendTo(new SyncWerewolfCap(were), (EntityPlayerMP) p);
		WorldServer ws = (WorldServer) p.world;
		int i = 0;
		while (i < 450) {
			ws.spawnParticle(EnumParticleTypes.CLOUD, p.posX + rand.nextGaussian(),
					p.posY + rand.nextGaussian(), p.posZ + rand.nextGaussian(), 1, 0, 1, 0, 0,
					null);
			++i;
		}
	}
	
}
