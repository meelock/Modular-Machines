package de.nedelosk.modularmachines.api.modular.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;

public interface IModularHandlerEntity<N extends NBTBase, K> extends IModularHandler<N, K> {

	int getID();

	Entity getEntity();
}
