package de.nedelosk.modularmachines.common.modular.handlers;

import java.util.ArrayList;
import java.util.List;

import de.nedelosk.modularmachines.api.energy.IEnergyBuffer;
import de.nedelosk.modularmachines.api.modules.controller.IModuleControl;
import de.nedelosk.modularmachines.api.modules.controller.IModuleControlled;
import de.nedelosk.modularmachines.api.modules.handlers.IModuleContentHandler;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import net.minecraft.util.EnumFacing;

public class ModularEnergyBuffer<E extends IEnergyBuffer & IModuleContentHandler> implements IEnergyBuffer {

	public final List<E> buffers;

	public ModularEnergyBuffer(List<E> buffers) {
		this.buffers = buffers;
	}

	@Override
	public long extractEnergy(IModuleState moduleState, EnumFacing facing, long maxExtract, boolean simulate) {
		List<E> buffers = this.buffers;
		if(moduleState != null && moduleState.getModule() instanceof IModuleControlled){
			List<E> newBuffers = new ArrayList();
			IModuleControl control = ((IModuleControlled)moduleState.getModule()).getModuleControl(moduleState);
			for(E energyBuffer : buffers){
				if(control.hasPermission(energyBuffer.getModuleState())){
					newBuffers.add(energyBuffer);
				}
			}
			buffers = newBuffers;
		}
		long totalExtract = 0;
		for(E energyBuffer : buffers){
			IModuleState state = energyBuffer.getModuleState();
			long extract = energyBuffer.extractEnergy(state, facing, maxExtract, simulate);
			if(state != null && extract > 0){
				state.getModule().sendModuleUpdate(state);
			}
			totalExtract+=extract;
			maxExtract-=extract;
			if(maxExtract <= 0){
				break;
			}
		}
		return totalExtract;
	}

	@Override
	public long receiveEnergy(IModuleState moduleState, EnumFacing facing, long maxReceive, boolean simulate) {
		List<E> buffers = this.buffers;
		if(moduleState != null && moduleState.getModule() instanceof IModuleControlled){
			List<E> newBuffers = new ArrayList();
			IModuleControl control = ((IModuleControlled)moduleState.getModule()).getModuleControl(moduleState);
			for(E energyBuffer : buffers){
				if(control.hasPermission(energyBuffer.getModuleState())){
					newBuffers.add(energyBuffer);
				}
			}
			buffers = newBuffers;
		}
		long totalReceived = 0;
		for(E energyBuffer : buffers){
			IModuleState state = energyBuffer.getModuleState();
			long receive = energyBuffer.receiveEnergy(state, facing, maxReceive, simulate);
			if(state != null && receive > 0){
				state.getModule().sendModuleUpdate(state);
			}
			totalReceived+=receive;
			maxReceive-=receive;
			if(maxReceive <= 0){
				break;
			}
			break;
		}
		return totalReceived;
	}

	@Override
	public void setEnergy(long energy) {
		for(IEnergyBuffer energyBuffer : buffers){
			long capacity = energyBuffer.getCapacity();
			if(energy > capacity){
				energyBuffer.setEnergy(capacity);
				energy-=capacity;
			}else{
				energyBuffer.setEnergy(energy);
				break;
			}
			if(energy <= 0){
				break;
			}
		}
	}

	@Override
	public long getEnergyStored() {
		long energyStored = 0;
		for(IEnergyBuffer energyBuffer : buffers){
			energyStored +=energyBuffer.getEnergyStored();
		}
		return energyStored;
	}

	@Override
	public long getCapacity() {
		long capacity = 0;
		for(IEnergyBuffer energyBuffer : buffers){
			capacity +=energyBuffer.getCapacity();
		}
		return capacity;
	}

	@Override
	public int getTier() {
		int tier = 1;
		for(IEnergyBuffer energyBuffer : buffers){
			if(energyBuffer.getTier() > tier){
				tier = energyBuffer.getTier();
			}
		}
		return tier;
	}
}