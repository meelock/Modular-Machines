package de.nedelosk.modularmachines.common.plugins.waila.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.nedelosk.modularmachines.api.modular.ModularManager;
import de.nedelosk.modularmachines.api.modules.integration.IWailaState;
import de.nedelosk.modularmachines.common.blocks.tile.TileModular;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProviderModular implements IWailaDataProvider {

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		TileModular modular = (TileModular) accessor.getTileEntity();
		IWailaState data = new WailaData(accessor, config);
		return modular.getCapability(ModularManager.MODULAR_HANDLER_CAPABILITY, null).getModular().getWailaHead(itemStack, currenttip, data);
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		TileModular modular = (TileModular) accessor.getTileEntity();
		IWailaState data = new WailaData(accessor, config);
		return modular.getCapability(ModularManager.MODULAR_HANDLER_CAPABILITY, null).getModular().getWailaBody(itemStack, currenttip, data);
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		TileModular modular = (TileModular) accessor.getTileEntity();
		IWailaState data = new WailaData(accessor, config);
		return modular.getCapability(ModularManager.MODULAR_HANDLER_CAPABILITY, null).getModular().getWailaTail(itemStack, currenttip, data);
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
		return null;
	}

	public static class WailaData implements IWailaState {

		private IWailaDataAccessor accessor;
		private IWailaConfigHandler config;

		public WailaData(IWailaDataAccessor accessor, IWailaConfigHandler config) {
			this.accessor = accessor;
			this.config = config;
		}

		@Override
		public World getWorld() {
			return accessor.getWorld();
		}

		@Override
		public EntityPlayer getPlayer() {
			return accessor.getPlayer();
		}

		@Override
		public Block getBlock() {
			return accessor.getBlock();
		}

		@Override
		public int getMetadata() {
			return accessor.getMetadata();
		}

		@Override
		public TileEntity getTileEntity() {
			return accessor.getTileEntity();
		}

		@Override
		public BlockPos getPosition() {
			return accessor.getPosition();
		}

		@Override
		public Vec3d getRenderingPosition() {
			return accessor.getRenderingPosition();
		}

		@Override
		public NBTTagCompound getNBTData() {
			return accessor.getNBTData();
		}

		@Override
		public int getNBTInteger(NBTTagCompound tag, String keyname) {
			return accessor.getNBTInteger(tag, keyname);
		}

		@Override
		public double getPartialFrame() {
			return accessor.getPartialFrame();
		}

		@Override
		public IBlockState getBlockState() {
			return accessor.getBlockState();
		}

		@Override
		public RayTraceResult getMOP() {
			return accessor.getMOP();
		}

		@Override
		public ItemStack getStack() {
			return accessor.getStack();
		}

		@Override
		public EnumFacing getSide() {
			return accessor.getSide();
		}

		@Override
		public Set<String> getModuleNames() {
			return config.getModuleNames();
		}

		@Override
		public HashMap<String, String> getConfigKeys(String modName) {
			return config.getConfigKeys(modName);
		}

		@Override
		public boolean getConfig(String key, boolean defvalue) {
			return config.getConfig(key, defvalue);
		}

		@Override
		public boolean getConfig(String key) {
			return config.getConfig(key);
		}
	}
}