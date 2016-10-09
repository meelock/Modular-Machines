package de.nedelosk.modularmachines.common.items;

import java.util.List;

import com.google.common.collect.Lists;

import de.nedelosk.modularmachines.api.modules.ModuleManager;
import de.nedelosk.modularmachines.api.modules.containers.IModuleItemProvider;
import de.nedelosk.modularmachines.api.modules.containers.ModuleItemProvider;
import de.nedelosk.modularmachines.api.modules.handlers.IAdvancedModuleContentHandler;
import de.nedelosk.modularmachines.api.modules.handlers.IModuleContentHandler;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.common.core.Registry;
import de.nedelosk.modularmachines.common.core.TabModularMachines;
import de.nedelosk.modularmachines.common.utils.IColoredItem;
import de.nedelosk.modularmachines.common.utils.Translator;
import forestry.api.core.IItemModelRegister;
import forestry.api.core.IModelManager;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemModuleContainer extends Item implements IColoredItem, IItemModelRegister {

	public ItemModuleContainer() {
		setCreativeTab(TabModularMachines.tabModules);
		setUnlocalizedName("module_container");
		setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
		IModuleItemProvider moduleProvider = itemStack.getCapability(ModuleManager.MODULE_PROVIDER_CAPABILITY, null);
		if(moduleProvider != null && moduleProvider.getItemStack() != null){
			if(player.isSneaking()){
				ItemStack providerStack = moduleProvider.getItemStack().copy();
				boolean dropItems = !world.isRemote;
				List<ItemStack> drops = Lists.newArrayList();
				for(IModuleState moduleState : moduleProvider) {
					if (moduleState != null) {
						if(dropItems){
							for(IModuleContentHandler handler : moduleState.getAllContentHandlers()){
								if(handler instanceof IAdvancedModuleContentHandler){
									drops.addAll(((IAdvancedModuleContentHandler)handler).getDrops());
								}
							}
						}
						moduleState.getModule().saveDataToItem(itemStack, moduleState);
					}
				}
				if(dropItems){
					for(ItemStack stack : drops){
						ItemHandlerHelper.giveItemToPlayer(player, stack);
					}
				}
				return ActionResult.newResult(EnumActionResult.SUCCESS, providerStack);
			}
		}
		return ActionResult.newResult(EnumActionResult.PASS, itemStack);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new ModuleItemProvider();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel(Item item, IModelManager manager) {
		manager.registerItemModel(item, 0);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return Registry.setUnlocalizedItemName(super.getUnlocalizedName(stack).replace("item.", ""));
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		IModuleItemProvider moduleProvider = stack.getCapability(ModuleManager.MODULE_PROVIDER_CAPABILITY, null);
		if(moduleProvider != null && moduleProvider.getItemStack() != null){
			return super.getItemStackDisplayName(stack) + ": " + moduleProvider.getItemStack().getDisplayName();
		}
		return super.getItemStackDisplayName(stack);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(Translator.translateToLocal("mm.tooltip.moduleContainer"));
		tooltip.add(TextFormatting.RED + Translator.translateToLocal("mm.tooltip.moduleContainer.warning"));
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List subItems) {
		ItemStack itemStack = new ItemStack(item);
		IModuleItemProvider moduleProvider = itemStack.getCapability(ModuleManager.MODULE_PROVIDER_CAPABILITY, null);
		moduleProvider.setItemStack(new ItemStack(Items.STICK));
		subItems.add(itemStack);
	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		IModuleItemProvider moduleProvider = stack.getCapability(ModuleManager.MODULE_PROVIDER_CAPABILITY, null);
		if(moduleProvider != null && moduleProvider.getItemStack() != null){
			ItemStack providerStack = moduleProvider.getItemStack();
			return Minecraft.getMinecraft().getItemColors().getColorFromItemstack(providerStack, tintIndex);
		}
		return -1;
	}
}