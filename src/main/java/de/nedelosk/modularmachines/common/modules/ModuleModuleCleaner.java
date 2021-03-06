package de.nedelosk.modularmachines.common.modules;

import java.util.List;

import de.nedelosk.modularmachines.api.ModularMachinesApi;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandler;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandlerTileEntity;
import de.nedelosk.modularmachines.api.modules.EnumModuleSizes;
import de.nedelosk.modularmachines.api.modules.IModule;
import de.nedelosk.modularmachines.api.modules.IModuleModuleCleaner;
import de.nedelosk.modularmachines.api.modules.Module;
import de.nedelosk.modularmachines.api.modules.handlers.ICleanableModuleContentHandler;
import de.nedelosk.modularmachines.api.modules.handlers.IModuleContentHandler;
import de.nedelosk.modularmachines.api.modules.handlers.IModulePage;
import de.nedelosk.modularmachines.api.modules.handlers.inventory.IModuleInventory;
import de.nedelosk.modularmachines.api.modules.items.IModuleColoredItem;
import de.nedelosk.modularmachines.api.modules.items.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.items.IModuleProvider;
import de.nedelosk.modularmachines.api.modules.position.EnumModulePositions;
import de.nedelosk.modularmachines.api.modules.position.IModulePositioned;
import de.nedelosk.modularmachines.api.modules.position.IModulePostion;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.common.modules.pages.CleanerPage;
import de.nedelosk.modularmachines.common.network.PacketHandler;
import de.nedelosk.modularmachines.common.network.packets.PacketSyncModule;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleModuleCleaner extends Module implements IModuleModuleCleaner, IModulePositioned, IModuleColoredItem{

	public ModuleModuleCleaner(String name) {
		super(name);
	}

	@Override
	public IModulePostion[] getValidPositions(IModuleContainer container) {
		return new IModulePostion[]{EnumModulePositions.CASING};
	}

	@Override
	public EnumModuleSizes getSize(IModuleContainer container) {
		return EnumModuleSizes.SMALL;
	}

	@Override
	public int getComplexity(IModuleContainer container) {
		return 1;
	}

	@Override
	public List<IModulePage> createPages(IModuleState state) {
		List<IModulePage> pages = super.createPages(state);
		pages.add(new CleanerPage(state));
		return pages;
	}

	@Override
	public void updateServer(IModuleState<IModule> state, int tickCount) {
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateClient(IModuleState<IModule> state, int tickCount) {
	}

	@Override
	public void cleanModule(IModuleState state) {
		IModuleInventory inventory = state.getPage(CleanerPage.class).getInventory();
		ItemStack stack = inventory.getStackInSlot(0);
		if(stack != null){
			IModuleProvider provider = stack.getCapability(ModularMachinesApi.MODULE_PROVIDER_CAPABILITY, null);
			if(provider != null){
				IModuleState moduleState = provider.createState(null);
				if(moduleState != null){
					for(IModuleContentHandler handler : moduleState.getContentHandlers()){
						if(handler instanceof ICleanableModuleContentHandler){
							((ICleanableModuleContentHandler)handler).cleanHandler(state);
						}
					}
				}
				provider.setState(null);
			}
		}
	}

	@Override
	public void sendModuleUpdate(IModuleState state){
		IModularHandler handler = state.getModular().getHandler();
		if(handler instanceof IModularHandlerTileEntity){
			PacketHandler.sendToNetwork(new PacketSyncModule(handler, state), ((IModularHandlerTileEntity)handler).getPos(), (WorldServer) handler.getWorld());
		}
	}

	@Override
	public int getColor(IModuleContainer container) {
		return 0x2E5D0E;
	}
}
