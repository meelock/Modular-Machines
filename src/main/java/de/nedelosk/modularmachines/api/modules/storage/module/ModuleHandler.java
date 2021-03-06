package de.nedelosk.modularmachines.api.modules.storage.module;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import de.nedelosk.modularmachines.api.modules.IModule;
import de.nedelosk.modularmachines.api.modules.controller.IModuleControl;
import de.nedelosk.modularmachines.api.modules.controller.IModuleControlled;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;

public class ModuleHandler implements IModuleHandler {

	protected final IModuleStorage defaultStorage;
	protected final IModuleState state;
	protected final IModuleControl control;

	public ModuleHandler(@Nonnull IModuleStorage defaultStorage, @Nonnull IModuleState state) {
		this.defaultStorage = defaultStorage;
		this.state = state;
		if(state.getModule() instanceof IModuleControlled){
			this.control = ((IModuleControlled) state.getModule()).getModuleControl(state);
		}else{
			this.control = null;
		}
	}

	@Override
	public <M extends IModule> List<IModuleState<M>> getModules(Class<? extends M> moduleClass) {
		List<IModuleState<M>> modules = new ArrayList<>();
		List<IModuleState<M>> defaultModules = defaultStorage.getModules(moduleClass);
		for(IModuleState<M> state : defaultModules){
			if(control != null && control.hasPermission(state)){
				modules.add(state);
			}
		}
		return modules;
	}

	@Override
	public List<IModuleState> getModules() {
		List<IModuleState> modules = new ArrayList<>();
		List<IModuleState> defaultModules = defaultStorage.getModules();
		for(IModuleState state : defaultModules){
			if(control != null && control.hasPermission(state)){
				modules.add(state);
			}
		}
		return modules;
	}

	@Override
	public <M extends IModule> IModuleState<M> getModule(int index) {
		IModuleState<M> module = defaultStorage.getModule(index);
		if(control != null && control.hasPermission(module)){
			return module;
		}
		return null;
	}

	@Override
	public <M extends IModule> IModuleState<M> getModule(Class<? extends M> moduleClass) {
		IModuleState<M> module = defaultStorage.getModule(moduleClass);
		if(control != null && control.hasPermission(module)){
			return module;
		}
		return null;
	}

	@Override
	public IModuleState getState() {
		return state;
	}

	@Override
	public IModuleStorage getDefaultStorage() {
		return defaultStorage;
	}
}
