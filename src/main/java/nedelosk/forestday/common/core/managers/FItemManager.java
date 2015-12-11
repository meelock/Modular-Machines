package nedelosk.forestday.common.core.managers;

import nedelosk.forestday.common.core.registry.FRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public enum FItemManager {

	// Materials
	Nature, Crop_Corn,

	Bucket_Wood, Bucket_Wood_Water, Ingots, Nuggets, Gems, Gears_Wood,

	// Campfire
	Curb, Pot, Pot_Holder,

	// Tools
	File_Stone, File_Iron, File_Diamond, Knife_Stone, Cutter, Hammer, Adze, Adze_Long, Axe_Flint, Tool_Parts;

	private Item item;

	public void registerItem(Item item) {
		this.item = item;
		FRegistry.registerItem(item, item.getUnlocalizedName(), "fd");
	}

	public boolean isItemEqual(ItemStack stack) {
		return stack != null && this.item == stack.getItem();
	}

	public boolean isItemEqual(Item i) {
		return i != null && this.item == i;
	}

	public Item item() {
		return item;
	}
}
