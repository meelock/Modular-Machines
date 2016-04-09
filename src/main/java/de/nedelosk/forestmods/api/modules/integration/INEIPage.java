package de.nedelosk.forestmods.api.modules.integration;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.nedelosk.forestmods.api.modules.handlers.IPage;
import de.nedelosk.forestmods.api.recipes.IRecipe;

@SideOnly(Side.CLIENT)
public interface INEIPage extends IPage {

	void setRecipe(IRecipe recipe);

	IRecipe getRecipe();

	void createSlots(List<SlotNEI> modularSlots);
}
