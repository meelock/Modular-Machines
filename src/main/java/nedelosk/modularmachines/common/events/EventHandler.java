package nedelosk.modularmachines.common.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nedelosk.modularmachines.api.utils.ModuleRegistry;
import nedelosk.modularmachines.api.utils.ModuleStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class EventHandler {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void tooltipEvent(ItemTooltipEvent event) {
		ModuleStack stack = ModuleRegistry.getProducer(event.itemStack);
		if (stack != null) {
			event.toolTip.add(StatCollector.translateToLocal("mm.module.tooltip.type") + ": " + stack.getMaterial().getLocalName());
			event.toolTip
					.add(StatCollector.translateToLocal("mm.module.tooltip.tier") + ": " + ModuleRegistry.getProducer(event.itemStack).getMaterial().getTier());
			event.toolTip.add(StatCollector.translateToLocal("mm.module.tooltip.name") + ": "
					+ StatCollector.translateToLocal(stack.getModule().getName(stack, false) + ".name"));
			if (stack.getModule() != null) {
				event.toolTip.add(StatCollector.translateToLocal("mm.module.tooltip.producer.name") + ": "
						+ StatCollector.translateToLocal(stack.getModule().getName(stack) + ".name"));
			}
		}
	}
}
