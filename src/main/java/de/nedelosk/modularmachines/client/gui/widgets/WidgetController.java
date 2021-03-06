package de.nedelosk.modularmachines.client.gui.widgets;

import java.util.ArrayList;

import de.nedelosk.modularmachines.api.gui.IGuiProvider;
import de.nedelosk.modularmachines.api.gui.Widget;
import de.nedelosk.modularmachines.api.modules.controller.IModuleControlled;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.common.network.PacketHandler;
import de.nedelosk.modularmachines.common.network.packets.PacketSyncPermission;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;

public class WidgetController extends Widget {

	public IModuleState state;
	public IModuleState<IModuleControlled> control;

	public WidgetController(int posX, int posY, IModuleState<IModuleControlled> control, IModuleState state) {
		super(posX, posY, 18, 18);
		this.state = state;
		this.control = control;
	}

	@Override
	public ArrayList<String> getTooltip(IGuiProvider gui) {
		ArrayList<String> list = new ArrayList<>();
		list.add(state.getContainer().getDisplayName());
		return list;
	}

	@Override
	public void draw(IGuiProvider gui) {
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.enableAlpha();
		Minecraft.getMinecraft().renderEngine.bindTexture(widgetTexture);
		int sx = gui.getGuiLeft();
		int sy = gui.getGuiTop();
		boolean hasPermission = control.getModule().getModuleControl(control).hasPermission(state);
		gui.getGui().drawTexturedModalRect(sx + pos.x, sy + pos.y, hasPermission ? 220 : 148, 0, 18, 18);
		ItemStack stack = state.getStack();
		if(stack == null){
			stack = state.getContainer().getItemStack();
		}
		gui.drawItemStack(stack, sx + pos.x + 1, sy + pos.y + 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(widgetTexture);
		if(!hasPermission){
			gui.getGui().drawTexturedModalRect(sx + pos.x, sy + pos.y, 130, 0, 18, 18);
		}
		GlStateManager.disableAlpha();
	}

	@Override
	public void handleMouseClick(int mouseX, int mouseY, int mouseButton, IGuiProvider gui) {
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

		control.getModule().getModuleControl(control).setPermission(state, !control.getModule().getModuleControl(control).hasPermission(state));

		PacketHandler.INSTANCE.sendToServer(new PacketSyncPermission(state.getModular().getHandler(), control, state));
	}
}