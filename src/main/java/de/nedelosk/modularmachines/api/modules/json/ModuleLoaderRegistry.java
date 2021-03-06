package de.nedelosk.modularmachines.api.modules.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import de.nedelosk.modularmachines.api.modules.IModule;
import de.nedelosk.modularmachines.api.modules.IModuleProperties;
import de.nedelosk.modularmachines.api.modules.items.IModuleContainer;
import net.minecraft.util.ResourceLocation;

public class ModuleLoaderRegistry{

	private ModuleLoaderRegistry() {
	}

	private static final Map<EnumLoaderType, List<ICustomLoader>> loaders = new HashMap();
	public static ICustomLoader defaultModuleLoader;
	public static ICustomLoader defaultContainerLoader;

	static{
		loaders.put(EnumLoaderType.MODULE, new ArrayList<>());
		loaders.put(EnumLoaderType.PROPERTY, new ArrayList<>());
		loaders.put(EnumLoaderType.CONTAINER, new ArrayList<>());
	}

	public static void registerLoader(EnumLoaderType loaderType, ICustomLoader loader){
		loaders.get(loaderType).add(loader);
	}

	public static IModule loadModuleFromJson(JsonObject jsonObject){
		ICustomLoader loader = null;
		if(jsonObject.has("loader") && jsonObject.get("loader").isJsonPrimitive() && jsonObject.get("loader").getAsJsonPrimitive().isString()){
			ResourceLocation loaderLocation = new ResourceLocation(jsonObject.get("loader").getAsString());
			for(ICustomLoader customLoader : loaders.get(EnumLoaderType.MODULE)){
				if(customLoader.accepts(loaderLocation)){
					loader = customLoader;
					break;
				}
			}
		}
		if(loader == null){
			loader = defaultModuleLoader;
		}
		return (IModule) loader.loadFromJson(jsonObject);
	}

	public static IModuleProperties loadPropertiesFromJson(JsonObject jsonObject){
		ICustomLoader loader = null;
		if(jsonObject.has("loader") && jsonObject.get("loader").isJsonPrimitive() && jsonObject.get("loader").getAsJsonPrimitive().isString()){
			ResourceLocation loaderLocation = new ResourceLocation(jsonObject.get("loader").getAsString());
			for(ICustomLoader customLoader : loaders.get(EnumLoaderType.PROPERTY)){
				if(customLoader.accepts(loaderLocation)){
					loader = customLoader;
					break;
				}
			}
		}
		if(loader == null){
			return null;
		}
		return (IModuleProperties) loader.loadFromJson(jsonObject);
	}

	public static IModuleContainer loadContainerFromJson(JsonObject jsonObject){
		ICustomLoader loader = null;
		if(jsonObject.has("loader") && jsonObject.get("loader").isJsonPrimitive() && jsonObject.get("loader").getAsJsonPrimitive().isString()){
			ResourceLocation loaderLocation = new ResourceLocation(jsonObject.get("loader").getAsString());
			for(ICustomLoader customLoader : loaders.get(EnumLoaderType.CONTAINER)){
				if(customLoader.accepts(loaderLocation)){
					loader = customLoader;
					break;
				}
			}
		}
		if(loader == null){
			loader = defaultContainerLoader;
		}
		return (IModuleContainer) loader.loadFromJson(jsonObject);
	}

	public static List<IModuleContainer> loadContainersFromJson(JsonElement jsonElement){
		List<IModuleContainer> containers = new ArrayList<>();
		if(jsonElement.isJsonArray()){
			JsonArray array = jsonElement.getAsJsonArray();
			for(JsonElement entry : array){
				if(entry.isJsonObject()){
					IModuleContainer container = loadContainerFromJson(entry.getAsJsonObject());
					if(container != null){
						containers.add(container);
					}
				}
			}
		}else{
			IModuleContainer container = loadContainerFromJson(jsonElement.getAsJsonObject());
			if(container != null){
				containers.add(container);
			}
		}
		return containers;
	}
}
