package com.aguila.wavm.init;

import com.aguila.wavm.core.WVCore;
import com.aguila.wavm.items.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

@ObjectHolder(WVCore.MODID)
public class WVItems {

	private static List<Item> items = new ArrayList<>();

	//Item References go here
	public static final BaseItem WEREWOLF_TOTEM = (BaseItem) addItem(new ItemWerewolfTotem());
	public static final BaseItem WOLFSBANE_DUST = (BaseItem) addItem(new ItemWolfsbaneDust());

	// Food
	public static final BaseItemFood NETHER_APPLE = (BaseItemFood) addItem(new ItemNetherApple());
	public static final ItemGarlic GARLIC = (ItemGarlic) addItem(new ItemGarlic());
	public static final ItemGarlicSoup GARLIC_SOUP = (ItemGarlicSoup) addItem(new ItemGarlicSoup());

	// Test items
	public static final BaseItem WW_TEST_ITEM = (BaseItem) addItem(new WWTestItem());

	private static Item addItem(Item item) {
		items.add(item);
		return item;
	}


	public static void regItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> reg = event.getRegistry();
		reg.registerAll(
				items.toArray(new Item[items.size()])
		);

	}

	@SideOnly(Side.CLIENT)
	public static void regModels() {
		for (Item item : items) {
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}
	
}
