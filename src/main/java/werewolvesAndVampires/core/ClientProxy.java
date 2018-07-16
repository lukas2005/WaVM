package werewolvesAndVampires.core;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		WVEntities.initRender();
	}

	@Override
	public World getWorld() {
		return Minecraft.getMinecraft().world;
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		WVItems.regModels();
		WVBlocks.regModels();
	}

}
