package de.nedelosk.modularmachines.common.modules.storage;

import de.nedelosk.modularmachines.api.modular.IModular;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.api.modules.storage.IPositionedModuleStorage;
import de.nedelosk.modularmachines.api.modules.storaged.EnumStoragePosition;
import net.minecraft.item.ItemStack;

public class PositionedModuleStorage extends ModuleStorage implements IPositionedModuleStorage {

	protected EnumStoragePosition position;

	public PositionedModuleStorage(IModular modular, EnumStoragePosition position) {
		super(modular);

		this.position = position;
	}

	@Override
	public IModuleState addModule(ItemStack itemStack, IModuleState state) {
		if (state == null) {
			return null;
		}

		if (moduleStates.add(state)) {
			state.setIndex(modular.getNextIndex());
			return state;
		}
		return null;
	}

	@Override
	public EnumStoragePosition getPosition() {
		return position;
	}
}
