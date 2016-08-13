package de.nedelosk.modularmachines.api.modules.handlers.energy;

import de.nedelosk.modularmachines.api.energy.IEnergyInterface;
import de.nedelosk.modularmachines.api.energy.IEnergyType;
import de.nedelosk.modularmachines.api.modules.handlers.IModuleContentHandler;

public interface IModuleEnergyInterface extends IModuleContentHandler, IEnergyInterface {

	void setEnergyStored(IEnergyType energyType, long energy);

}
