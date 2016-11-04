package modularmachines.api.modules.handlers.filters;

import modularmachines.api.modules.IModule;
import modularmachines.api.modules.state.IModuleState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class ItemFliterFurnaceFuel implements IContentFilter<ItemStack, IModule> {

	public static final ItemFliterFurnaceFuel INSTANCE = new ItemFliterFurnaceFuel();

	private ItemFliterFurnaceFuel() {
	}

	@Override
	public boolean isValid(int index, ItemStack content, IModuleState<IModule> moduleState) {
		if (content == null) {
			return false;
		}
		return TileEntityFurnace.getItemBurnTime(content) > 0;
	}
}