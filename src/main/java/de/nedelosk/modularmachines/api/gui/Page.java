package de.nedelosk.modularmachines.api.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Page<T extends IGuiHandler> implements IPage<T> {

	protected String title;
	@SideOnly(Side.CLIENT)
	protected IGuiProvider<T> gui;

	public Page(String title) {
		this.title = title;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateGui() {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initGui(){
	}

	@SideOnly(Side.CLIENT)
	protected boolean renderPageTitle() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	protected abstract ResourceLocation getGuiTexture();

	@SideOnly(Side.CLIENT)
	@Override
	public void drawForeground(FontRenderer fontRenderer, int mouseX, int mouseY) {
		if (renderPageTitle()) {
			fontRenderer.drawString(getPageTitle(), 90 - (fontRenderer.getStringWidth(getPageTitle()) / 2),
					6, 4210752);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawBackground(int mouseX, int mouseY) {
		if(gui != null && getGuiTexture() != null){
			Minecraft.getMinecraft().renderEngine.bindTexture(getGuiTexture());
			gui.getGui().drawTexturedModalRect(gui.getGuiLeft(), gui.getGuiTop(), 0, 0, getXSize(), getYSize());
		}

		drawPlayerInventory();
	}

	@SideOnly(Side.CLIENT)
	protected void drawPlayerInventory() {
		if(gui != null && getInventoryTexture() != null && getPlayerInvPosition() >= 0){
			GlStateManager.enableAlpha();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			Minecraft.getMinecraft().renderEngine.bindTexture(getInventoryTexture());
			int invPosition = getPlayerInvPosition();
			gui.getGui().drawTexturedModalRect(gui.getGuiLeft() + 7, gui.getGuiTop() + invPosition, 0, 0, 162, 76);
			GlStateManager.disableAlpha();
		}
	}

	@SideOnly(Side.CLIENT)
	protected ResourceLocation getInventoryTexture() {
		return new ResourceLocation("modularmachines:textures/gui/inventory_player.png");
	}

	public int getPlayerInvPosition() {
		return -1;
	}


	@SideOnly(Side.CLIENT)
	@Override
	public void drawFrontBackground(int mouseX, int mouseY) {
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getXSize() {
		return 176;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getYSize() {
		return 166;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addButtons() {
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addWidgets() {
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void handleMouseClicked(int mouseX, int mouseY, int mouseButton) {
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawTooltips(int mouseX, int mouseY) {
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IGuiProvider getGui() {
		return gui;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setGui(IGuiProvider gui) {
		this.gui = gui;
	}

	@Override
	public String getPageTitle(){
		return I18n.translateToLocal(title);
	}

}
