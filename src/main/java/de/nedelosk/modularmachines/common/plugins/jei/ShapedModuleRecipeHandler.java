package de.nedelosk.modularmachines.common.plugins.jei;

public class ShapedModuleRecipeHandler /*extends TemplateRecipeHandler*/ {

	/*public class CachedShapedRecipe extends CachedRecipe {

		public ArrayList<PositionedStack> ingredients;
		public PositionedStack result;

		public CachedShapedRecipe(int width, int height, Object[] items, ItemStack out) {
			result = new PositionedStack(out, 119, 24);
			ingredients = new ArrayList<PositionedStack>();
			setIngredients(width, height, items);
		}

		/**
	 * @param width
	 * @param height
	 * @param items
	 *            an ItemStack[] or ItemStack[][]

		public void setIngredients(int width, int height, Object[] items) {
			for(int x = 0; x < width; x++) {
				for(int y = 0; y < height; y++) {
					if (items[y * width + x] == null) {
						continue;
					}
					PositionedStack stack = new PositionedStack(items[y * width + x], 25 + x * 18, 6 + y * 18, false);
					stack.setMaxSize(1);
					ingredients.add(stack);
				}
			}
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(cycleticks / 20, ingredients);
		}

		@Override
		public PositionedStack getResult() {
			return result;
		}

		public void computeVisuals() {
			for(PositionedStack p : ingredients) {
				p.generatePermutations();
			}
		}
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18), "crafting"));
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiCrafting.class;
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("recipe.shaped.modular");
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("crafting") && getClass() == ShapedModuleRecipeHandler.class) {
			for(IRecipe irecipe : CraftingManager.getInstance().getRecipeList()) {
				CachedShapedRecipe recipe = null;
				if (irecipe instanceof ShapedModuleRecipe) {
					recipe = moduleShapedRecipe((ShapedModuleRecipe) irecipe);
				}
				if (recipe == null) {
					continue;
				}
				recipe.computeVisuals();
				arecipes.add(recipe);
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for(IRecipe irecipe : CraftingManager.getInstance().getRecipeList()) {
			if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result) && result.hasTagCompound()
					&& result.getTagCompound().equals(irecipe.getRecipeOutput().getTagCompound())) {
				CachedShapedRecipe recipe = null;
				if (irecipe instanceof ShapedModuleRecipe) {
					recipe = moduleShapedRecipe((ShapedModuleRecipe) irecipe);
				}
				if (recipe == null) {
					continue;
				}
				recipe.computeVisuals();
				arecipes.add(recipe);
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		for(IRecipe irecipe : CraftingManager.getInstance().getRecipeList()) {
			CachedShapedRecipe recipe = null;
			if (irecipe instanceof ShapedModuleRecipe) {
				recipe = moduleShapedRecipe((ShapedModuleRecipe) irecipe);
			}
			if (recipe == null || !recipe.contains(recipe.ingredients, ingredient.getItem())) {
				continue;
			}
			recipe.computeVisuals();
			if (recipe.contains(recipe.ingredients, ingredient)) {
				recipe.setIngredientPermutation(recipe.ingredients, ingredient);
				arecipes.add(recipe);
			}
		}
	}

	public CachedShapedRecipe moduleShapedRecipe(ShapedModuleRecipe recipe) {
		int width;
		int height;
		try {
			width = ReflectionManager.getField(ShapedModuleRecipe.class, Integer.class, recipe, 4);
			height = ReflectionManager.getField(ShapedModuleRecipe.class, Integer.class, recipe, 5);
		} catch (Exception e) {
			NEIClientConfig.logger.error("Error loading recipe", e);
			return null;
		}
		Object[] items = recipe.getInput();
		for(Object item : items) {
			if (item instanceof List && ((List<?>) item).isEmpty()) {
				// handler,
				// no ores
				return null;
			}
		}
		return new CachedShapedRecipe(width, height, items, recipe.getRecipeOutput());
	}

	@Override
	public String getGuiTexture() {
		return "textures/gui/container/crafting_table.png";
	}

	@Override
	public String getOverlayIdentifier() {
		return "crafting";
	}

	@Override
	public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
		return super.hasOverlay(gui, container, recipe) || isRecipe2x2(recipe) && RecipeInfo.hasDefaultOverlay(gui, "crafting2x2");
	}

	@Override
	public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe) {
		IRecipeOverlayRenderer renderer = super.getOverlayRenderer(gui, recipe);
		if (renderer != null) {
			return renderer;
		}
		IStackPositioner positioner = RecipeInfo.getStackPositioner(gui, "crafting2x2");
		if (positioner == null) {
			return null;
		}
		return new DefaultOverlayRenderer(getIngredientStacks(recipe), positioner);
	}

	@Override
	public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe) {
		IOverlayHandler handler = super.getOverlayHandler(gui, recipe);
		if (handler != null) {
			return handler;
		}
		return RecipeInfo.getOverlayHandler(gui, "crafting2x2");
	}

	public boolean isRecipe2x2(int recipe) {
		for(PositionedStack stack : getIngredientStacks(recipe)) {
			if (stack.relx > 43 || stack.rely > 24) {
				return false;
			}
		}
		return true;
	}*/
}
