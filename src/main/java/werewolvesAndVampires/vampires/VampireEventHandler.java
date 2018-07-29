package werewolvesAndVampires.vampires;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import werewolvesAndVampires.blocks.BlockCoffin;
import werewolvesAndVampires.vampires.gui.GuiSleepCoffin;

@Mod.EventBusSubscriber
public class VampireEventHandler {

	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent e) {
		if (e.phase == TickEvent.Phase.END) {
			DaySleepHelper.checkSleepWorld(e.world);
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
		EntityPlayer player = e.player;
		World world = player.world;
		if (player.isPlayerSleeping() && BlockCoffin.isPlayerSleepingInCoffin(player)) {
			player.noClip = true;
			player.motionX = player.motionY = player.motionZ = 0;
			if (!world.isRemote) {
				IBlockState state = world.getBlockState(player.bedLocation);
				boolean bed = state.getBlock().isBed(state, world, player.bedLocation, player);

				if (!bed) {
					player.wakeUpPlayer(true, true, false);
				} else if (!world.isDaytime()) {
					player.wakeUpPlayer(false, true, true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerSleep(PlayerSleepInBedEvent e) {
		if (e.getEntity().world.getBlockState(e.getPos()).getBlock() instanceof BlockCoffin) {
			e.setResult(VampireHelpers.trySleep(e.getEntityPlayer(), e.getPos()));
		}
	}

	@SubscribeEvent
	public static void onPlayerWakeUp(PlayerWakeUpEvent e) {
		DaySleepHelper.updateAllPlayersSleeping(e.getEntityPlayer().world);
	}

	@SubscribeEvent
	public static void onGuiOpen(GuiOpenEvent e) {
		Minecraft mc = Minecraft.getMinecraft();
		if (e.getGui() == null && (mc.currentScreen instanceof GuiSleepMP && !(mc.currentScreen instanceof GuiSleepCoffin)) && BlockCoffin.isPlayerSleepingInCoffin(mc.player)) {
			e.setGui(new GuiSleepCoffin());
		}
	}
}
