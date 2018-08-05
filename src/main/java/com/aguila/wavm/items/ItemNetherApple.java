package com.aguila.wavm.items;

import com.aguila.wavm.capability.werewolf.IWerewolf;
import com.aguila.wavm.capability.werewolf.WerewolfProvider;
import com.aguila.wavm.capability.werewolf.WerewolfType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemNetherApple extends BaseItemFood {

	public ItemNetherApple() {
		super(1, false, "nether_apple");
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);

		IWerewolf were = player.getCapability(WerewolfProvider.WEREWOLF_CAP, null);
		were.setWerewolfType(WerewolfType.IMMUNE);
	}
}
