package de.nedelosk.modularmachines.api.modules.state;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.filefilter.MagicNumberFileFilter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.nedelosk.modularmachines.api.modular.IModular;
import de.nedelosk.modularmachines.api.modules.IModule;
import de.nedelosk.modularmachines.api.modules.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.ModuleEvents;
import de.nedelosk.modularmachines.api.modules.handlers.IModuleContentHandler;
import de.nedelosk.modularmachines.api.modules.handlers.IModulePage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

public class ModuleState<M extends IModule> implements IModuleState<M> {

	protected static final PropertyInteger INDEX = new PropertyInteger("index", -1);

	protected Map<IProperty, Object> properties;
	protected final List<IProperty> registeredProperties;
	protected final IModular modular;
	protected final M module;
	protected final IModuleContainer container;
	protected final List<IModuleContentHandler> contentHandlers;
	protected final List<IModulePage> pages;

	public ModuleState(IModular modular, M module, IModuleContainer container) {
		this.registeredProperties = Lists.newArrayList();
		register(INDEX);

		this.modular = modular;
		this.module = module;
		this.container = container;
		List<IModulePage> createdPages = module.createPages(this);
		MinecraftForge.EVENT_BUS.post(new ModuleEvents.ModulePageCreateEvent(this, createdPages));
		this.pages = createdPages;
		this.contentHandlers = module.createContentHandlers(this);
	}

	@Override
	public IModuleState<M> createState() {
		this.properties = Maps.newHashMap();
		for(IProperty property : registeredProperties){
			properties.put(property, property.getDefaultValue());
		}
		return this;
	}

	@Override
	public IModuleState<M> register(IProperty property) {
		if(properties == null){
			if(!registeredProperties.contains(property)){
				registeredProperties.add(property);
			}
		}
		return this;
	}

	@Override
	public <T> T get(IProperty<T, ? extends NBTBase> property) {
		if(!registeredProperties.contains(property)){
			throw new IllegalArgumentException("Cannot get property " + property + " as it is not registred in the module state from the model " + this.module.getUnlocalizedName());
		}
		if(!properties.containsKey(property)){
			return null;
		}
		return (T) properties.get(property);
	}

	@Override
	public <T, V extends T> IModuleState<M> add(IProperty<T, ? extends NBTBase> property, V value) {
		if(!registeredProperties.contains(property)){
			throw new IllegalArgumentException("Cannot set property " + property + " as it is not registred in the module state from the model " + this.module.getUnlocalizedName());
		}
		properties.put(property, value);
		return this;
	}

	@Override
	public List<IModuleContentHandler> getContentHandlers() {
		return contentHandlers;
	}

	@Override
	public <C> IModuleContentHandler<C, IModule> getContentHandler(Class<? extends C> contentClass) {
		for(IModuleContentHandler handler : contentHandlers){
			if(handler.getContentClass() == contentClass){
				return handler;
			}
		}
		return null;
	}

	@Override
	public Map<IProperty, Object> getProperties() {
		return properties;
	}

	@Override
	public int getIndex() {
		return get(INDEX);
	}

	@Override
	public void setIndex(int index) {
		add(INDEX, index);
	}

	@Override
	public List<IModulePage> getPages() {
		return pages;
	}

	@Override
	public M getModule() {
		return module;
	}

	@Override
	public IModular getModular() {
		return modular;
	}

	@Override
	public IModuleContainer getContainer() {
		return container;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, IModular modular) {
		for(Entry<IProperty, Object> object : properties.entrySet()){
			if(object.getValue() != null){
				nbt.setTag(object.getKey().getName(), object.getKey().writeToNBT(this, object.getValue()));
			}
		}
		for(IModuleContentHandler handler : contentHandlers){
			nbt.setTag(handler.getHandlerUID(), handler.writeToNBT(new NBTTagCompound()));
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, IModular modular) {
		for(IProperty property : registeredProperties){
			if(nbt.hasKey(property.getName())){
				properties.put(property, property.readFromNBT(nbt.getTag(property.getName()), this));
			}
		}
		for(IModuleContentHandler handler : contentHandlers){
			if(nbt.hasKey(handler.getHandlerUID())){
				handler.readFromNBT(nbt.getCompoundTag(handler.getHandlerUID()));
			}
		}
	}

}
