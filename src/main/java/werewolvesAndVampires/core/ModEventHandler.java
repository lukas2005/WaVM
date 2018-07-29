package werewolvesAndVampires.core;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ModEventHandler {

	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load e) {
		e.getWorld().addEventListener(new ModWorldEventListener());
	}

}
