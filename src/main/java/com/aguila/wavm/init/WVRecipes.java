package com.aguila.wavm.init;

import com.aguila.wavm.core.WVCore;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WVRecipes {
	
	public static void init() {
		GameRegistry.addSmelting(WVBlocks.wolfsbane, new ItemStack(WVItems.WOLFSBANE_DUST), 1.0f);
		GameRegistry.addShapedRecipe(new ResourceLocation(WVCore.MODID, "wolfsbane_cake"), null, new ItemStack(WVBlocks.wolfsbane_cake),
				"MMM",
						 "DED",
						 "WWW",
				'M', Items.MILK_BUCKET,
				'D', WVItems.WOLFSBANE_DUST,
				'E', Items.EGG,
				'W', Items.WHEAT
		);

		GameRegistry.addShapedRecipe(new ResourceLocation(WVCore.MODID, "nether_apple"), null, new ItemStack(WVItems.NETHER_APPLE),
				"WSW",
					     "SAS",
					     "WSW",
				'W', Items.NETHER_WART,
				'S', Items.NETHER_STAR,
				'A', Items.APPLE
		);

		GameRegistry.addShapelessRecipe(new ResourceLocation(WVCore.MODID, "garlic_soup"), null, new ItemStack(WVItems.GARLIC_SOUP),
				Ingredient.fromItem(WVItems.GARLIC),
				Ingredient.fromItem(WVItems.GARLIC),
				Ingredient.fromItem(WVItems.GARLIC),
				Ingredient.fromItem(Items.BOWL)
		);
	}
	
}
