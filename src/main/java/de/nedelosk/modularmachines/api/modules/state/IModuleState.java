package de.nedelosk.modularmachines.api.modules.state;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.nedelosk.modularmachines.api.modular.IModular;
import de.nedelosk.modularmachines.api.modules.IModule;
import de.nedelosk.modularmachines.api.modules.IModuleProperties;
import de.nedelosk.modularmachines.api.modules.handlers.IModuleContentProvider;
import de.nedelosk.modularmachines.api.modules.handlers.IModulePage;
import de.nedelosk.modularmachines.api.modules.items.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.storage.module.IModuleHandler;
import de.nedelosk.modularmachines.api.property.IProperty;
import de.nedelosk.modularmachines.api.property.IPropertyProvider;
import de.nedelosk.modularmachines.api.property.IPropertyRegistryBuilder;
import de.nedelosk.modularmachines.api.property.IPropertySetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface IModuleState<M extends IModule> extends IPropertyProvider, ICapabilityProvider, IPropertySetter<IModuleState<M>>, IPropertyRegistryBuilder, IModuleContentProvider {

	@Override
	@Nonnull
	<T, V extends T> IModuleState<M> set(IProperty<T, ? extends NBTBase, ? extends IPropertyProvider> property, V value);

	@Override
	@Nonnull
	IModuleState<M> register(IProperty property);

	/**
	 * Finish the registration of the properties.
	 */
	@Override
	@Nonnull
	IModuleState<M> build();

	int getIndex();

	void setIndex(int index);

	void addPage(@Nonnull IModulePage page);

	@Nonnull
	List<IModulePage> getPages();

	@Nullable
	<P extends IModulePage> P getPage(Class<? extends P> pageClass);

	@Nullable
	IModulePage getPage(String pageID);

	@Nonnull
	M getModule();

	@Nullable
	ItemStack getStack();

	void setStack(@Nullable ItemStack stack);

	@Nonnull
	IModuleProperties getModuleProperties();

	@Nullable
	IModuleHandler getModuleHandler();

	@Nullable
	IModular getModular();

	@Nonnull
	IModuleContainer getContainer();

	@Override
	@Nonnull
	NBTTagCompound serializeNBT();

	@Override
	void deserializeNBT(@Nonnull NBTTagCompound nbt);

}
