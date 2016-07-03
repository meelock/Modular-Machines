package de.nedelosk.modularmachines.client.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL11;

import de.nedelosk.modularmachines.api.gui.IGuiBase;
import de.nedelosk.modularmachines.api.gui.IWidgetManager;
import de.nedelosk.modularmachines.common.utils.RenderUtil;
import net.minecraft.client.Minecraft;

public class WidgetManager<G extends IGuiBase> implements IWidgetManager<G> {

	public final G gui;
	public final Minecraft minecraft;
	protected final List<Widget> widgets = new ArrayList<Widget>();

	public WidgetManager(G gui) {
		this.gui = gui;
		this.minecraft = Minecraft.getMinecraft();
	}

	@Override
	public void add(Widget slot) {
		if (!widgets.contains(slot)) {
			this.widgets.add(slot);
		}
	}

	@Override
	public void addAll(Collection<Widget> slots) {
		if (slots == null) {
			return;
		}
		for(Widget slot : slots) {
			if (!widgets.contains(slot)) {
				widgets.add(slot);
			}
		}
	}

	@Override
	public void remove(Widget slot) {
		this.widgets.remove(slot);
	}

	public void clear() {
		this.widgets.clear();
	}

	@Override
	public Widget getWidgetAtMouse(int mouseX, int mouseY) {
		for(Widget slot : widgets) {
			if (slot.isMouseOver(mouseX, mouseY)) {
				return slot;
			}
		}
		return null;
	}

	public void drawWidgets() {
		for(Widget slot : widgets) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			slot.draw(gui);
		}
		for(Widget slot : widgets) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			slot.drawStrings(gui);
		}
	}

	public boolean keyTyped(char keyChar, int keyCode) {
		for(Widget slot : widgets) {
			if (slot.keyTyped(keyChar, keyCode, gui)) {
				return true;
			}
		}
		return false;
	}

	public void drawTooltip(int mX, int mY) {
		for(Widget slot : widgets) {
			if (slot.isMouseOver(mX - gui.getGuiLeft(), mY - gui.getGuiTop())) {
				if(slot.showTooltip) {
					RenderUtil.renderTooltip(mX, mY, slot.getTooltip(gui));
				}
			}
		}
	}

	public void handleMouseClicked(int mouseX, int mouseY, int mouseButton) {
		Widget slot = getWidgetAtMouse(mouseX - gui.getGuiLeft(), mouseY - gui.getGuiTop());
		if (slot != null) {
			slot.handleMouseClick(mouseX, mouseY, mouseButton, gui);
		}
	}

	@Override
	public List<Widget> getWidgets() {
		return widgets;
	}

	@Override
	public G getGui() {
		return gui;
	}
}