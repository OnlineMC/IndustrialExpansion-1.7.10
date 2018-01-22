package cf.brforgers.mods.IndustrialExpansion.items;

import cf.brforgers.mods.IndustrialExpansion.IndustrialExpansion;
import cf.brforgers.mods.IndustrialExpansion.Lib;
import net.minecraft.item.Item;

public class SeepedIridiumPlate extends Item{
    public SeepedIridiumPlate() {
        this.setUnlocalizedName("SeepedIridiumPlate");
        this.setTextureName(Lib.RESOURCE_PATH + "SeepedIridiumPlate");
        this.setCreativeTab(IndustrialExpansion.tabIndustrialExpansion);
    }
}
