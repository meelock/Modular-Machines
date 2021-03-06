package de.nedelosk.modularmachines.client.core;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import com.google.common.collect.ImmutableMap;

import de.nedelosk.modularmachines.api.ModularMachinesApi;
import de.nedelosk.modularmachines.api.modules.IModelInitHandler;
import de.nedelosk.modularmachines.api.modules.ModuleEvents;
import de.nedelosk.modularmachines.api.modules.items.IModuleContainer;
import de.nedelosk.modularmachines.common.utils.IColoredBlock;
import de.nedelosk.modularmachines.common.utils.IColoredItem;
import forestry.api.core.IItemModelRegister;
import forestry.api.core.IModelManager;
import forestry.api.core.IStateMapperRegister;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelManager implements IModelManager {

	private static final ModelManager instance = new ModelManager();

	private final List<IItemModelRegister> itemModelRegisters = new ArrayList<>();
	private final List<IStateMapperRegister> stateMapperRegisters = new ArrayList<>();
	private final List<IColoredBlock> blockColorList = new ArrayList<>();
	private final List<IColoredItem> itemColorList = new ArrayList<>();

	private TRSRTransformation flipX = new TRSRTransformation(null, null, new Vector3f(-1, 1, 1), null);
	public final IModelState DEFAULT_BLOCK;
	public final IModelState DEFAULT_ITEM;
	public final IModelState DEFAULT_TOOL;

	public ModelManager() {
		TRSRTransformation blockThirdperson = get(0, 2.5f, 0, 75, 45, 0, 0.375f);
		ImmutableMap.Builder<TransformType, TRSRTransformation> blockBuilder = ImmutableMap.builder();
		blockBuilder.put(TransformType.GUI,                     get(0, 0, 0, 30, 225, 0, 0.625f));
		blockBuilder.put(TransformType.GROUND,                  get(0, 3, 0, 0, 0, 0, 0.25f));
		blockBuilder.put(TransformType.FIXED,                   get(0, 0, 0, 0, 0, 0, 0.5f));
		blockBuilder.put(TransformType.THIRD_PERSON_RIGHT_HAND, blockThirdperson);
		blockBuilder.put(TransformType.THIRD_PERSON_LEFT_HAND,  leftify(blockThirdperson));
		blockBuilder.put(TransformType.FIRST_PERSON_RIGHT_HAND, get(0, 0, 0, 0, 45, 0, 0.4f));
		blockBuilder.put(TransformType.FIRST_PERSON_LEFT_HAND,  get(0, 0, 0, 0, 225, 0, 0.4f));
		DEFAULT_BLOCK = new SimpleModelState(blockBuilder.build());

		TRSRTransformation itemThirdperson = get(0, 3, 1, 0, 0, 0, 0.55f);
		TRSRTransformation firstperson = get(1.13f, 3.2f, 1.13f, 0, -90, 25, 0.68f);
		ImmutableMap.Builder<TransformType, TRSRTransformation> itemBuilder = ImmutableMap.builder();
		itemBuilder.put(TransformType.GROUND,                  get(0, 2, 0, 0, 0, 0, 0.5f));
		itemBuilder.put(TransformType.HEAD,                    get(0, 13, 7, 0, 180, 0, 1));
		itemBuilder.put(TransformType.THIRD_PERSON_RIGHT_HAND, itemThirdperson);
		itemBuilder.put(TransformType.THIRD_PERSON_LEFT_HAND, leftify(itemThirdperson));
		itemBuilder.put(TransformType.FIRST_PERSON_RIGHT_HAND, firstperson);
		itemBuilder.put(TransformType.FIRST_PERSON_LEFT_HAND, leftify(firstperson));
		DEFAULT_ITEM = new SimpleModelState(itemBuilder.build());

		DEFAULT_TOOL = new SimpleModelState(ImmutableMap.of(
				TransformType.THIRD_PERSON_RIGHT_HAND, get(0, 4, 0.5f,         0, -90, 55, 0.85f),
				TransformType.THIRD_PERSON_LEFT_HAND,  get(0, 4, 0.5f,         0, 90, -55, 0.85f),
				TransformType.FIRST_PERSON_RIGHT_HAND, get(1.13f, 3.2f, 1.13f, 0, -90, 25, 0.68f),
				TransformType.FIRST_PERSON_LEFT_HAND,  get(1.13f, 3.2f, 1.13f, 0, 90, -25, 0.68f))); 
	}

	public static ModelManager getInstance() {
		return instance;
	}

	private TRSRTransformation get(float tx, float ty, float tz, float ax, float ay, float az, float s){
		return TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
				new Vector3f(tx / 16, ty / 16, tz / 16),
				TRSRTransformation.quatFromXYZDegrees(new Vector3f(ax, ay, az)),
				new Vector3f(s, s, s),
				null));
	}

	private TRSRTransformation leftify(TRSRTransformation transform){
		if(flipX == null){
			flipX = new TRSRTransformation(null, null, new Vector3f(-1, 1, 1), null);
		}
		return TRSRTransformation.blockCenterToCorner(flipX.compose(TRSRTransformation.blockCornerToCenter(transform)).compose(flipX));
	}

	@Override
	public void registerItemModel(Item item, int meta, String identifier) {
		ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(identifier));
	}

	@Override
	public void registerItemModel(Item item, int meta, String modID, String identifier) {
		ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(modID, identifier));
	}

	@Override
	public void registerItemModel(Item item, int meta) {
		ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(item));
	}

	@Override
	public void registerItemModel(Item item, ItemMeshDefinition definition) {
		ModelLoader.setCustomMeshDefinition(item, definition);
	}

	@Override
	public ModelResourceLocation getModelLocation(Item item) {
		String itemName = ForgeRegistries.ITEMS.getKey(item).getResourcePath();
		return getModelLocation(itemName);
	}

	@Override
	public ModelResourceLocation getModelLocation(String identifier) {
		return getModelLocation("modularmachines", identifier);
	}

	@Override
	public ModelResourceLocation getModelLocation(String modID, String identifier) {
		return new ModelResourceLocation(modID + ":" + identifier, "inventory");
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockClient(Block block) {
		if (block instanceof IItemModelRegister) {
			itemModelRegisters.add((IItemModelRegister) block);
		}
		if (block instanceof IStateMapperRegister) {
			stateMapperRegisters.add((IStateMapperRegister) block);
		}
		if (block instanceof IColoredBlock) {
			blockColorList.add((IColoredBlock) block);
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerItemClient(Item item) {
		if (item instanceof IItemModelRegister) {
			itemModelRegisters.add((IItemModelRegister) item);
		}
		if (item instanceof IColoredItem) {
			itemColorList.add((IColoredItem) item);
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerModuleModels() {
		for(IModuleContainer container : ModularMachinesApi.MODULE_CONTAINERS){
			if(container != null && container.getModule() != null){
				List<IModelInitHandler> handlers = container.getModule().getInitModelHandlers(container);
				if(handlers != null && !handlers.isEmpty()){
					for(IModelInitHandler handle : handlers){
						if(handle != null){
							handle.initModels(container);
						}
					}
				}
				MinecraftForge.EVENT_BUS.post(new ModuleEvents.ModuleModelInitEvent(container));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerModels() {
		for (IItemModelRegister itemModelRegister : itemModelRegisters) {
			Item item = null;
			if (itemModelRegister instanceof Block) {
				item = Item.getItemFromBlock((Block) itemModelRegister);
			} else if (itemModelRegister instanceof Item) {
				item = (Item) itemModelRegister;
			}

			if (item != null) {
				itemModelRegister.registerModel(item, this);
			}
		}

		for (IStateMapperRegister stateMapperRegister : stateMapperRegisters) {
			stateMapperRegister.registerStateMapper();
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerItemAndBlockColors() {
		Minecraft minecraft = Minecraft.getMinecraft();

		BlockColors blockColors = minecraft.getBlockColors();
		for (IColoredBlock blockColor : blockColorList) {
			if (blockColor instanceof Block) {
				blockColors.registerBlockColorHandler(ColoredBlockBlockColor.INSTANCE, (Block) blockColor);
			}
		}

		ItemColors itemColors = minecraft.getItemColors();
		for (IColoredItem itemColor : itemColorList) {
			if (itemColor instanceof Item) {
				itemColors.registerItemColorHandler(ColoredItemItemColor.INSTANCE, (Item) itemColor);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private static class ColoredItemItemColor implements IItemColor {
		public static final ColoredItemItemColor INSTANCE = new ColoredItemItemColor();

		private ColoredItemItemColor() {

		}

		@Override
		public int getColorFromItemstack(ItemStack stack, int tintIndex) {
			Item item = stack.getItem();
			if (item instanceof IColoredItem) {
				return ((IColoredItem) item).getColorFromItemstack(stack, tintIndex);
			}
			return 0xffffff;
		}
	}

	@SideOnly(Side.CLIENT)
	private static class ColoredBlockBlockColor implements IBlockColor {
		public static final ColoredBlockBlockColor INSTANCE = new ColoredBlockBlockColor();

		private ColoredBlockBlockColor() {

		}

		@Override
		public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
			Block block = state.getBlock();
			if (block instanceof IColoredBlock) {
				return ((IColoredBlock) block).colorMultiplier(state, world, pos, tintIndex);
			}
			return 0xffffff;
		}
	}

}
