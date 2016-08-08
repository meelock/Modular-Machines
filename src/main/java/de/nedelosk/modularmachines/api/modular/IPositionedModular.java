package de.nedelosk.modularmachines.api.modular;

import de.nedelosk.modularmachines.api.modular.handlers.IModularHandler;
import de.nedelosk.modularmachines.api.modules.storage.IPositionedModuleStorage;
import de.nedelosk.modularmachines.api.modules.storaged.EnumPosition;

public interface IPositionedModular extends IModular {

	void setModuleStorage(EnumPosition position, IPositionedModuleStorage storage);

	IPositionedModuleStorage getModuleStorage(EnumPosition position);

	@Override
	IPositionedModularAssembler disassemble();

	@Override
	IPositionedModular copy(IModularHandler handler);

}
