package nedelosk.forestday.common.machines.base.wood.campfire;

import net.minecraft.item.ItemStack;

public class CampfireRecipe {
	
	public ItemStack input;
	public ItemStack input2;
	public ItemStack output;
	public int burnTime;
	public int potTier;
	
	public CampfireRecipe(ItemStack input, ItemStack input2, ItemStack output, int potTier, int burnTime){
		this.input = input;
		this.input2 = input2;
		this.output = output;
		this.burnTime = burnTime;
		this.potTier = potTier;
	}
	
	public CampfireRecipe(ItemStack input, ItemStack output, int potTier, int burnTime){
		this.input = input;
		this.output = output;
		this.burnTime = burnTime;
		this.potTier = potTier;
	}
	
}