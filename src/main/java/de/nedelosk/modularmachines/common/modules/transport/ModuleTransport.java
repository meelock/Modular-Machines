package de.nedelosk.modularmachines.common.modules.transport;

import de.nedelosk.modularmachines.api.modular.handlers.IModularHandler;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandlerTileEntity;
import de.nedelosk.modularmachines.api.modules.Module;
import de.nedelosk.modularmachines.api.modules.items.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.position.EnumModulePositions;
import de.nedelosk.modularmachines.api.modules.position.IModulePositioned;
import de.nedelosk.modularmachines.api.modules.position.IModulePostion;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.api.modules.transports.EnumTransportMode;
import de.nedelosk.modularmachines.api.modules.transports.IModuleTransport;
import de.nedelosk.modularmachines.common.network.PacketHandler;
import de.nedelosk.modularmachines.common.network.packets.PacketSyncModule;
import net.minecraft.world.WorldServer;

public class ModuleTransport extends Module implements IModuleTransport, IModulePositioned {

	protected int transportedAmount;

	public ModuleTransport(String name) {
		super(name);
	}

	@Override
	public EnumTransportMode getMode() {
		return null;
	}

	@Override
	public IModulePostion[] getValidPositions(IModuleContainer container) {
		return new IModulePostion[]{EnumModulePositions.CASING};
	}

	@Override
	public void sendModuleUpdate(IModuleState state) {
		IModularHandler handler = state.getModular().getHandler();
		if(handler instanceof IModularHandlerTileEntity){
			PacketHandler.sendToNetwork(new PacketSyncModule(handler, state), ((IModularHandlerTileEntity)handler).getPos(), (WorldServer) handler.getWorld());
		}
	}

}
