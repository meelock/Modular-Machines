package nedelosk.modularmachines.common.items;

import nedelosk.modularmachines.common.core.tabs.TabModularMachines;
import net.minecraft.item.Item;

public class ModularItem extends Item {

	public ModularItem(String uln) {
		setCreativeTab(TabModularMachines.instance);
		setUnlocalizedName(uln);
	}
	
}