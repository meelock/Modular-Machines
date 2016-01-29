package nedelosk.modularmachines.api.modules.energy;

import nedelosk.modularmachines.api.modules.IModuleAddable;
import nedelosk.modularmachines.api.modules.basic.IModuleWithRenderer;

public interface IModuleBattery<S extends IModuleBatterySaver> extends IModuleAddable<S>, IModuleWithRenderer<S> {
}