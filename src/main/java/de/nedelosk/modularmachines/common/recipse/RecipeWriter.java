package de.nedelosk.modularmachines.common.recipse;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import de.nedelosk.modularmachines.api.recipes.IRecipe;
import de.nedelosk.modularmachines.api.recipes.IRecipeHandler;
import de.nedelosk.modularmachines.api.recipes.IRecipeJsonSerializer;
import de.nedelosk.modularmachines.api.recipes.RecipeRegistry;
import de.nedelosk.modularmachines.common.recipse.RecipeJsonManager.RecipeEntry;
import de.nedelosk.modularmachines.common.utils.JsonUtils;

public class RecipeWriter implements JsonSerializer<RecipeEntry> {

	@Override
	public JsonElement serialize(RecipeEntry obj, Type typeOfSrc, JsonSerializationContext context) {
		IRecipe src = obj.recipe;
		JsonObject json = new JsonObject();
		json.addProperty("isActive", obj.isActive);
		json.addProperty("RecipeName", src.getRecipeName());
		json.addProperty("RecipeCategory", src.getRecipeCategory());
		json.addProperty("MaterialModifier", src.getRequiredMaterial());
		json.addProperty("SpeedModifier", src.getRequiredSpeedModifier());
		json.add("Inputs", JsonUtils.writeRecipeItem(src.getInputs()));
		json.add("Outputs", JsonUtils.writeRecipeItem(src.getOutputs()));
		IRecipeHandler handler = RecipeRegistry.getRecipeHandler(src.getRecipeCategory());
		if (handler != null) {
			IRecipeJsonSerializer serializer = handler.getJsonSerialize();
			if (serializer != null) {
				JsonObject craftingModifiers = serializer.serializeJson(src.getModifiers());
				if (craftingModifiers != null) {
					json.add("CraftingModifiers", craftingModifiers);
				}
			}
		}
		return json;
	}
}