package de.nedelosk.modularmachines.common.modules.storaged.tools.jei.lathe;

import javax.annotation.Nonnull;

import de.nedelosk.modularmachines.api.recipes.IRecipe;
import de.nedelosk.modularmachines.api.recipes.IRecipeHandler;
import de.nedelosk.modularmachines.api.recipes.IToolMode;
import de.nedelosk.modularmachines.api.recipes.RecipeRegistry;
import de.nedelosk.modularmachines.common.modules.storaged.tools.recipe.RecipeHandlerToolMode;
import de.nedelosk.modularmachines.common.plugins.jei.ModuleRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class LatheRecipeWrapper extends ModuleRecipeWrapper {

	@Nonnull
	private final IDrawable modeItem;
	@Nonnull
	private final IDrawable modeBackground;

	protected final static ResourceLocation widgetTexture = new ResourceLocation("modularmachines", "textures/gui/widgets.png");

	public LatheRecipeWrapper(IRecipe recipe, String recipeCategoryUid, IGuiHelper guiHelper) {
		super(recipe, recipeCategoryUid);	
		IRecipeHandler handler = RecipeRegistry.getRecipeHandler(recipe.getRecipeCategory());
		IToolMode mode = recipe.get(((RecipeHandlerToolMode)handler).propertyMode);

		modeBackground = guiHelper.createDrawable(widgetTexture, 238, 0, 18, 18);
		modeItem = guiHelper.createDrawable(widgetTexture, 238, 18 * mode.ordinal() + 18, 18, 18);
	}

	@Override
	public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight) {
		modeBackground.draw(minecraft, 84, 0);
		modeItem.draw(minecraft, 84, 0);
	}
}
