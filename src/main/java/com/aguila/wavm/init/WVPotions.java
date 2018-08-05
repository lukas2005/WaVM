package com.aguila.wavm.init;

import com.aguila.wavm.core.WVCore;
import com.aguila.wavm.potions.PotionWWFever;
import com.aguila.wavm.potions.PotionWWHangover;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
@ObjectHolder(WVCore.MODID)
public class WVPotions {

	@ObjectHolder("ww_fever")
	public static Potion WW_FEVER = null;

	@ObjectHolder("ww_hangover")
	public static Potion WW_HANGOVER = null;

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> e) {
		IForgeRegistry<Potion> reg = e.getRegistry();

		reg.registerAll(
				new PotionWWFever(),
				new PotionWWHangover()
		);
	}

}
