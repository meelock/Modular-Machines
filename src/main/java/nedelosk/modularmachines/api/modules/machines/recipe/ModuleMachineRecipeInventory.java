package nedelosk.modularmachines.api.modules.machines.recipe;

import nedelosk.modularmachines.api.modular.basic.IModularDefault;
import nedelosk.modularmachines.api.modular.tile.IModularTileEntity;
import nedelosk.modularmachines.api.modules.inventory.ModuleInventory;
import nedelosk.modularmachines.api.modules.machines.IModuleMachineSaver;
import nedelosk.modularmachines.api.recipes.RecipeItem;
import nedelosk.modularmachines.api.recipes.RecipeRegistry;
import nedelosk.modularmachines.api.utils.ModuleStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public abstract class ModuleMachineRecipeInventory<M extends IModuleMachineRecipe, S extends IModuleMachineSaver> extends ModuleInventory<M, S> {

	public ModuleMachineRecipeInventory(String UID, int slots) {
		super(UID, slots);
	}

	@Override
	public boolean transferInput(ModuleStack<M, S> stackModule, IModularTileEntity tile, EntityPlayer player, int slotID, Container container,
			ItemStack stackItem) {
		RecipeItem input = RecipeRegistry.getRecipeInput(stackModule.getModule().getRecipeName(stackModule), new RecipeItem(slotID, stackItem));
		if (input != null) {
			if (mergeItemStack(stackItem, 36 + input.slotIndex, 37 + input.slotIndex, false, container)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getSizeInventory(ModuleStack<M, S> stack, IModularDefault modular) {
		return stack.getModule().getItemInputs(stack) + stack.getModule().getItemOutputs(stack);
	}
}
