package werewolvesAndVampires.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import werewolvesAndVampires.core.WVCore;

public class BaseBlock extends Block {

	public BaseBlock(Material blockMaterialIn, MapColor blockMapColorIn, String name) {
		super(blockMaterialIn, blockMapColorIn);
		setUnlocalizedName(WVCore.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(WVCore.ctab);
	}

	@SideOnly(Side.CLIENT)
	public void regModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
