package modularmachines.common.modules.turbines;

import java.util.List;

import modularmachines.api.modules.IModulePage;
import modularmachines.api.modules.handlers.inventory.IModuleInventory;
import modularmachines.api.modules.handlers.tank.IModuleTank;
import modularmachines.api.modules.state.IModuleState;
import modularmachines.common.core.managers.FluidManager;
import modularmachines.common.modules.pages.SteamTurbinePage;
import modularmachines.common.utils.ModuleUtil;
import net.minecraftforge.fluids.FluidStack;

public class ModuleTurbineSteam extends ModuleTurbine {

	public ModuleTurbineSteam() {
		super("steam");
	}

	@Override
	public boolean removeMaterial(IModuleState state) {
		IModuleTank tank = state.getPage(SteamTurbinePage.class).getTank();
		if (tank == null) {
			return false;
		}
		FluidStack drained = tank.drainInternal(new FluidStack(FluidManager.STEAM, getMaterialPerWork(state)), false);
		if (drained != null && drained.amount >= getMaterialPerWork(state)) {
			return tank.drainInternal(new FluidStack(FluidManager.STEAM, getMaterialPerWork(state)), true).amount >= getMaterialPerWork(state);
		} else {
			return false;
		}
	}

	@Override
	public void updateServer(IModuleState state, int tickCount) {
		super.updateServer(state, tickCount);
		if (state.getModular().updateOnInterval(20)) {
			IModulePage page = state.getPage(SteamTurbinePage.class);
			IModuleInventory inventory = page.getInventory();
			IModuleTank tank = page.getTank();
			ModuleUtil.tryEmptyContainer(0, 1, inventory, tank.getTank(0));
		}
	}

	@Override
	public boolean canWork(IModuleState state) {
		IModuleTank tank = state.getPage(SteamTurbinePage.class).getTank();
		if (tank == null) {
			return false;
		}
		if (tank.getTank(0).getFluid() == null) {
			return false;
		}
		if (tank.getTank(0).getFluid().amount > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<IModulePage> createPages(IModuleState state) {
		List<IModulePage> pages = super.createPages(state);
		pages.add(new SteamTurbinePage(state));
		return pages;
	}
}