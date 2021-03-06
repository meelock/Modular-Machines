package de.nedelosk.modularmachines.common.plugins.theoneprobe;

import com.google.common.base.Function;

import de.nedelosk.modularmachines.common.config.Config;
import de.nedelosk.modularmachines.common.plugins.APlugin;
import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class PluginTheOneProbe extends APlugin implements Function<ITheOneProbe, Void> {

	public static ITheOneProbe probe;

	@Override
	public Void apply(ITheOneProbe theOneProbe) {
		probe = theOneProbe;
		return null;
	}

	@Override
	public String getRequiredMod() {
		return "theoneprobe";
	}

	@Override
	public void init() {
		FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "de.nedelosk.modularmachines.common.plugins.theoneprobe.PluginTheOneProbe");
	}

	@Override
	public boolean isActive() {
		return Config.pluginTheOneProbe;
	}
}
