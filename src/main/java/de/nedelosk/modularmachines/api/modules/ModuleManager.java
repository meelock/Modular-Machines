package de.nedelosk.modularmachines.api.modules;

import de.nedelosk.modularmachines.api.modular.IModular;
import de.nedelosk.modularmachines.api.modular.IModularHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public class ModuleManager {

	@CapabilityInject(IModularHandler.class)
	public static Capability<IModularHandler> MODULAR_HANDLER_CAPABILITY;

	public static IModuleContainer getContainerFromItem(ItemStack stack){
		if (stack == null) {
			return null;
		}
		IForgeRegistry<IModuleContainer> containerRegistry = GameRegistry.findRegistry(IModuleContainer.class);
		for(IModuleContainer container : containerRegistry) {
			if (container.getItemStack() != null && container.getItemStack().getItem() != null && stack.getItem() == container.getItemStack().getItem()
					&& stack.getItemDamage() == container.getItemStack().getItemDamage()) {
				if (container.ignorNBT() || ItemStack.areItemStackTagsEqual(stack, container.getItemStack())) {
					return container;
				}
			}
		}
		return null;
	}

}
