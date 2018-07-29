package werewolvesAndVampires.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import werewolvesAndVampires.core.WVCore;
import werewolvesAndVampires.tileentity.TileEntityCoffin;
import werewolvesAndVampires.vampires.VampireHelpers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Block coffin.
 */
public class BlockCoffin extends BlockContainer {

	public static final PropertyDirection FACING        = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyEnum<EnumPartType> PART = PropertyEnum.create("part", EnumPartType.class);
	public static final PropertyBool OCCUPIED           = PropertyBool.create("occupied");

	public static boolean isPlayerSleepingInCoffin(EntityPlayer player) {
		return player.world.getBlockState(player.getPosition()).getBlock() instanceof BlockCoffin;
	}

	public static boolean isOccupied(IBlockAccess world, BlockPos pos) {
		return world.getBlockState(pos).getValue(OCCUPIED);
	}

	public static void setCoffinOccupied(World world, BlockPos pos, boolean value) {
		IBlockState state = world.getBlockState(pos);
		world.setBlockState(pos, state.withProperty(OCCUPIED, value), 4);
	}

	public static boolean isHead(IBlockAccess world, BlockPos pos) {
		return world.getBlockState(pos).getValue(PART) == EnumPartType.HEAD;
	}

	public BlockCoffin() {
		super(Material.WOOD);
		this.setDefaultState(
				this.blockState.getBaseState()
						.withProperty(PART, EnumPartType.FOOT)
						.withProperty(OCCUPIED, false)
						.withProperty(FACING, EnumFacing.NORTH)
		);

		setRegistryName(new ResourceLocation(WVCore.MODID, "coffin"));
		setUnlocalizedName(WVCore.MODID + ".coffin");
		setCreativeTab(WVCore.ctab);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return state.getValue(PART) == EnumPartType.HEAD;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCoffin();
	}

	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
		if (state.getValue(PART) == EnumPartType.FOOT) {
			super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
		}
	}

	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if (state.getValue(PART) == EnumPartType.FOOT) {
			IBlockState iblockstate = worldIn.getBlockState(pos.offset(state.getValue(FACING)));

			if (iblockstate.getBlock() == this) {
				state = state.withProperty(OCCUPIED, iblockstate.getValue(OCCUPIED));
			}
		}

		return state;
	}

	@Nonnull
	@Override
	public EnumFacing getBedDirection(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
		return getActualState(state, world, pos).getValue(FACING);
	}

	@Nonnull
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	public ItemStack getItem(World worldIn, BlockPos pos, @Nonnull IBlockState state) {
		return new ItemStack(Item.getItemFromBlock(this));
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(PART) == EnumPartType.HEAD ? null : Item.getItemFromBlock(this);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | state.getValue(FACING).getHorizontalIndex();

		if (state.getValue(PART) == EnumPartType.HEAD) {
			i |= 8;

			if (state.getValue(OCCUPIED)) {
				i |= 4;
			}
		}

		return i;
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
		return (meta & 8) > 0 ? this.getDefaultState().withProperty(PART, EnumPartType.HEAD).withProperty(FACING, enumfacing).withProperty(OCCUPIED, (meta & 4) > 0) : this.getDefaultState().withProperty(PART, EnumPartType.FOOT).withProperty(FACING, enumfacing);
	}

	@Override
	public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player) {
		return true;
	}

	@Override
	public boolean isBedFoot(IBlockAccess world, @Nonnull BlockPos pos) {
		return getActualState(world.getBlockState(pos), world, pos).getValue(PART) == EnumPartType.FOOT;
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}

	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}


	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		EnumFacing enumfacing = state.getValue(FACING);

		if (state.getValue(PART) == EnumPartType.HEAD) {
			if (worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() != this) {
				worldIn.setBlockToAir(pos);
			}
		} else if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this) {
			worldIn.setBlockToAir(pos);

			if (!worldIn.isRemote) {
				this.dropBlockAsItem(worldIn, pos, state, 0);
			}
		}
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return true;
		} else {
			if (state.getValue(PART) != EnumPartType.HEAD) {
				pos = pos.offset(state.getValue(FACING));
				state = worldIn.getBlockState(pos);

				if (state.getBlock() != this) {
					return true;
				}
			}

			if (worldIn.provider.canRespawnHere() && worldIn.getBiome(pos) != Biomes.HELL) {
				if (state.getValue(OCCUPIED)) {
					EntityPlayer entityplayer = this.getPlayerInCoffin(worldIn, pos);

					if (entityplayer != null) {
						playerIn.sendMessage(new TextComponentTranslation("text."+WVCore.MODID+".coffin.occupied"));
						return true;
					}

					state = state.withProperty(OCCUPIED, false);
					worldIn.setBlockState(pos, state, 2);
				}

				EntityPlayer.SleepResult entityplayer$enumstatus = playerIn.trySleep(pos);

				if (entityplayer$enumstatus == EntityPlayer.SleepResult.OK) {
					state = state.withProperty(OCCUPIED, true);
					worldIn.setBlockState(pos, state, 2);
					return true;
				} else {
					if (entityplayer$enumstatus == EntityPlayer.SleepResult.NOT_POSSIBLE_NOW) {
						playerIn.sendMessage(new TextComponentTranslation("text."+WVCore.MODID+".coffin.no_sleep"));
					} else if (entityplayer$enumstatus == EntityPlayer.SleepResult.NOT_SAFE) {
						playerIn.sendMessage(new TextComponentTranslation("tile.bed.notSafe"));
					}

					return true;
				}
			} else {
				playerIn.sendMessage(new TextComponentTranslation("text."+WVCore.MODID+".coffin.wrong_dimension"));
				return true;
			}
		}
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (player.capabilities.isCreativeMode && state.getValue(PART) == EnumPartType.HEAD) {
			BlockPos blockpos = pos.offset(state.getValue(FACING).getOpposite());

			if (worldIn.getBlockState(blockpos).getBlock() == this) {
				worldIn.setBlockToAir(blockpos);
			}
		}
	}

	@Override
	public void setBedOccupied(IBlockAccess world, BlockPos pos, EntityPlayer player, boolean occupied) {
		if (world instanceof World) {
			IBlockState state = world.getBlockState(pos);
			state = state.getBlock().getActualState(state, world, pos);
			state = state.withProperty(OCCUPIED, occupied);//In forge 12.16.0.1859-1.9 the vanilla method of this is even wrong, setting it always to true
			((World) world).setBlockState(pos, state, 2);
		}
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, PART, OCCUPIED);
	}

	/**
	 * Finds the player that is currently sleeping in this coffin
	 *
	 * @param worldIn
	 * @param pos
	 * @return
	 */
	private @Nullable
	EntityPlayer getPlayerInCoffin(World worldIn, BlockPos pos) {
		for (EntityPlayer entityplayer : worldIn.playerEntities) {
			if (entityplayer.isPlayerSleeping() && entityplayer.bedLocation.equals(pos)) {
				return entityplayer;
			}
		}

		return null;
	}


	public enum EnumPartType implements IStringSerializable {
		HEAD("head"),
		FOOT("foot");

		private final String name;

		EnumPartType(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public String toString() {
			return this.name;
		}
	}


}