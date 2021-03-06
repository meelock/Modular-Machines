package de.nedelosk.modularmachines.common.items;

import java.util.List;

import de.nedelosk.modularmachines.common.core.Registry;
import de.nedelosk.modularmachines.common.core.TabModularMachines;
import forestry.api.core.IItemModelRegister;
import forestry.api.core.IModelManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemModuleMeta extends Item implements IItemModelRegister {

	public String[] names;
	public String uln;

	public ItemModuleMeta(String uln, String[] names) {
		setCreativeTab(TabModularMachines.tabModularMachines);
		setUnlocalizedName(uln);
		setHasSubtypes(true);
		setCreativeTab(TabModularMachines.tabModules);
		this.names = names;
		this.uln = uln;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel(Item item, IModelManager manager) {
		for(int i = 0; i < names.length; ++i) {
			manager.registerItemModel(item, i, uln + "/" + names[i]);
		}
	}

	@Override
	public void getSubItems(Item id, CreativeTabs tab, List list) {
		for(int i = 0; i < names.length; i++) {
			list.add(new ItemStack(id, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return Registry.setUnlocalizedItemName(uln + "_" + names[itemstack.getItemDamage()]);
	}
}
