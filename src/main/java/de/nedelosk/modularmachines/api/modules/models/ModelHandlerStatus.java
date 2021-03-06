package de.nedelosk.modularmachines.api.modules.models;

import com.google.common.base.Function;

import de.nedelosk.modularmachines.api.modules.IModelInitHandler;
import de.nedelosk.modularmachines.api.modules.items.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.api.modules.storage.IStorage;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelHandlerStatus extends ModelHandler implements IModelInitHandler {

	public boolean status;
	protected ResourceLocation[] locations;

	public ModelHandlerStatus(String modelFolder, IModuleContainer container, ResourceLocation[] locations) {
		super(modelFolder, container);
		this.locations = locations;
	}

	@Override
	public void reload(IModuleState state, IStorage storage, IModelState modelState, VertexFormat format, Function bakedTextureGetter) {
		if(status){
			bakedModel = getBakedModel(locations[0], modelState, format, bakedTextureGetter);
		}else{
			bakedModel = getBakedModel(locations[1], modelState, format, bakedTextureGetter);
		}

	}

	@Override
	public void initModels(IModuleContainer container) {
		getModelOrDefault(locations[0]);
		getModelOrDefault(locations[1]);
	}
}
