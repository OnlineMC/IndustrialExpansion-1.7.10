package cf.brforgers.mods.IndustrialExpansion.items;

import cf.brforgers.mods.IndustrialExpansion.IndustrialExpansion;
import cf.brforgers.mods.IndustrialExpansion.Lib;
import net.minecraft.item.Item;

public class IridiumPlate extends Item{

	public IridiumPlate()
    {
        this.setUnlocalizedName("IridiumPlate");
        this.setTextureName(Lib.RESOURCE_PATH + "IridiumPlate");
        this.setCreativeTab(IndustrialExpansion.tabIndustrialExpansion);
    }
}
