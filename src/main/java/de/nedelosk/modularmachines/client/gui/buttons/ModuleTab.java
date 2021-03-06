package de.nedelosk.modularmachines.client.gui.buttons;

import java.util.Arrays;
import java.util.List;

import de.nedelosk.modularmachines.api.gui.Button;
import de.nedelosk.modularmachines.api.modular.IModular;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandler;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.client.gui.GuiPage;
import de.nedelosk.modularmachines.common.network.PacketHandler;
import de.nedelosk.modularmachines.common.network.packets.PacketSelectModule;
import de.nedelosk.modularmachines.common.network.packets.PacketSelectModulePage;
import de.nedelosk.modularmachines.common.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ModuleTab extends Button<GuiPage<IModularHandler>> {

	protected static final ResourceLocation guiTexture = new ResourceLocation("modularmachines", "textures/gui/modular_widgets.png");
	public final IModuleState state;
	public final IModularHandler tile;
	public final boolean right;

	public ModuleTab(int ID, int xPosition, int yPosition, IModuleState state, IModularHandler tile, boolean right) {
		super(ID, xPosition, yPosition, 28, 21, null);
		this.state = state;
		this.right = right;
		this.tile = tile;
	}

	@Override
	public void drawButton(Minecraft mc, int mx, int my) {
		GlStateManager.color(1F, 1F, 1F, 1F);
		RenderUtil.bindTexture(guiTexture);
		getGui().getGui().drawTexturedModalRect(xPosition, yPosition, (state.equals(tile.getModular().getCurrentModule())) ? 0 : 28,
				right ? 214 : 235, 28, 21);
		ItemStack stack = state.getStack();
		if(stack == null){
			stack = state.getContainer().getItemStack();
		}
		getGui().drawItemStack(stack, xPosition + (right ? 5 : 7), yPosition + 2);
	}

	@Override
	public void onButtonClick() {
		IModular modular = getGui().getHandler().getModular();
		IModuleState currentModule = modular.getCurrentModule();
		if (currentModule.getIndex() != state.getIndex()) {
			modular.setCurrentModule(state);
			PacketHandler.INSTANCE.sendToServer(new PacketSelectModule(tile, state));
			PacketHandler.INSTANCE.sendToServer(new PacketSelectModulePage(tile, tile.getModular().getCurrentPage().getPageID()));
		}
	}

	@Override
	public List<String> getTooltip() {
		return Arrays.asList(state.getContainer().getDisplayName());
	}
}
