package de.nedelosk.modularmachines.api.modules.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.nedelosk.modularmachines.api.modules.handlers.IModuleContentHandler;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

public class ModuleControl implements IModuleControl, IModuleContentHandler, INBTSerializable<NBTTagCompound> {

	private IModuleState state;
	private Map<Integer, Boolean> permissions = new HashMap<>();
	private EnumRedstoneMode mode;

	public ModuleControl(IModuleState state) {
		this.state = state;
		this.mode = EnumRedstoneMode.IGNORE;
	}

	@Override
	public IModuleState getModuleState() {
		return state;
	}

	@Override
	public String getUID() {
		return "Control";
	}

	@Override
	public void addToolTip(List<String> tooltip, ItemStack stack, IModuleState state) {
	}

	@Override
	public boolean hasPermission(IModuleState state) {
		if(permissions.containsKey(state.getIndex())){
			return permissions.get(state.getIndex());
		}
		return false;
	}

	@Override
	public void setPermission(IModuleState state, boolean permission) {
		permissions.put(state.getIndex(), permission);
	}

	@Override
	public EnumRedstoneMode getRedstoneMode() {
		return mode;
	}

	@Override
	public void setRedstoneMode(EnumRedstoneMode mode) {
		this.mode = mode;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		nbtTag.setShort("Mode", (short)mode.ordinal());
		NBTTagList list = new NBTTagList();
		for(Entry<Integer, Boolean> permission : permissions.entrySet()){
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("Index", permission.getKey());
			tag.setBoolean("Permission", permission.getValue());
			list.appendTag(tag);
		}
		nbtTag.setTag("Permissions", list);
		return nbtTag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		mode = EnumRedstoneMode.values()[nbt.getShort("Mode")];
		NBTTagList list = nbt.getTagList("Permissions", 10);
		for(int i = 0;i < list.tagCount();i++){
			NBTTagCompound tag = list.getCompoundTagAt(i);
			permissions.put(tag.getInteger("Index"), tag.getBoolean("Permission"));
		}
	}
}
