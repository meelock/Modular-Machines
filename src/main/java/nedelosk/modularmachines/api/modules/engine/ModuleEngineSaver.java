package nedelosk.modularmachines.api.modules.engine;

import nedelosk.modularmachines.api.modular.IModular;
import nedelosk.modularmachines.api.recipes.IRecipeManager;
import nedelosk.modularmachines.api.utils.ModuleStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModuleEngineSaver implements IModuleEngineSaver {

	protected int burnTime, burnTimeTotal;
	protected int timer;
	protected final int timerTotal;
	protected boolean isWorking;
	public IRecipeManager manager;
	protected float progress;

	public ModuleEngineSaver() {
		this.timerTotal = 50;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, IModular modular, ModuleStack stack) {
		progress = nbt.getFloat("Progress");
		burnTime = nbt.getInteger("burnTime");
		burnTimeTotal = nbt.getInteger("burnTimeTotal");
		timer = nbt.getInteger("timer");
		isWorking = nbt.getBoolean("isWorking");
		if (nbt.hasKey("Manager")) {
			manager = ((IModuleEngine) stack.getModule()).creatRecipeManager();
			manager = manager.readFromNBT(nbt.getCompoundTag("Manager"), modular);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, IModular modular, ModuleStack stack) {
		nbt.setFloat("Progress", progress);
		nbt.setInteger("burnTime", burnTime);
		nbt.setInteger("burnTimeTotal", burnTimeTotal);
		nbt.setInteger("timer", timer);
		nbt.setBoolean("isWorking", isWorking);
		if (manager != null) {
			NBTTagCompound nbtTag = new NBTTagCompound();
			manager.writeToNBT(nbtTag, modular);
			nbt.setTag("Manager", nbtTag);
		}
	}

	@Override
	public float getProgress() {
		return progress;
	}

	@Override
	public void setManager(IRecipeManager manager) {
		this.manager = manager;
	}

	@Override
	public IRecipeManager getManager(ModuleStack stack) {
		return manager;
	}

	@Override
	public void setProgress(float progress) {
		this.progress = progress;
	}

	@Override
	public void addProgress(float progress) {
		this.progress += progress;
	}

	@Override
	public int getBurnTime(ModuleStack stack) {
		return burnTime;
	}

	@Override
	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}

	@Override
	public int getBurnTimeTotal(ModuleStack stack) {
		return burnTimeTotal;
	}

	@Override
	public void setBurnTimeTotal(int burnTimeTotal) {
		this.burnTimeTotal = burnTimeTotal;
	}

	@Override
	public void addBurnTime(int burntime) {
		this.burnTime += burntime;
	}

	@Override
	public void addTimer(int timer) {
		this.timer += timer;
	}

	@Override
	public boolean isWorking() {
		return isWorking;
	}

	@Override
	public void setIsWorking(boolean isWorking) {
		this.isWorking = isWorking;
	}

	@Override
	public int getTimer() {
		return timer;
	}

	@Override
	public void setTimer(int timer) {
		this.timer = timer;
	}

	@Override
	public int getTimerTotal() {
		return timerTotal;
	}
}