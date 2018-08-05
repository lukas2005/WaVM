package werewolvesAndVampires.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import werewolvesAndVampires.blocks.BlockCoffin;

public class ItemCoffin extends ItemBlock {

	public ItemCoffin(Block block) {
		super(block);
		setRegistryName(block.getRegistryName());
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return EnumActionResult.SUCCESS;
		} else if (facing != EnumFacing.UP) {
			return EnumActionResult.FAIL;
		} else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);

			if (!flag) {
				pos = pos.up();
			}

			int i = MathHelper.floor((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			EnumFacing enumfacing = EnumFacing.getHorizontal(i);
			BlockPos blockpos = pos.offset(enumfacing);
			ItemStack itemstack = player.getHeldItem(hand);

			if (player.canPlayerEdit(pos, facing, itemstack) && player.canPlayerEdit(blockpos, facing, itemstack)) {
				IBlockState iblockstate1 = worldIn.getBlockState(blockpos);
				boolean flag1 = iblockstate1.getBlock().isReplaceable(worldIn, blockpos);
				boolean flag2 = flag || worldIn.isAirBlock(pos);
				boolean flag3 = flag1 || worldIn.isAirBlock(blockpos);

				if (flag2 && flag3 && worldIn.getBlockState(pos.down()).isTopSolid() && worldIn.getBlockState(blockpos.down()).isTopSolid()) {
					IBlockState iblockstate2 = this.block.getDefaultState()
							.withProperty(BlockCoffin.OCCUPIED, false)
							.withProperty(BlockCoffin.FACING, enumfacing)
							.withProperty(BlockCoffin.PART, BlockCoffin.EnumPartType.FOOT);

					worldIn.setBlockState(pos, iblockstate2, 10);
					worldIn.setBlockState(blockpos, iblockstate2.withProperty(BlockCoffin.PART, BlockCoffin.EnumPartType.HEAD), 10);

					SoundType soundtype = iblockstate2.getBlock().getSoundType(iblockstate2, worldIn, pos, player);
					worldIn.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

					worldIn.notifyNeighborsRespectDebug(pos, block, false);
					worldIn.notifyNeighborsRespectDebug(blockpos, iblockstate1.getBlock(), false);

					if (player instanceof EntityPlayerMP) {
						CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, itemstack);
					}

					itemstack.shrink(1);
					return EnumActionResult.SUCCESS;
				} else {
					return EnumActionResult.FAIL;
				}
			} else {
				return EnumActionResult.FAIL;
			}
		}
	}
}
