package de.nedelosk.forestmods.common.modules.producers.recipe.assembler.module;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.nedelosk.forestcore.gui.IGuiBase;
import de.nedelosk.forestcore.gui.Widget;
import de.nedelosk.forestmods.api.modular.IModular;
import de.nedelosk.forestmods.api.modules.handlers.inventory.IModuleInventory;
import de.nedelosk.forestmods.api.modules.recipes.RecipeModuleAssembler;
import de.nedelosk.forestmods.api.recipes.IRecipe;
import de.nedelosk.forestmods.api.recipes.RecipeItem;
import de.nedelosk.forestmods.api.utils.ModuleCategoryUIDs;
import de.nedelosk.forestmods.api.utils.ModuleStack;
import de.nedelosk.forestmods.client.gui.widgets.WidgetProgressBar;

public class ModuleModuleAssembler extends ModuleProducerRecipe {

	public ModuleModuleAssembler() {
		super(ModuleCategoryUIDs.MACHINE_ASSEMBLER_MODULE, 9, 2);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public List<Widget> addNEIWidgets(IGuiBase gui, ModuleStack stack, IRecipe recipe) {
		gui.getWidgetManager().add(new WidgetProgressBar(87, 35, 0, 0));
		return gui.getWidgetManager().getWidgets();
	}

	// NEI
	@SideOnly(Side.CLIENT)
	@Override
	public List<NeiSlot> addNEIStacks(ModuleStack stack, IRecipe recipe) {
		ArrayList<NeiSlot> list = new ArrayList<NeiSlot>();
		list.addAll(new NeiSlot(17, 16, true));
		list.addAll(new NeiSlot(35, 16, true));
		list.addAll(new NeiSlot(53, 16, true));
		list.addAll(new NeiSlot(17, 34, true));
		list.addAll(new NeiSlot(35, 34, true));
		list.addAll(new NeiSlot(53, 34, true));
		list.addAll(new NeiSlot(17, 52, true));
		list.addAll(new NeiSlot(35, 52, true));
		list.addAll(new NeiSlot(53, 52, true));
		list.addAll(new NeiSlot(125, 34, false));
		list.addAll(new NeiSlot(143, 34, false));
		return list;
	}

	// Recipe
	@Override
	public RecipeItem[] getInputs(IModular modular, ModuleStack stack) {
		return getInputItems(modular, stack);
	}

	@Override
	public String getRecipeCategory(ModuleStack stack) {
		return "AssemblerModule";
	}

	@Override
	public Class<? extends IRecipe> getRecipeClass() {
		return RecipeModuleAssembler.class;
	}

	@Override
	public int getColor() {
		return 0x601C93;
	}

	@Override
	public IModuleInventory createInventory(ModuleStack stack) {
		return new ModuleModuleAssemblerInventory(getUID(), itemInputs + itemOutputs);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IModuleGui createGui(ModuleStack stack) {
		return new ModuleModuleAssemblerGui(getUID());
	}
}
