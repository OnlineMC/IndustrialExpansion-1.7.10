package cf.brforgers.mods.IndustrialExpansion.items;

import cf.brforgers.mods.IndustrialExpansion.IndustrialExpansion;
import cf.brforgers.mods.IndustrialExpansion.Lib;
import net.minecraft.item.Item;

public class DenseIridiumPlate extends Item{
    public DenseIridiumPlate()
    {
        this.setUnlocalizedName("DenseIridiumPlate");
        this.setTextureName(Lib.RESOURCE_PATH + "DenseIridiumPlate");
        this.setCreativeTab(IndustrialExpansion.tabIndustrialExpansion);
    }
}
