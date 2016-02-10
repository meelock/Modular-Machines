package de.nedelosk.forestmods.client.gui.widgets;

import de.nedelosk.forestcore.gui.Widget;
import de.nedelosk.forestcore.gui.WidgetManager;
import de.nedelosk.forestmods.api.modular.basic.IModularDefault;
import de.nedelosk.forestmods.api.modular.tile.IModularTileEntity;
import de.nedelosk.forestmods.api.modules.gui.IModuleGui;
import de.nedelosk.forestmods.api.utils.ModularUtils;
import de.nedelosk.forestmods.api.utils.ModuleStack;
import de.nedelosk.forestmods.client.gui.GuiModularMachines;

public class WidgetManagerModular extends WidgetManager<GuiModularMachines> {

	public WidgetManagerModular(GuiModularMachines gui) {
		super(gui);
	}

	@Override
	public void handleMouseClicked(int mouseX, int mouseY, int mouseButton) {
		Widget widget = getAtPosition(mouseX - gui.getGuiLeft(), mouseY - gui.getGuiTop());
		if (widget != null) {
			widget.handleMouseClick(mouseX, mouseY, mouseButton, gui);
			IModularDefault modular = ((IModularTileEntity<IModularDefault>) getGui().getTile()).getModular();
			IModuleGui gui = modular.getGuiManager().getCurrentGui();
			ModuleStack stack = ModularUtils.getStackFromGui(modular, gui);
			gui.handleMouseClicked((IModularTileEntity) getGui().getTile(), widget, mouseX, mouseY, mouseButton, stack);
		}
	}
}
