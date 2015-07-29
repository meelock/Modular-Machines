package nedelosk.modularmachines.common.network.packets;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import nedelosk.modularmachines.common.network.packets.assembler.PacketModularAssembler;
import nedelosk.modularmachines.common.network.packets.assembler.PacketModularAssemblerBookmark;
import nedelosk.modularmachines.common.network.packets.assembler.PacketModularAssemblerBuildMachine;
import nedelosk.modularmachines.common.network.packets.machine.PacketModularMachine;
import nedelosk.modularmachines.common.network.packets.machine.PacketModularMachineNBT;

public class PacketHandler {
	
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("modularmachines");

    private static int ID = 0;
    
    public static void preInit()
    {
    	INSTANCE.registerMessage(PacketModularAssembler.class, PacketModularAssembler.class, nextID(), Side.SERVER);
    	INSTANCE.registerMessage(PacketModularAssemblerBookmark.class, PacketModularAssemblerBookmark.class, nextID(), Side.SERVER);
    	INSTANCE.registerMessage(PacketModularAssemblerBuildMachine.class, PacketModularAssemblerBuildMachine.class, nextID(), Side.SERVER);
    	INSTANCE.registerMessage(PacketModularMachine.class, PacketModularMachine.class, nextID(), Side.SERVER);
    	INSTANCE.registerMessage(PacketModularMachineNBT.class, PacketModularMachineNBT.class, nextID(), Side.SERVER);
    	INSTANCE.registerMessage(PacketModularMachineNBT.class, PacketModularMachineNBT.class, nextID(), Side.CLIENT);
    }

    public static int nextID()
    {
        return ID++;
    }
	
}
