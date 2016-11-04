package modularmachines.common.plugins.jei;

import java.awt.Rectangle;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.BlankAdvancedGuiHandler;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import modularmachines.api.gui.Widget;
import modularmachines.api.modules.IModule;
import modularmachines.api.modules.ModuleManager;
import modularmachines.api.modules.containers.IModuleContainer;
import modularmachines.api.modules.containers.IModuleItemContainer;
import modularmachines.api.modules.integration.IModuleJEI;
import modularmachines.client.gui.GuiAssembler;
import modularmachines.client.gui.GuiModuleCrafter;
import modularmachines.client.gui.GuiPage;
import modularmachines.client.gui.widgets.WidgetFluidTank;
import modularmachines.common.core.managers.BlockManager;
import modularmachines.common.inventory.ContainerModuleCrafter;
import modularmachines.common.plugins.jei.alloysmelter.AlloySmelterRecipeCategory;
import modularmachines.common.plugins.jei.alloysmelter.AlloySmelterRecipeWrapper;
import modularmachines.common.plugins.jei.boiler.BoilerRecipeCategory;
import modularmachines.common.plugins.jei.boiler.BoilerRecipeWrapper;
import modularmachines.common.plugins.jei.lathe.LatheRecipeCategory;
import modularmachines.common.plugins.jei.lathe.LatheRecipeWrapper;
import modularmachines.common.plugins.jei.pulverizer.PulverizerRecipeCategory;
import modularmachines.common.plugins.jei.pulverizer.PulverizerRecipeWrapper;
import modularmachines.common.utils.Translator;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JeiPlugin extends BlankModPlugin {

	public static boolean isAdded;
	private static List<String> producerHandlers = Lists.newArrayList();
	public static IJeiRuntime jeiRuntime;

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
		subtypeRegistry.useNbtForSubtypes(ModuleManager.defaultModuleItem, ModuleManager.defaultModuleHolderItem);
	}

	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registry.addRecipeClickArea(GuiModuleCrafter.class, 93, 35, 22, 15, VanillaRecipeCategoryUid.CRAFTING, CategoryUIDs.CRAFTING);
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(BlockManager.blockModular));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModuleManager.defaultModuleItemContainer));
		registry.addRecipeCategories(new ModuleCrafterRecipeCategory(guiHelper), new AlloySmelterRecipeCategory(guiHelper), new BoilerRecipeCategory(guiHelper), new PulverizerRecipeCategory(guiHelper), new LatheRecipeCategory(guiHelper));
		for(IModuleItemContainer container : ModuleManager.MODULE_CONTAINERS) {
			for(IModuleContainer moduleContainer : container.getContainers()) {
				IModule module = moduleContainer.getModule();
				if (module instanceof IModuleJEI) {
					registry.addRecipeCategoryCraftingItem(container.getItemStack(), ((IModuleJEI) module).getJEIRecipeCategorys(moduleContainer));
				}
				String description = moduleContainer.getDescription();
				if (moduleContainer.getDescription() != null && Translator.canTranslateToLocal(description)) {
					registry.addDescription(container.getItemStack(), description);
				}
			}
		}
		registry.addDescription(new ItemStack(BlockManager.blockModular), "tile.modular.description");
		registry.addRecipeCategoryCraftingItem(new ItemStack(BlockManager.blockModuleCrafter), CategoryUIDs.CRAFTING);
		registry.addAdvancedGuiHandlers(new AssemblerGuiHandler(), new ModularGuiHandler());
		registry.addRecipeHandlers(new ModuleCrafterRecipeHandler(), new ModuleRecipeHandler(CategoryUIDs.ALLOYSMELTER, AlloySmelterRecipeWrapper.class), new ModuleRecipeHandler(CategoryUIDs.BOILER, BoilerRecipeWrapper.class),
				new ModuleRecipeHandler(CategoryUIDs.PULVERIZER, PulverizerRecipeWrapper.class), new ModuleRecipeHandler(CategoryUIDs.LATHE, LatheRecipeWrapper.class));
		registry.addRecipes(ModuleRecipeWrapper.getRecipes("AlloySmelter", CategoryUIDs.ALLOYSMELTER, AlloySmelterRecipeWrapper.class));
		registry.addRecipes(ModuleRecipeWrapper.getRecipes("Boiler", CategoryUIDs.BOILER, BoilerRecipeWrapper.class));
		registry.addRecipes(ModuleRecipeWrapper.getRecipes("Pulverizer", CategoryUIDs.PULVERIZER, PulverizerRecipeWrapper.class));
		registry.addRecipes(ModuleRecipeWrapper.getRecipes("Lathe", CategoryUIDs.LATHE, LatheRecipeWrapper.class, guiHelper));
		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
		recipeTransferRegistry.addRecipeTransferHandler(ContainerModuleCrafter.class, VanillaRecipeCategoryUid.CRAFTING, 36, 9, 0, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerModuleCrafter.class, CategoryUIDs.CRAFTING, 36, 10, 0, 36);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		JeiPlugin.jeiRuntime = jeiRuntime;
	}

	private static class AssemblerGuiHandler extends BlankAdvancedGuiHandler<GuiAssembler> {

		@Nonnull
		@Override
		public Class<GuiAssembler> getGuiContainerClass() {
			return GuiAssembler.class;
		}

		@Nullable
		@Override
		public List<Rectangle> getGuiExtraAreas(GuiAssembler guiContainer) {
			return guiContainer.getExtraGuiAreas();
		}
	}

	private static class ModularGuiHandler extends BlankAdvancedGuiHandler<GuiPage> {

		@Nonnull
		@Override
		public Class<GuiPage> getGuiContainerClass() {
			return GuiPage.class;
		}

		@Nullable
		@Override
		public Object getIngredientUnderMouse(GuiPage guiContainer, int mouseX, int mouseY) {
			Widget widget = guiContainer.getWidgetManager().getWidgetAtMouse(mouseX, mouseY);
			if(widget instanceof WidgetFluidTank){
				return ((WidgetFluidTank)widget).getProvider().getFluid();
			}
			return null;
		}
	}
}