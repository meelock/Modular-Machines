package de.nedelosk.modularmachines.api.material;

import de.nedelosk.modularmachines.api.Translator;

public enum EnumBlockMaterials implements IMaterial {
	WOOD(0, "Wood", "wood"), STONE(1, "Stone", "stone"), BRICK(1, "Brick", "brick");

	private int tier;
	private int color;
	private String name;
	private String unlocalizedName;
	private String[] oreDicts;

	EnumBlockMaterials(int tier, String name, String unlocalizedName) {
		this.tier = tier;
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		MaterialRegistry.registerMaterial(this);
	}

	@Override
	public int getTier() {
		return tier;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getLocalizedName() {
		return Translator.translateToLocal("material." + unlocalizedName + ".name");
	}
}