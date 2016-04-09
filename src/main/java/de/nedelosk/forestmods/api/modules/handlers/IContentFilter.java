package de.nedelosk.forestmods.api.modules.handlers;

import de.nedelosk.forestmods.api.modules.IModuleAdvanced;
import de.nedelosk.forestmods.api.utils.ModuleStack;
import net.minecraftforge.common.util.ForgeDirection;

public interface IContentFilter<C> {

	boolean isValid(int index, C content, ModuleStack<IModuleAdvanced> moduleStack, ForgeDirection facing);
}
