package de.nedelosk.modularmachines.common.modules.storages;

import java.util.List;

import de.nedelosk.modularmachines.api.energy.IEnergyBuffer;
import de.nedelosk.modularmachines.api.modular.IModular;
import de.nedelosk.modularmachines.api.modular.IModularAssembler;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandler;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandlerTileEntity;
import de.nedelosk.modularmachines.api.modules.IModuleProperties;
import de.nedelosk.modularmachines.api.modules.handlers.IModuleContentHandler;
import de.nedelosk.modularmachines.api.modules.handlers.IModulePage;
import de.nedelosk.modularmachines.api.modules.handlers.energy.ModuleEnergyBuffer;
import de.nedelosk.modularmachines.api.modules.items.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.position.IStoragePosition;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.api.modules.storage.BasicStoragePage;
import de.nedelosk.modularmachines.api.modules.storage.IStorage;
import de.nedelosk.modularmachines.api.modules.storage.IStoragePage;
import de.nedelosk.modularmachines.api.modules.storage.Storage;
import de.nedelosk.modularmachines.api.modules.storage.StorageModule;
import de.nedelosk.modularmachines.api.modules.storage.energy.IModuleBattery;
import de.nedelosk.modularmachines.api.modules.storage.energy.IModuleBatteryProperties;
import de.nedelosk.modularmachines.common.modules.pages.BatteryPage;
import de.nedelosk.modularmachines.common.network.PacketHandler;
import de.nedelosk.modularmachines.common.network.packets.PacketSyncModule;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.WorldServer;

public abstract class ModuleBattery extends StorageModule implements IModuleBattery {

	private static final String[] tiers = new  String[]{"LV", "MV", "HV", "EV"};

	public ModuleBattery(String name) {
		super(name);
	}

	@Override
	public void sendModuleUpdate(IModuleState state){
		IModularHandler handler = state.getModular().getHandler();
		if(handler instanceof IModularHandlerTileEntity){
			PacketHandler.sendToNetwork(new PacketSyncModule(handler, state), ((IModularHandlerTileEntity)handler).getPos(), (WorldServer) handler.getWorld());
		}
	}

	@Override
	public void addTooltip(List<String> tooltip, ItemStack stack, IModuleContainer container) {
		super.addTooltip(tooltip, stack, container);
		tooltip.add(I18n.translateToLocal("mm.module.tooltip.tier") + getTier(container));
	}

	@Override
	public int getCapacity(IModuleState state) {
		IModuleProperties properties = state.getContainer().getProperties();
		if(properties instanceof IModuleBatteryProperties){
			return ((IModuleBatteryProperties) properties).getCapacity(state);
		}
		return 0;
	}

	@Override
	public int getMaxReceive(IModuleState state) {
		IModuleProperties properties = state.getContainer().getProperties();
		if(properties instanceof IModuleBatteryProperties){
			return ((IModuleBatteryProperties) properties).getMaxReceive(state);
		}
		return 0;
	}

	@Override
	public int getMaxExtract(IModuleState state) {
		IModuleProperties properties = state.getContainer().getProperties();
		if(properties instanceof IModuleBatteryProperties){
			return ((IModuleBatteryProperties) properties).getMaxExtract(state);
		}
		return 0;
	}

	@Override
	public int getTier(IModuleContainer container) {
		IModuleProperties properties = container.getProperties();
		if(properties instanceof IModuleBatteryProperties){
			return ((IModuleBatteryProperties) properties).getTier(container);
		}
		return 0;
	}

	@Override
	public List<IModuleContentHandler> createHandlers(IModuleState state) {
		List<IModuleContentHandler> handlers = super.createHandlers(state);
		handlers.add(new ModuleEnergyBuffer(state, getCapacity(state), getMaxReceive(state), getMaxExtract(state), getTier(state.getContainer())));
		return handlers;
	}

	@Override
	public IModuleState loadStateFromItem(IModuleState state, ItemStack stack) {
		state = super.loadStateFromItem(state, stack);
		IEnergyBuffer energyBuffer = state.getContentHandler(IEnergyBuffer.class);
		if(energyBuffer != null){
			energyBuffer.setEnergy(loadEnergy(state, stack));
		}
		return state;
	}

	@Override
	public ItemStack saveDataToItem(IModuleState state) {
		ItemStack stack = super.saveDataToItem(state);
		IEnergyBuffer energyBuffer = state.<IEnergyBuffer>getContentHandler(IEnergyBuffer.class);
		if(energyBuffer != null){
			saveEnergy(state, energyBuffer.getEnergyStored(), stack);
		}
		return stack;
	}

	@Override
	public List<IModulePage> createPages(IModuleState state) {
		List<IModulePage> pages = super.createPages(state);
		pages.add(new BatteryPage(state));
		return pages;
	}

	@Override
	public IStorage createStorage(IModuleState state, IModular modular, IStoragePosition position) {
		return new Storage(modular, position, state);
	}

	@Override
	public IStoragePage createPage(IModularAssembler assembler, IModular modular, IStorage storage, IModuleState state, IStoragePosition position) {
		if(storage != null){
			return new BasicStoragePage(assembler, storage);
		}
		return new BasicStoragePage(assembler, position);
	}
}