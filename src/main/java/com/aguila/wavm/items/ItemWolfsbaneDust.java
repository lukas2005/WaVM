package com.aguila.wavm.items;

import com.aguila.wavm.init.WVBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWolfsbaneDust extends BaseItem {

	public ItemWolfsbaneDust() {
		super("wolfsbane_dust");
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
		BlockPos blockpos = flag ? pos : pos.offset(facing);
		ItemStack itemstack = player.getHeldItem(hand);

		if (player.canPlayerEdit(blockpos, facing, itemstack) && worldIn.mayPlace(worldIn.getBlockState(blockpos).getBlock(), blockpos, false, facing, null) && WVBlocks.wolfsbane_dust.canPlaceBlockAt(worldIn, blockpos)) {
			worldIn.setBlockState(blockpos, WVBlocks.wolfsbane_dust.getDefaultState());

			if (player instanceof EntityPlayerMP) {
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, blockpos, itemstack);
			}

			itemstack.shrink(1);
			return EnumActionResult.SUCCESS;
		} else {
			return EnumActionResult.FAIL;
		}
	}
}
