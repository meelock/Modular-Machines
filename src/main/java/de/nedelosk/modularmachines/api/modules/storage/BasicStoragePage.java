package de.nedelosk.modularmachines.api.modules.storage;

import java.util.List;

import de.nedelosk.modularmachines.api.gui.IContainerBase;
import de.nedelosk.modularmachines.api.modular.AssemblerException;
import de.nedelosk.modularmachines.api.modular.IModular;
import de.nedelosk.modularmachines.api.modular.IModularAssembler;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandler;
import de.nedelosk.modularmachines.api.modules.position.IStoragePosition;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BasicStoragePage extends StoragePage {

	public BasicStoragePage(IModularAssembler assembler, IStorage storage) {
		super(assembler, storage);
	}

	public BasicStoragePage(IModularAssembler assembler, IItemHandlerStorage itemHandler, IStoragePosition position) {
		super(assembler, itemHandler, position);
	}

	public BasicStoragePage(IModularAssembler assembler, IStoragePosition position) {
		super(assembler, position);
	}

	@Override
	public boolean isItemValid(ItemStack stack, SlotAssembler slot, SlotAssemblerStorage storageSlot) {
		return false;
	}

	@Override
	public void createSlots(IContainerBase<IModularHandler> container, List<Slot> slots) {
		if(position != null){
			SlotAssemblerStorage storageSlot;
			slots.add(storageSlot = new SlotAssemblerStorage(assembler.getItemHandler(), assembler.getIndex(position), 44, 35, this, position, container));
		}
	}

	@Override
	public void onSlotChanged(IContainerBase<IModularHandler> container) {
	}

	@Override
	public void canAssemble(IModular modular) throws AssemblerException {
	}
}
