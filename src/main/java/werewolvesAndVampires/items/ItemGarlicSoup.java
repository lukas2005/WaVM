package werewolvesAndVampires.items;

import net.minecraft.item.ItemSoup;
import werewolvesAndVampires.core.WVCore;

public class ItemGarlicSoup extends ItemSoup {

	public ItemGarlicSoup() {
		super(5);
		setRegistryName("garlic_soup");
		setUnlocalizedName(WVCore.MODID + ".garlic_soup");
		setCreativeTab(WVCore.ctab);
	}

}
