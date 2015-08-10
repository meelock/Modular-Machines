package nedelosk.modularmachines.common.blocks;

import java.util.List;
import nedelosk.modularmachines.common.ModularMachines;
import nedelosk.modularmachines.common.blocks.tile.TileModularMachine;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ModularMachineBlock extends ModularBlock {

	public ModularMachineBlock() {
		super(Material.iron);
		setStepSound(soundTypeMetal);
		setHardness(2.0F);
		setHarvestLevel("pickaxe", 1);
		setBlockName("modular.machine");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileModularMachine();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
		player.openGui(ModularMachines.instance, 0, player.worldObj, x, y, z);
		return true;
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs ptab, List list) {
	}

}