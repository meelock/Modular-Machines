package de.nedelosk.modularmachines.api.modular;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.nedelosk.modularmachines.api.gui.IGuiHandler;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandler;
import de.nedelosk.modularmachines.api.modules.position.IStoragePosition;
import de.nedelosk.modularmachines.api.modules.storage.IStoragePage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

public interface IModularAssembler extends INBTSerializable<NBTTagCompound>, IGuiHandler {

	@Nonnull
	IModularHandler getHandler();

	@Nullable
	IModular assemble() throws AssemblerException;

	int getComplexity(boolean withStorage, @Nullable IStoragePosition position);

	int getAllowedComplexity(@Nullable IStoragePosition position);

	@Nonnull
	IModularAssembler copy(@Nonnull IModularHandler handler);

	void setSelectedPosition(IStoragePosition position);

	IItemHandler getItemHandler();

	@Nullable
	IStoragePage getStoragePage(IStoragePosition position);

	int getIndex(@Nonnull IStoragePosition position);

	@Nonnull
	Collection<IStoragePage> getStoragePages();

	@Nonnull
	IStoragePosition getSelectedPosition();

	@Nonnull
	List<IStoragePosition> getStoragePositions();

	void updatePages(IStoragePosition position);

	void onStorageChange();

}
