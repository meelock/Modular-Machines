package nedelosk.modularmachines.api.modular.managers;

import java.util.List;
import java.util.Map;

import nedelosk.forestcore.library.tile.TileBaseInventory;
import nedelosk.modularmachines.api.modular.tile.IModularTileEntity;
import nedelosk.modularmachines.api.modules.container.gui.IGuiContainer;
import nedelosk.modularmachines.api.modules.container.module.IModuleContainer;
import nedelosk.modularmachines.api.modules.gui.IModuleGui;
import nedelosk.modularmachines.api.utils.ModuleStack;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

public interface IModularGuiManager extends IModularManager {

	Map<String, IGuiContainer> getGuis();

	List<IModuleGui> getAllGuis();

	IModuleGui getCurrentGui();

	void setCurrentGui(IModuleGui gui);

	void searchForGuis();

	IModuleGui getGui(ModuleStack stack);

	IModuleGui getGui(String UID);

	void addGui(IModuleGui gui, ModuleStack stack, IModuleContainer moduleContainer);

	<T extends TileBaseInventory & IModularTileEntity> GuiContainer getGUIContainer(T tile, InventoryPlayer inventory);
}
