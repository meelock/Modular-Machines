package de.nedelosk.modularmachines.common.modular;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.nedelosk.modularmachines.api.ModularMachinesApi;
import de.nedelosk.modularmachines.api.modular.AssemblerException;
import de.nedelosk.modularmachines.api.modular.AssemblerItemHandler;
import de.nedelosk.modularmachines.api.modular.IModular;
import de.nedelosk.modularmachines.api.modular.IModularAssembler;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandler;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandlerTileEntity;
import de.nedelosk.modularmachines.api.modules.IModule;
import de.nedelosk.modularmachines.api.modules.IModuleCasing;
import de.nedelosk.modularmachines.api.modules.controller.IModuleController;
import de.nedelosk.modularmachines.api.modules.items.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.position.IStoragePosition;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.api.modules.storage.IItemHandlerStorage;
import de.nedelosk.modularmachines.api.modules.storage.IStorage;
import de.nedelosk.modularmachines.api.modules.storage.IStorageModule;
import de.nedelosk.modularmachines.api.modules.storage.IStoragePage;
import de.nedelosk.modularmachines.api.modules.storage.module.IModuleModuleStorage;
import de.nedelosk.modularmachines.api.modules.storage.module.IModuleStorage;
import de.nedelosk.modularmachines.client.gui.GuiAssembler;
import de.nedelosk.modularmachines.common.config.Config;
import de.nedelosk.modularmachines.common.core.ModularMachines;
import de.nedelosk.modularmachines.common.inventory.ContainerAssembler;
import de.nedelosk.modularmachines.common.utils.Translator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ModularAssembler implements IModularAssembler {

	protected final IItemHandlerStorage itemHandler;
	protected final Map<IStoragePosition, IStoragePage> pages;
	protected final List<IStoragePosition> indexes;
	protected final IModularHandler modularHandler;
	protected IStoragePosition selectedPosition;

	public ModularAssembler(IModularHandler modularHandler, ItemStack[] stacks, Map<IStoragePosition, IStoragePage> pages) {
		this(modularHandler, new AssemblerItemHandler(stacks, null, null), pages);
		this.itemHandler.setAssembler(this);
	}

	public ModularAssembler(IModularHandler modularHandler, Map<IStoragePosition, IStoragePage> pages) {
		this(modularHandler, new AssemblerItemHandler(pages.size(), null), pages);
		this.itemHandler.setAssembler(this);
	}

	public ModularAssembler(IModularHandler modularHandler) {
		this(modularHandler, createEmptyPages(modularHandler));
	}

	public ModularAssembler(IModularHandler modularHandler, NBTTagCompound nbtTag) {
		this(modularHandler, createEmptyPages(modularHandler));
		deserializeNBT(nbtTag);
	}

	public ModularAssembler(IModularHandler modularHandler, IItemHandlerStorage itemHandler, Map<IStoragePosition, IStoragePage> pages) {
		this.modularHandler = modularHandler;
		this.pages = pages;
		this.itemHandler = itemHandler;
		this.indexes = new ArrayList<>(pages.keySet());
		Collections.sort(indexes, StoragePositionComperator.INSTANCE);
		this.selectedPosition = indexes.get(0);
		updatePages(null);
	}

	private static Map<IStoragePosition, IStoragePage> createEmptyPages(IModularHandler modularHandler){
		Map<IStoragePosition, IStoragePage> pages = new HashMap<>();
		for(IStoragePosition position : (List<IStoragePosition>)modularHandler.getStoragePositions()){
			pages.put(position, null);
		}
		return pages;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		if(itemHandler != null){
			tagCompound.setTag("itemHandler", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().writeNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, itemHandler, null));
		}
		NBTTagList list = new NBTTagList();
		for(Entry<IStoragePosition, IStoragePage> entry : pages.entrySet()){
			if(entry.getValue() != null){
				NBTTagCompound nbtTag = new NBTTagCompound();
				nbtTag.setInteger("Index", getIndex(entry.getKey()));
				nbtTag.setTag("Page", entry.getValue().serializeNBT());
				list.appendTag(nbtTag);
			}
		}
		tagCompound.setTag("Pages", list);
		return tagCompound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if(nbt.hasKey("itemHandler") && itemHandler != null){
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().readNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, itemHandler, null, nbt.getTag("itemHandler"));
		}
		updatePages(null);
		NBTTagList list = nbt.getTagList("Pages", 10);
		for(int i = 0;i< list.tagCount();i++){
			NBTTagCompound nbtTag = list.getCompoundTagAt(i);
			IStoragePosition position = indexes.get(nbtTag.getInteger("Index"));
			pages.get(position).deserializeNBT(nbtTag.getCompoundTag("Page"));
		}
	}

	@Override
	public IModularHandler getHandler() {
		return modularHandler;
	}


	@Override
	public void setSelectedPosition(IStoragePosition position) {
		this.selectedPosition = position;
	}

	@Override
	public IStoragePosition getSelectedPosition() {
		return selectedPosition;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public GuiContainer createGui(InventoryPlayer inventory) {
		return new GuiAssembler(modularHandler, inventory);
	}

	@Override
	public Container createContainer(InventoryPlayer inventory) {
		return new ContainerAssembler(modularHandler, inventory);
	}

	protected void testComplexity() throws AssemblerException{
		int complexity = getComplexity(true, null);
		int allowedComplexity = getAllowedComplexity(null);
		if(complexity > allowedComplexity){
			if(allowedComplexity == Config.defaultAllowedComplexity){
				throw new AssemblerException(Translator.translateToLocalFormatted("modular.assembler.error.no.controller"));
			}
			throw new AssemblerException(Translator.translateToLocalFormatted("modular.assembler.error.complexity"));
		}
	}

	@Override
	public int getComplexity(boolean withStorage, IStoragePosition position) {
		int complexity = 0;
		if(position == null){
			for(IStoragePosition otherPosition : indexes){
				ItemStack storageStack = itemHandler.getStackInSlot(getIndex(otherPosition));
				if(storageStack != null){
					IModuleContainer container = ModularMachinesApi.getContainerFromItem(storageStack);
					if(container != null){
						if(withStorage || !(container.getModule() instanceof IStorageModule)){
							complexity+=container.getModule().getComplexity(container);
						}
					}
				}
				IStoragePage page = pages.get(otherPosition);
				if(page != null){
					IItemHandler assemblerHandler = page.getItemHandler();
					for(int index = 0;index < assemblerHandler.getSlots();index++){
						ItemStack stack = assemblerHandler.getStackInSlot(index);
						if(stack != null){
							IModuleContainer container = ModularMachinesApi.getContainerFromItem(stack);
							if(container != null){
								if(container.getModule() instanceof IModuleModuleStorage &&!withStorage){
									continue;
								}
								complexity+=container.getModule().getComplexity(container);
							}
						}
					}
				}
			}
		}else{
			ItemStack storageStack = itemHandler.getStackInSlot(getIndex(position));
			if(storageStack != null){
				IModuleContainer container = ModularMachinesApi.getContainerFromItem(storageStack);
				if(container != null){
					if(withStorage || !(container.getModule() instanceof IStorageModule)){
						complexity+=container.getModule().getComplexity(container);
					}
				}
			}
			if(pages.get(position) != null){
				IItemHandler assemblerHandler = pages.get(position).getItemHandler();
				for(int index = 0;index < assemblerHandler.getSlots();index++){
					ItemStack slotStack = assemblerHandler.getStackInSlot(index);
					if(slotStack != null){
						IModuleContainer container = ModularMachinesApi.getContainerFromItem(slotStack);
						if(container != null){
							if(container.getModule() instanceof IModuleModuleStorage &&!withStorage){
								continue;
							}
							complexity+=container.getModule().getComplexity(container);
						}
					}
				}
			}
		}
		return complexity;
	}

	@Override
	public int getAllowedComplexity(IStoragePosition position) {
		if(position == null){
			for(IStoragePage page : pages.values()){
				if(page != null){
					IItemHandler assemblerHandler = page.getItemHandler();
					for(int index = 0;index < assemblerHandler.getSlots();index++){
						ItemStack stack = assemblerHandler.getStackInSlot(index);
						if(stack != null){
							IModuleContainer container = ModularMachinesApi.getContainerFromItem(stack);
							if(container != null){
								IModule module = container.getModule();
								if(module instanceof IModuleController) {
									return ((IModuleController)module).getAllowedComplexity(container);
								}
							}
						}
					}
				}
			}
			return Config.defaultAllowedComplexity;
		}else{
			if(pages.get(position) != null){
				IItemHandler assemblerHandler = pages.get(position).getItemHandler();
				for(int index = 0;index < assemblerHandler.getSlots();index++){
					ItemStack slotStack = assemblerHandler.getStackInSlot(index);
					if(slotStack != null){
						IModuleContainer container = ModularMachinesApi.getContainerFromItem(slotStack);
						if(container != null){
							IModule module = container.getModule();
							if(module instanceof IModuleModuleStorage) {
								return ((IModuleModuleStorage) module).getAllowedComplexity(container);
							}
						}
					}
				}
			}
			return Config.defaultAllowedStorageComplexity;
		}
	}

	@Override
	public IItemHandler getItemHandler() {
		return itemHandler;
	}

	@Override
	public Collection<IStoragePage> getStoragePages() {
		return pages.values();
	}

	@Override
	public List<IStoragePosition> getStoragePositions() {
		return indexes;
	}

	@Override
	public IStoragePage getStoragePage(IStoragePosition position) {
		return pages.get(position);
	}

	@Override
	public IModular assemble() throws AssemblerException {
		IModular modular = new Modular();
		for(IStoragePage page : pages.values()){
			if(page != null){
				modular.addStorage(page.assemble(modular));
			}
		}
		for(IStorage storage : modular.getStorages().values()){
			if(storage != null){
				storage.getModule().getModule().assembleModule(this, modular, storage, storage.getModule());
				if(storage instanceof IModuleStorage){
					for(IModuleState state : ((IModuleStorage)storage).getModules()){
						state.getModule().assembleModule(this, modular, storage, state);
					}
				}
			}
		}
		if(modular.getModules(IModuleCasing.class).isEmpty()){
			throw new AssemblerException(Translator.translateToLocal("modular.assembler.error.no.casing"));
		}
		for(IStoragePage page : pages.values()){
			if(page != null){
				page.canAssemble(modular);
			}
		}
		testComplexity();
		modular.onModularAssembled();
		return modular;
	}

	@Override
	public int getIndex(IStoragePosition position) {
		if(indexes.indexOf(position) < 0){
			getClass();
		}
		return indexes.indexOf(position);
	}

	@Override
	public IModularAssembler copy(IModularHandler handler) {
		return new ModularAssembler(handler, pages);
	}

	@Override
	public void updatePages(IStoragePosition position){
		if(position == null){
			for(IStoragePosition pos : indexes){
				IStoragePage page = pages.get(pos);
				ItemStack stack = itemHandler.getStackInSlot(getIndex(pos));
				if(page == null){
					if(stack != null){
						IModuleState state = ModularMachinesApi.loadOrCreateModuleState(null, stack);
						if(state != null){
							pages.put(pos, ((IStorageModule)state.getModule()).createPage(this, null, null, state, pos));
						}
					}
				}else{
					if(stack == null){
						pages.put(pos, null);
					}
					page.setAssembler(this);
				}
			}
		}
		modularHandler.markDirty();
	}

	@Override
	public void onStorageChange(){
		if(!modularHandler.getWorld().isRemote && modularHandler instanceof IModularHandlerTileEntity){
			BlockPos pos = ((IModularHandlerTileEntity)modularHandler).getPos();
			WorldServer server = (WorldServer) modularHandler.getWorld();
			for(EntityPlayer otherPlayer : server.playerEntities) {
				if(otherPlayer.openContainer instanceof ContainerAssembler) {
					ContainerAssembler assembler = (ContainerAssembler) otherPlayer.openContainer;
					if(modularHandler == assembler.getHandler()) {
						ItemStack heldStack = null;

						if(otherPlayer.inventory.getItemStack() != null) {
							heldStack = otherPlayer.inventory.getItemStack();
							otherPlayer.inventory.setItemStack(null);
						}
						otherPlayer.openGui(ModularMachines.instance, 0, otherPlayer.worldObj, pos.getX(), pos.getY(), pos.getZ());

						if(heldStack != null) {
							otherPlayer.inventory.setItemStack(heldStack);
							((EntityPlayerMP)otherPlayer).connection.sendPacket(new SPacketSetSlot(-1, -1, heldStack));
						}
					}
				}
			}
		}
	}

	private enum StoragePositionComperator implements Comparator<IStoragePosition> {
		INSTANCE;

		@Override
		public int compare(IStoragePosition arg0, IStoragePosition arg1) {
			return arg0.getProperty(arg1);
		}

	}

}
