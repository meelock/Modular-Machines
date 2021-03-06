package de.nedelosk.modularmachines.common.plugins.ic2;

import java.util.Collections;
import java.util.List;

import de.nedelosk.modularmachines.api.modules.IModelInitHandler;
import de.nedelosk.modularmachines.api.modules.items.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.models.IModelHandler;
import de.nedelosk.modularmachines.api.modules.models.ModelHandler;
import de.nedelosk.modularmachines.api.modules.models.ModelHandlerDefault;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.common.modules.storages.ModuleBattery;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModuleEUBattery extends ModuleBattery {

	public ModuleEUBattery() {
		super("euBattery");
	}

	@Override
	public List<IModelInitHandler> getInitModelHandlers(IModuleContainer container) {
		return Collections.singletonList(new ModelHandlerDefault(name, container, ModelHandler.getModelLocation(container, name, getSize(container))));
	}

	@Override
	public IModelHandler createModelHandler(IModuleState state) {
		return new ModelHandlerDefault(name, state.getContainer(), ModelHandler.getModelLocation(state.getContainer(), name, getSize(state.getContainer())));
	}

	@Override
	protected boolean showName(IModuleContainer container) {
		return false;
	}

	@Override
	protected boolean showMaterial(IModuleContainer container) {
		return false;
	}

	@Override
	public void saveEnergy(IModuleState state, long energy, ItemStack itemStack) {
		if(!itemStack.hasTagCompound()){
			itemStack.setTagCompound(new NBTTagCompound());
		}
		itemStack.getTagCompound().setDouble("energy", energy / 2);
	}

	@Override
	public long loadEnergy(IModuleState state, ItemStack itemStack) {
		if(!itemStack.hasTagCompound()){
			return 0;
		}
		return Double.valueOf(itemStack.getTagCompound().getDouble("energy") * 2).longValue();
	}
}
