package nedelosk.forestday.common.plugins.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import nedelosk.forestday.common.core.Defaults;
import nedelosk.forestday.common.plugins.nei.machines.KilnHandler;
import nedelosk.forestday.common.plugins.nei.machines.WorkbenchHandler;

public class NEIConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {
		
		API.registerRecipeHandler(new KilnHandler());
		API.registerUsageHandler(new KilnHandler());
		API.registerRecipeHandler(new WorkbenchHandler());
		API.registerUsageHandler(new WorkbenchHandler());
		
	}

	@Override
	public String getName() {
		return "Forest Day NEI Plugin";
	}

	@Override
	public String getVersion() {
		return Defaults.VERSION;
	}
}