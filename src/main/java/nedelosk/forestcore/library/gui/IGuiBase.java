package nedelosk.forestcore.library.gui;

import java.util.List;

import nedelosk.forestcore.library.inventory.IGuiHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;

public interface IGuiBase<T extends IGuiHandler> {

	IButtonManager getButtonManager();

	IWidgetManager getWidgetManager();

	EntityPlayer getPlayer();

	T getTile();

	void setZLevel(float zLevel);

	float getZLevel();

	int getGuiLeft();

	int getGuiTop();

	List<GuiButton> getButtons();

	FontRenderer getFontRenderer();

	void drawTexturedModalRect(int x, int y, int tx, int ty, int w, int h);
}