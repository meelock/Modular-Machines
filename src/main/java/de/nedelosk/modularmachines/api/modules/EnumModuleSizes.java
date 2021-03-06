package de.nedelosk.modularmachines.api.modules;

import java.util.Locale;

import net.minecraft.util.text.translation.I18n;

public enum EnumModuleSizes {
	UNKNOWN(0), 
	/* S * 1 */
	SMALL(1),
	/* S * 2 */
	/* M * 1 */
	MEDIUM(2),
	/* S * 3 */
	/* M * 1.5 */
	/* L * 1 */
	LARGE(3),
	/* S * 6 */
	/* M * 3 */
	/* L * 2 */
	LARGER(6), 
	/* S * 9 */
	/* M * 4.5 */
	/* L * 3 */
	LARGEST(9);

	public int slots;
	public static final EnumModuleSizes[] VALUES = values();


	private EnumModuleSizes(int slots) {
		this.slots = slots;
	}

	public static EnumModuleSizes getSize(EnumModuleSizes firstSize, EnumModuleSizes secondSize){
		if(firstSize == null && secondSize != null){
			return secondSize;
		}else if(firstSize != null && secondSize == null){
			return firstSize;
		}else if(firstSize == null && secondSize == null){
			return UNKNOWN;
		}
		int newSize = firstSize.ordinal() + secondSize.ordinal();
		if(VALUES.length > newSize){
			return VALUES[newSize];
		}
		return UNKNOWN;
	}

	public String getLocalizedName(){
		return I18n.translateToLocal("module.size." + name().toLowerCase(Locale.ENGLISH) + ".name");
	}

	public String getName(){
		return name().toLowerCase(Locale.ENGLISH);
	}

}
