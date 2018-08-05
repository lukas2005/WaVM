package com.aguila.wavm.items;

import com.aguila.wavm.capability.werewolf.IWerewolf;
import com.aguila.wavm.capability.werewolf.WerewolfProvider;
import com.aguila.wavm.utils.WerewolfHelpers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemWerewolfTotem extends BaseItem {
	
	public ItemWerewolfTotem() {
		super("werewolf_totem");
		this.setCreativeTab(CreativeTabs.MISC);
		this.setMaxDamage(10);
		this.maxStackSize = 1;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote) {
		IWerewolf were = playerIn.getCapability(WerewolfProvider.WEREWOLF_CAP, null);
		WerewolfHelpers.transformEntity(playerIn, were, !were.getIsTransformed());
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
}
