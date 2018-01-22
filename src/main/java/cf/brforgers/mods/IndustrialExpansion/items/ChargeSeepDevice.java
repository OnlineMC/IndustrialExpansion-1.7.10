package cf.brforgers.mods.IndustrialExpansion.items;

import cf.brforgers.mods.IndustrialExpansion.IndustrialExpansion;
import cf.brforgers.mods.IndustrialExpansion.Lib;
import net.minecraft.item.Item;

public class ChargeSeepDevice extends Item{
    public ChargeSeepDevice()
    {
        this.setUnlocalizedName("ChargeSeepDevice");
        this.setTextureName(Lib.RESOURCE_PATH + "ChargeSeepDevice");
        this.setCreativeTab(IndustrialExpansion.tabIndustrialExpansion);
    }
}
