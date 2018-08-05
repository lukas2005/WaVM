package com.aguila.wavm.items;

import com.aguila.wavm.capability.werewolf.IWerewolf;
import com.aguila.wavm.capability.werewolf.WerewolfProvider;
import com.aguila.wavm.capability.werewolf.WerewolfType;
import com.aguila.wavm.utils.WerewolfHelpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class WWTestItem extends BaseItem {

	public WWTestItem() {
		super("ww_test_item");
		setCreativeTab(null);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		IWerewolf were = playerIn.getCapability(WerewolfProvider.WEREWOLF_CAP, null);

		if (!playerIn.isSneaking()) {
			were.setWerewolfType(WerewolfType.INFECTED);
		} else {
			were.setWerewolfType(WerewolfType.NONE);
			if (!worldIn.isRemote) WerewolfHelpers.transformEntity(playerIn, were, false);
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
}
