package modularmachines.common.config;

import java.util.ArrayList;
import java.util.List;

import modularmachines.common.config.Config.ConfigGroup;
import modularmachines.common.core.Constants;
import modularmachines.common.utils.Translator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiConfigFactory extends GuiConfig {

	public GuiConfigFactory(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(parentScreen), Constants.MOD_ID, false, false, Translator.translateToLocal("mm.config.title"));
	}

	private static List<IConfigElement> getConfigElements(GuiScreen parent) {
		List<IConfigElement> list = new ArrayList<>();
		String prefix = "mm.config.";
		for(ConfigGroup group : Config.groups) {
			list.add(new ConfigElement(Config.config.getCategory(group.getLowerCase()).setLanguageKey(prefix + group.lang).setRequiresMcRestart(group.reloadMC)));
		}
		return list;
	}
}
