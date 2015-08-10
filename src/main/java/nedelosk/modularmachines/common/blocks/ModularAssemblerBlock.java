package nedelosk.modularmachines.common.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nedelosk.modularmachines.common.ModularMachines;
import nedelosk.modularmachines.common.blocks.tile.TileModularAssembler;
import nedelosk.nedeloskcore.utils.ItemUtils;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ModularAssemblerBlock extends ModularBlock {

	public ModularAssemblerBlock() {
		super(Material.iron);
		setStepSound(soundTypeMetal);
		setHardness(2.0F);
		setHarvestLevel("pickaxe", 1);
		setBlockName("modular.assembler");
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return -1;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileModularAssembler();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
		player.openGui(ModularMachines.instance, 0, player.worldObj, x, y, z);
		return true;
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs ptab, List list) {
		ItemStack stack = new ItemStack(item);
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("capacity", 1000000);
		list.add(stack);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		if(world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileModularAssembler)
		{
			TileModularAssembler tile = (TileModularAssembler) world.getTileEntity(x, y, z);
			for(Map.Entry<String, ItemStack[]> entry : tile.slot.entrySet())
			{
				ItemUtils.dropItem(world, x, y, z, entry.getValue());
			}
		}
		return super.getDrops(world, x, y, z, metadata, fortune);
	}

}