package nedelosk.nedeloskcore.common.inventory;

import nedelosk.nedeloskcore.api.INBTTagable;
import nedelosk.nedeloskcore.utils.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryPlanningTool implements IInventory, INBTTagable {

	public ItemStack[] slots = new ItemStack[1];
	public ItemStack parent;

	public InventoryPlanningTool(ItemStack parent)
	{
		
		this.parent = parent;
		if (parent.getTagCompound() == null)
			parent.setTagCompound(new NBTTagCompound());
		readFromNBT(parent.getTagCompound());
	}
    
	@Override
	public int getSizeInventory() {
		return this.slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return this.slots[i];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if(this.slots[slot] != null){
			ItemStack itemstack;
				
		    if(this.slots[slot].stackSize <= amount){
		    	itemstack = this.slots[slot];
		    	this.slots[slot] = null;
		    	return itemstack;
		    }
		    else{
		    	itemstack = this.slots[slot].splitStack(amount);
		    	
		    	if(this.slots[slot].stackSize == 0){
		    		this.slots[slot] = null;
		    	}
		    }
		}
		
		return null;
		
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if(this.slots[i] != null){
			ItemStack itemstack = this.slots[i];
			this.slots[i] = null;
			return itemstack;
		};
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.slots[i] = itemstack;
		
		if(itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()){
			itemstack.stackSize = this.getInventoryStackLimit();
		}
		
	}
	
	@Override
	public String getInventoryName() {
		return "";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {
		
	}
	
	public ItemStack findParentInInventory(EntityPlayer player) {
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (ItemUtils.isIdenticalItem(stack, parent)) {
				return stack;
			}
		}
		return parent;
	}
	
	public void onGuiSaved(EntityPlayer player) {
		parent = findParentInInventory(player);
		if (parent != null) {
			save();
		}
	}
	
	public void save()
	{
		NBTTagCompound nbt = parent.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
		}
		writeToNBT(nbt);
		parent.setTagCompound(nbt);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		
		NBTTagList nbtTagList = new NBTTagList();
		for(int i = 0; i< this.getSizeInventory(); i++){
			if (this.slots[i] != null){
				NBTTagCompound item = new NBTTagCompound();
				item.setByte("item", (byte)i);
				this.slots[i].writeToNBT(item);
				nbtTagList.appendTag(item);
			}
		}
		nbt.setTag("slots", nbtTagList);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		NBTTagList nbtTagList = nbt.getTagList("slots", 10);
		this.slots = new ItemStack[this.getSizeInventory()];
		
		for(int i = 0; i < nbtTagList.tagCount(); i++){
			NBTTagCompound item = nbtTagList.getCompoundTagAt(i);
			byte itemLocation = item.getByte("item");
			if (itemLocation >= 0 && itemLocation < this.getSizeInventory()){
				this.slots[itemLocation] = ItemStack.loadItemStackFromNBT(item);
			}
		}
	}

	@Override
	public void markDirty() {
		
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return true;
	}

}