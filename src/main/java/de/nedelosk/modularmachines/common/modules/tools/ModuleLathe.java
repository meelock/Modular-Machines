package de.nedelosk.modularmachines.common.modules.tools;

import java.util.ArrayList;
import java.util.List;

import de.nedelosk.modularmachines.api.inventory.IContainerBase;
import de.nedelosk.modularmachines.api.modular.IModularHandler;
import de.nedelosk.modularmachines.api.modules.handlers.IModulePage;
import de.nedelosk.modularmachines.api.modules.handlers.inventory.IModuleInventory;
import de.nedelosk.modularmachines.api.modules.handlers.inventory.IModuleInventoryBuilder;
import de.nedelosk.modularmachines.api.modules.handlers.inventory.slots.SlotModule;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.api.modules.tool.IModuleToolAdvanced;
import de.nedelosk.modularmachines.api.recipes.IMachineMode;
import de.nedelosk.modularmachines.api.recipes.RecipeItem;
import de.nedelosk.modularmachines.client.gui.widgets.WidgetButtonMode;
import de.nedelosk.modularmachines.client.gui.widgets.WidgetProgressBar;
import de.nedelosk.modularmachines.common.modules.ModuleToolAdvanced;
import de.nedelosk.modularmachines.common.modules.handlers.ItemFilterMachine;
import de.nedelosk.modularmachines.common.modules.handlers.OutputAllFilter;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleLathe extends ModuleToolAdvanced{

	public ModuleLathe(int speedModifier, int size) {
		super(speedModifier, size, LatheModes.ROD);
	}

	// Recipe
	@Override
	public RecipeItem[] getInputs(IModuleState state) {
		return ((IModuleInventory)state.getContentHandler(ItemStack.class)).getInputItems();
	}

	@Override
	public String getRecipeCategory(IModuleState state) {
		return "Lathe";
	}

	@Override
	public Class<? extends IMachineMode> getModeClass() {
		return LatheModes.class;
	}

	@Override
	public List<IModulePage> createPages(IModuleState state) {
		List<IModulePage> pages = super.createPages(state);
		pages.add(new ModuleLathePage("Basic", state));
		return pages;
	}

	/*@SideOnly(Side.CLIENT)
	@Override
	public IJEIPage createNEIPage(IModuleJEI module) {
		return new LatheNEIPage(module);
	}

	@SideOnly(Side.CLIENT)
	public static class LatheNEIPage extends NEIPage {

		public LatheNEIPage(IModuleJEI module) {
			super(module);
		}

		@Override
		public void createSlots(List<SlotJEI> modularSlots) {
			modularSlots.add(new SlotJEI(54, 24, true));
			modularSlots.add(new SlotJEI(116, 24, false));
			modularSlots.add(new SlotJEI(134, 24, false));
		}

		@Override
		public void addWidgets(List widgets) {
			widgets.add(new WidgetProgressBar(82, 25, 0, 0).setShowTooltip(false));
			widgets.add(new WidgetButtonMode(86, 0, (IMachineMode) recipe.getModifiers()[0]));
		}
	}*/

	public static class ModuleLathePage extends ModuleAdvancedPage{

		public ModuleLathePage(String pageID, IModuleState<IModuleToolAdvanced> state) {
			super(pageID, state);
		}

		@SideOnly(Side.CLIENT)
		@Override
		public void addWidgets(List widgets) {
			int burnTime = 0;
			int burnTimeTotal = 0;

			widgets.add(new WidgetProgressBar(82, 36, state.getModule().getWorkTime(state), state.getModule().getWorkTimeTotal(state)));
			gui.getWidgetManager().add(new WidgetButtonMode(86, 16, state.getModule().getCurrentMode(state)));
		}

		@Override
		public void createInventory(IModuleInventoryBuilder invBuilder) {
			invBuilder.addInventorySlot(true, new ItemFilterMachine());
			invBuilder.addInventorySlot(true, new ItemFilterMachine());
			invBuilder.addInventorySlot(false, new OutputAllFilter());
			invBuilder.addInventorySlot(false, new OutputAllFilter());
		}

		@Override
		public void createSlots(IContainerBase<IModularHandler> container, List<SlotModule> modularSlots) {
			ArrayList<Slot> list = new ArrayList<Slot>();
			list.add(new SlotModule(state, 0, 54, 35));
			list.add(new SlotModule(state, 0, 54, 35));
			list.add(new SlotModule(state, 1, 116, 35));
			list.add(new SlotModule(state, 2, 134, 35));
		}

	}

	public static enum LatheModes implements IMachineMode {
		ROD("rod"), WIRE("wire"), SCREW("screw");

		private String name;

		private LatheModes(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}

}