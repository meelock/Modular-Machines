package de.nedelosk.modularmachines.common.inventory.slots;

import de.nedelosk.modularmachines.common.blocks.tile.TileModularAssembler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotAssemblerOutput extends SlotOutput {

	public SlotAssemblerOutput(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}
}
