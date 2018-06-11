package werewolvesAndVampires.core;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import werewolvesAndVampires.items.BaseItem;
import werewolvesAndVampires.items.ItemWerewolfTotem;

@ObjectHolder(WVCore.MODID)
public class WVItems {
	
	//Item References go here
	public static final BaseItem werewolf_totem = null;
	
	
	
	public static void regItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new ItemWerewolfTotem());
	}
	
	public static void regModels() {
		werewolf_totem.regModel();
	}
	
}
