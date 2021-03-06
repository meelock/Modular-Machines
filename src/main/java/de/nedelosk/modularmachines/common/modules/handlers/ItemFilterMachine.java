package de.nedelosk.modularmachines.common.modules.handlers;

import de.nedelosk.modularmachines.api.modules.handlers.filters.IContentFilter;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.api.modules.tools.IModuleMachine;
import de.nedelosk.modularmachines.api.recipes.RecipeItem;
import net.minecraft.item.ItemStack;

public class ItemFilterMachine implements IContentFilter<ItemStack, IModuleMachine> {

	@Override
	public boolean isValid(int index, ItemStack content, IModuleState<IModuleMachine> state) {
		return state.getModule().isRecipeInput(state, new RecipeItem(index, content));
	}
}
