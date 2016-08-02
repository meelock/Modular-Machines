package de.nedelosk.modularmachines.api.modules.storaged.tools;

import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.api.recipes.IToolMode;

public interface IModuleMachineAdvanced extends IModuleMachine {

	Class<? extends IToolMode> getModeClass();

	IToolMode getCurrentMode(IModuleState state);

	void setCurrentMode(IModuleState state, IToolMode mode);
}