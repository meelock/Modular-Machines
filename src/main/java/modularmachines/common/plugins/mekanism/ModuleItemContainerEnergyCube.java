package modularmachines.common.plugins.mekanism;

import modularmachines.api.material.IMaterial;
import modularmachines.api.modules.EnumModuleSizes;
import modularmachines.api.modules.containers.IModuleContainer;
import modularmachines.api.modules.containers.ModuleItemContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModuleItemContainerEnergyCube extends ModuleItemContainer {

	private final int tier;

	public ModuleItemContainerEnergyCube(IMaterial material, int tier, IModuleContainer... containers) {
		super(new ItemStack(PluginMekanism.energyCube), material, EnumModuleSizes.LARGE, true, containers);
		this.tier = tier;
	}

	@Override
	public boolean matches(ItemStack stackToTest) {
		boolean matches = super.matches(stackToTest);
		if (!matches) {
			return false;
		}
		if (!stackToTest.hasTagCompound()) {
			return false;
		}
		NBTTagCompound nbtTag = stackToTest.getTagCompound();
		if (!nbtTag.hasKey("tier")) {
			return false;
		}
		int nbtTier = nbtTag.getInteger("tier");
		if (nbtTier != tier) {
			return false;
		}
		return true;
	}
}