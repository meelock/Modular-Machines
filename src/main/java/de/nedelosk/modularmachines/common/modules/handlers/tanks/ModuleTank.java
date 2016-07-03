package de.nedelosk.modularmachines.common.modules.handlers.tanks;

import de.nedelosk.modularmachines.api.modules.IModule;
import de.nedelosk.modularmachines.api.modules.handlers.IContentFilter;
import de.nedelosk.modularmachines.api.modules.handlers.tank.EnumTankMode;
import de.nedelosk.modularmachines.api.modules.handlers.tank.FluidTankAdvanced;
import de.nedelosk.modularmachines.api.modules.handlers.tank.IModuleTank;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.api.recipes.RecipeItem;
import de.nedelosk.modularmachines.common.modules.handlers.FilterWrapper;
import de.nedelosk.modularmachines.common.network.PacketHandler;
import de.nedelosk.modularmachines.common.network.packets.PacketModule;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class ModuleTank<M extends IModule> implements IModuleTank<M> {

	protected final FluidTankAdvanced[] tanks;
	protected final IModuleState<M> state;
	private final FilterWrapper insertFilter;
	private final FilterWrapper extractFilter;

	public ModuleTank(FluidTankAdvanced[] tanks, IModuleState<M> state, FilterWrapper insertFilter, FilterWrapper extractFilter) {
		this.tanks = tanks;
		this.state = state;
		this.insertFilter = insertFilter;
		this.extractFilter = extractFilter;

		for(FluidTankAdvanced tank : tanks){
			tank.moduleTank = this;
		}
	}

	@Override
	public FluidTankAdvanced getTank(int index) {
		return tanks[index];
	}

	@Override
	public FluidTankAdvanced[] getTanks() {
		return tanks;
	}

	@Override
	public RecipeItem[] getInputItems() {
		RecipeItem[] inputs = new RecipeItem[getInputs()];
		for(int index = 0; index < getInputs(); index++) {
			FluidStack input = getTank(index).getFluid();
			if (input != null) {
				input = input.copy();
			}
			inputs[index] = new RecipeItem(index, input);
		}
		return inputs;
	}

	@Override
	public boolean canRemoveRecipeInputs(int chance, RecipeItem[] inputs) {
		if (inputs != null) {
			for(RecipeItem recipeInput : inputs) {
				if (recipeInput != null) {
					if (recipeInput.isFluid()) {
						FluidStack test = drainInternal(recipeInput.fluid, false);
						if (test == null || test.amount != recipeInput.fluid.amount) {
							return false;
						}
					}
					continue;
				} else {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean canAddRecipeOutputs(int chance, RecipeItem[] outputs) {
		if (outputs != null) {
			for(RecipeItem output : outputs) {
				if (output != null) {
					if (output.isFluid()) {
						if(output.chance == -1 || chance <= output.chance){
							int test = fillInternal(output.fluid, false);
							if (test != output.fluid.amount) {
								return false;
							}
						}
					}
					continue;
				} else {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void removeRecipeInputs(int chance, RecipeItem[] inputs) {
		if (inputs != null) {
			for(RecipeItem recipeInput : inputs) {
				if (recipeInput != null) {
					if (recipeInput.isFluid()) {
						drainInternal(recipeInput.fluid, true);
					}
				}
			}
		}
	}

	@Override
	public void addRecipeOutputs(int chance, RecipeItem[] outputs) {
		if (outputs != null) {
			for(RecipeItem item : outputs) {
				if (item != null && item.isFluid()) {
					if(item.chance == -1 || chance <= item.chance){
						fillInternal(item.fluid.copy(), true);
					}
				}
			}
		}
	}

	@Override
	public int fillInternal(FluidStack resource, boolean doFill) {
		if (resource == null) {
			return 0;
		}
		for(int i = 0; i < tanks.length; i++) {
			FluidTankAdvanced tank = tanks[i];
			if (tank == null || tank.isFull()) {
				continue;
			}
			if (!tank.isEmpty()) {
				if (resource.getFluid() != tank.getFluid().getFluid()) {
					continue;
				}
			}
			if (insertFilter.isValid(i, resource, state)) {
				return tank.fillInternal(resource, doFill);
			}
		}
		return 0;
	}

	@Override
	public FluidStack drainInternal(FluidStack resource, boolean doDrain) {
		if (resource == null) {
			return null;
		}
		for(int i = 0; i < tanks.length; i++) {
			FluidTankAdvanced tank = tanks[i];
			if (tank == null) {
				continue;
			}
			FluidStack fluidStack = tank.getFluid();
			if (fluidStack == null || resource.getFluid() != fluidStack.getFluid()) {
				continue;
			}
			if (extractFilter.isValid(i, resource, state)) {
				return tank.drainInternal(resource, doDrain);
			}
		}
		return null;
	}

	@Override
	public FluidStack drainInternal(int maxDrain, boolean doDrain) {
		if (maxDrain < 0) {
			return null;
		}
		for(int i = 0; i < tanks.length; i++) {
			FluidTankAdvanced tank = tanks[i];
			if (tank == null || tank.isEmpty()) {
				continue;
			}
			if (tank.getFluid().amount < 0) {
				continue;
			}
			FluidStack resource = new FluidStack(tank.getFluid().getFluid(), maxDrain);
			if (extractFilter.isValid(i, resource, state)) {
				return tank.drainInternal(maxDrain, doDrain);
			}
		}
		return null;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (resource == null) {
			return 0;
		}
		for(int i = 0; i < tanks.length; i++) {
			FluidTankAdvanced tank = tanks[i];
			if (tank == null || tank.isFull()) {
				continue;
			}
			if (!tank.isEmpty()) {
				if (resource.getFluid() != tank.getFluid().getFluid()) {
					continue;
				}
			}
			if (insertFilter.isValid(i, resource, state)) {
				return tank.fill(resource, doFill);
			}
		}
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (resource == null) {
			return null;
		}
		for(int i = 0; i < tanks.length; i++) {
			FluidTankAdvanced tank = tanks[i];
			if (tank == null) {
				continue;
			}
			FluidStack fluidStack = tank.getFluid();
			if (fluidStack == null || resource.getFluid() != fluidStack.getFluid()) {
				continue;
			}
			if (extractFilter.isValid(i, resource, state)) {
				return tank.drain(resource, doDrain);
			}
		}
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (maxDrain < 0) {
			return null;
		}
		for(int i = 0; i < tanks.length; i++) {
			FluidTankAdvanced tank = tanks[i];
			if (tank == null || tank.isEmpty()) {
				continue;
			}
			if (tank.getFluid().amount < 0) {
				continue;
			}
			FluidStack resource = new FluidStack(tank.getFluid().getFluid(), maxDrain);
			if (extractFilter.isValid(i, resource, state)) {
				return tank.drain(maxDrain, doDrain);
			}
		}
		return null;
	}

	@Override
	public IFluidTankProperties[] getTankProperties(){
		IFluidTankProperties[] properties = new FluidTankProperties[tanks.length];
		for(int i = 0;i < tanks.length;i++){
			FluidTankAdvanced tank = tanks[i];
			properties[i] = new FluidTankPropertiesWrapper(tank);
		}
		return properties;
	}

	@Override
	public IContentFilter<FluidStack, M> getInsertFilter() {
		return insertFilter;
	}

	@Override
	public IContentFilter<FluidStack, M> getExtractFilter() {
		return extractFilter;
	}

	@Override
	public int getInputs() {
		int inputs = 0;
		for(FluidTankAdvanced tank : tanks) {
			if (tank.getMode() == EnumTankMode.INPUT) {
				inputs++;
			}
		}
		return inputs;
	}

	@Override
	public int getOutputs() {
		int outputs = 0;
		for(FluidTankAdvanced tank : tanks) {
			if (tank.getMode() == EnumTankMode.OUTPUT) {
				outputs++;
			}
		}
		return outputs;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList nbtTagList = nbt.getTagList("Tanks", 10);
		for(int index = 0;index < nbtTagList.tagCount();index++){
			NBTTagCompound tankTag = nbtTagList.getCompoundTagAt(index);
			int capacity = tankTag.getInteger("Capacity");
			tanks[index] = new FluidTankAdvanced(capacity, this, index, tankTag);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagList nbtTagList = new NBTTagList();
		for(FluidTankAdvanced tank : tanks){
			NBTTagCompound tankTag = new NBTTagCompound();
			tank.writeToNBT(tankTag);
			tankTag.setInteger("Capacity", tank.getCapacity());;
			nbtTagList.appendTag(tankTag);
		}
		nbt.setTag("Tanks", nbtTagList);
		return nbt;
	}

	@Override
	public String getHandlerUID() {
		return "Tanks";
	}

	@Override
	public IModuleState<M> getModuleState() {
		return state;
	}

	@Override
	public void onChange() {
		PacketHandler.INSTANCE.sendToAll(new PacketModule(state.getModular().getHandler(), state));
	}

	@Override
	public Class<FluidStack> getContentClass() {
		return FluidStack.class;
	}
}