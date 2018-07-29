package werewolvesAndVampires.vampires;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import werewolvesAndVampires.blocks.BlockCoffin;

import java.util.HashMap;
import java.util.Map;

/**
 * Handle coffin sleep during the day.
 * {@link DaySleepHelper#updateAllPlayersSleeping(World)} has to be called every time a player leaves/enters a coffin or leaves/enters the world.
 */
public class DaySleepHelper {
	private static final Map<Integer, Boolean> enoughPlayersAsleep = new HashMap<>();

	public static int coffinSleepPercentage = 51;

	/**
	 * Updates the all players sleeping flag
	 *
	 * @param world
	 */
	public static void updateAllPlayersSleeping(World world) {
		updateAllPlayersSleeping(world, 0);
	}

	/**
	 * Updates the all players sleeping flag
	 *
	 * @param world
	 * @param ignorePlayers This many players will be ignored for calculation. Used for log out event
	 */
	public static void updateAllPlayersSleeping(World world, int ignorePlayers) {
		if (!world.playerEntities.isEmpty()) {
			int spectators = 0;
			int sleeping = 0;
			int all = 0;
			for (EntityPlayer player : world.playerEntities) {
				all++;
				if (player.isSpectator()) {
					++spectators;
				} else if (player.isPlayerSleeping() && BlockCoffin.isPlayerSleepingInCoffin(player)) {
					++sleeping;
				}
			}
			boolean enough = sleeping > 0 && sleeping / ((float) all - spectators - ignorePlayers) * 100 >= coffinSleepPercentage;
			enoughPlayersAsleep.put(world.provider.getDimension(), enough);
		}
	}

	/**
	 * Check if all players are fully asleep and if so make it night and wake all players
	 *
	 * @param world
	 */
	public static void checkSleepWorld(World world) {
		if (enoughPlayersAsleep.get(world.provider.getDimension()) == Boolean.TRUE) {
			int sleeping = 0;
			int total = 0;
			for (EntityPlayer entityplayer : world.playerEntities) {
				if (!entityplayer.isSpectator()) {
					total++;
					if (entityplayer.isPlayerFullyAsleep()) {
						sleeping++;
					}
				}
			}
			if (sleeping / (float) total * 100 < coffinSleepPercentage) return;
			if (world.getGameRules().getBoolean("doDaylightCycle")) {
				long i = world.getWorldInfo().getWorldTime() + 24000L;
				world.getWorldInfo().setWorldTime(i - i % 24000L + 12700L);
			}

			wakeAllPlayers(world);
		}
	}

	/**
	 * Wake all sleeping vampire player
	 *
	 * @param world
	 */
	public static void wakeAllPlayers(World world) {
		enoughPlayersAsleep.put(world.provider.getDimension(), Boolean.FALSE);

		for (EntityPlayer entityplayer : world.playerEntities) {
			if (entityplayer.isPlayerSleeping()) {
				entityplayer.wakeUpPlayer(false, false, true);
			}
		}

		if (!world.isRemote) {
			world.provider.resetRainAndThunder();
		}
	}
}