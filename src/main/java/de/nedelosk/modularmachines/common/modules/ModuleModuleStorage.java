package de.nedelosk.modularmachines.common.modules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.nedelosk.modularmachines.api.modular.IModular;
import de.nedelosk.modularmachines.api.modular.IModularAssembler;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandler;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandlerTileEntity;
import de.nedelosk.modularmachines.api.modules.EnumModuleSizes;
import de.nedelosk.modularmachines.api.modules.IModuleProperties;
import de.nedelosk.modularmachines.api.modules.containers.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.containers.IModuleItemContainer;
import de.nedelosk.modularmachines.api.modules.containers.IModuleProvider;
import de.nedelosk.modularmachines.api.modules.models.IModelHandler;
import de.nedelosk.modularmachines.api.modules.models.ModuleModelLoader;
import de.nedelosk.modularmachines.api.modules.position.IStoragePosition;
import de.nedelosk.modularmachines.api.modules.properties.IModuleModuleStorageProperties;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.api.modules.storage.IStorage;
import de.nedelosk.modularmachines.api.modules.storage.IStoragePage;
import de.nedelosk.modularmachines.api.modules.storage.StorageModule;
import de.nedelosk.modularmachines.api.modules.storage.module.IDefaultModuleStorage;
import de.nedelosk.modularmachines.api.modules.storage.module.IModuleModuleStorage;
import de.nedelosk.modularmachines.api.modules.storage.module.ModuleStorage;
import de.nedelosk.modularmachines.api.modules.storage.module.ModuleStoragePage;
import de.nedelosk.modularmachines.client.model.ModelHandlerModuleStorage;
import de.nedelosk.modularmachines.common.config.Config;
import de.nedelosk.modularmachines.common.network.PacketHandler;
import de.nedelosk.modularmachines.common.network.packets.PacketSyncModule;
import de.nedelosk.modularmachines.common.utils.Translator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleModuleStorage extends StorageModule implements IModuleModuleStorage<ModuleStorage> {

	public ModuleModuleStorage() {
		super("moduleStorage");
	}

	@Override
	public void addTooltip(List<String> tooltip, ItemStack stack, IModuleContainer container) {
		tooltip.add(Translator.translateToLocal("mm.module.allowed.complexity") + getAllowedComplexity(container));
		super.addTooltip(tooltip, stack, container);
	}

	@Override
	public void sendModuleUpdate(IModuleState state) {
		IModularHandler handler = state.getModular().getHandler();
		if (handler instanceof IModularHandlerTileEntity) {
			PacketHandler.sendToNetwork(new PacketSyncModule(state), ((IModularHandlerTileEntity) handler).getPos(), (WorldServer) handler.getWorld());
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Map<ResourceLocation, ResourceLocation> getModelLocations(IModuleItemContainer container) {
		Map<ResourceLocation, ResourceLocation> locations = new HashMap();
		locations.put(ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "moduleStorage"),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), "default", "module_storage", "moduleStorage"));
		locations.put(ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "top"),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), "default", "module_storage", "top"));
		locations.put(ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "back"),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), "default", "module_storage", "back"));
		locations.put(ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "wall"),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), "default", "module_storage", "wall"));
		locations.put(ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/stick_down"),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), "default", "module_storage", "front_walls/stick_down"));
		locations.put(ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/stick_up"),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), "default", "module_storage", "front_walls/stick_up"));
		locations.put(ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/small_down"),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), "default", "module_storage", "front_walls/small_down"));
		locations.put(ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/small_medium"),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), "default", "module_storage", "front_walls/small_medium"));
		locations.put(ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/small_up"),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), "default", "module_storage", "front_walls/small_up"));
		locations.put(ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/medium_medium"),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), "default", "module_storage", "front_walls/medium_medium"));
		locations.put(ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/medium_up"),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), "default", "module_storage", "front_walls/medium_up"));
		locations.put(ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/large"),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), "default", "module_storage", "front_walls/large"));
		return locations;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IModelHandler createModelHandler(IModuleState state) {
		IModuleItemContainer container = state.getContainer().getItemContainer();
		return new ModelHandlerModuleStorage(ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "moduleStorage"),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "top"),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "back"),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "wall"),
				new ResourceLocation[] { ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/stick_down"),
						ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/stick_up"),
						ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/small_down"),
						ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/small_medium"),
						ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/small_up"),
						ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/medium_medium"),
						ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/medium_up"),
						ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), "module_storage", "front_walls/large") });
	}

	@Override
	public int getAllowedComplexity(IModuleContainer container) {
		IModuleProperties properties = container.getProperties();
		if (properties instanceof IModuleModuleStorageProperties) {
			return ((IModuleModuleStorageProperties) properties).getAllowedComplexity(container);
		}
		return Config.defaultAllowedStorageComplexity;
	}

	@Override
	public ModuleStorage createStorage(IModuleProvider provider, IStoragePosition position) {
		return new ModuleStorage(position, provider, EnumModuleSizes.LARGE, true);
	}

	@Override
	public IStoragePage createPage(IModularAssembler assembler, IModular modular, IStorage storage, IStoragePosition position) {
		if (storage instanceof IDefaultModuleStorage) {
			return new ModuleStoragePage(assembler, (IDefaultModuleStorage) storage);
		}
		return new ModuleStoragePage(assembler, EnumModuleSizes.LARGE, position);
	}
}
